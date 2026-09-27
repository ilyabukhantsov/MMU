package com.mycompany.app.kernel;

import com.mycompany.app.frame.*;
import com.mycompany.app.process.Process;
import com.mycompany.app.MemoryManagmentUnit.MemoryManagmentUnit;
import com.mycompany.app.pageTableEntry.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Random;

public class KernelImpl implements Kernel {
    private String algorithm;
    private Deque<Process> processes;
    private List<FrameImpl> frames;

    private int TotalAccess;
    private int PageFault;

    private Random random;

    public KernelImpl(String algorithm, int numberOfFrames) {
        this.algorithm = algorithm;
        this.PageFault = 0;
        this.TotalAccess = 0;

        this.processes = new ArrayDeque<>();
        this.frames = new ArrayList<>();
        this.random = new Random();

        for (int i = 0; i < numberOfFrames; i++) {
            this.frames.add(new FrameImpl(i));
        }
    }

    public void addProcess(Process process) throws Exception {
        if (process == null) {
            throw new Exception("Процес не може бути null");
        } else {
            this.processes.push(process);
        }
    }

    public KernelResult start(int frame, String algorythm, MemoryManagmentUnit memoryManagmentUnit) {
        if (algorythm != null && !algorythm.isEmpty()) {
            this.algorithm = algorythm;
        }
        System.out.println("Your algorithm is " + this.algorithm);

        int stepCount = 0;

        while (!processes.isEmpty()) {
            Process currentProcess = processes.pop();

            try {
                this.TotalAccess++;
                stepCount++;

                currentProcess.Work();

                if (this.algorithm.equalsIgnoreCase("nru") && stepCount % 10 == 0) {
                    resetReferenceBits();
                }
            } catch (Exception e) {
                System.err.println("Не вийшло опрацювати процес: " + e.getMessage());
            }

            if (!currentProcess.isLifeLeft()) {
                System.out.println("--- Process " + currentProcess.GetId() + " FINISHED ---");
                makeMemoryFree(currentProcess.GetId());
            } else {
                this.processes.addLast(currentProcess);
            }
        }

        System.out.println("\n=== Симуляцію закінченно ===");

        return calculateResult();
    }

    public void handlePageFault(int pid, PageTableEntry[] pte, int virtalPage) throws Exception {
        if (pid < 0 || virtalPage < 0 || pte == null || virtalPage >= pte.length || pte[virtalPage] == null) {
            throw new IllegalArgumentException("Не правильна сторінка " + virtalPage + " для процессу з " + pid);
        }

        this.PageFault++;

        FrameImpl targetFrame = selectFrame();

        if (targetFrame == null) {
            throw new IllegalStateException("Не вдалося знайти вільний або кандидатний фрейм");
        }

        swapFrame(pid, pte, virtalPage, targetFrame);
    }

    private boolean isFrameFree(FrameImpl frame) {
        return frame.GetOwnerId() == -1;
    }

    private FrameImpl selectFrame() {
        for (FrameImpl frame : frames) {
            if (isFrameFree(frame)) {
                return frame;
            }
        }

        if (this.algorithm.equalsIgnoreCase("nru")) {
            return selectNRUFrame();
        }

        return selectRandomFrame();
    }

    private FrameImpl selectRandomFrame() {
        List<FrameImpl> busyFrames = new ArrayList<>();
        for (FrameImpl frame : frames) {
            if (!isFrameFree(frame)) {
                busyFrames.add(frame);
            }
        }
        if (busyFrames.isEmpty()) return null;

        return busyFrames.get(random.nextInt(busyFrames.size()));
    }

    private FrameImpl selectNRUFrame() {
        List<FrameImpl>[] classes = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            classes[i] = new ArrayList<>();
        }

        for (FrameImpl frame : frames) {
            if (isFrameFree(frame)) continue;

            PageTableEntry pte = getPTEForFrame(frame);
            if (pte == null) {
                classes[0].add(frame);
                continue;
            }

            int classIdx = 0;
            if (pte.isReference()) classIdx += 2;
            if (pte.isModification()) classIdx += 1;

            classes[classIdx].add(frame);
        }

        for (int i = 0; i < 4; i++) {
            if (!classes[i].isEmpty()) {
                return classes[i].get(random.nextInt(classes[i].size()));
            }
        }

        return selectRandomFrame();
    }

    private void swapFrame(int pid, PageTableEntry[] pte, int virtalPage, FrameImpl targetFrame) {
        if (!isFrameFree(targetFrame)) {
            evictFrame(targetFrame);
        }

        targetFrame.SetOwnerId(pid);
        targetFrame.SetVirtualPageNumberId(virtalPage);

        PageTableEntry entry = pte[virtalPage];
        entry.setPresence(true);
        entry.setPhysicalPageNumber(targetFrame.GetFrameId());
        entry.setReference(true);
        entry.setModification(false);

        System.out.println("[KERNEL] Loaded PID " + pid + " VPage " + virtalPage + " -> Frame " + targetFrame.GetFrameId());
    }

    private void evictFrame(FrameImpl frame) {
        int ownerPID = frame.GetOwnerId();
        int vPageToEvict = frame.GetVirtualPageNumberId();

        for (Process process : processes) {
            if (process.GetId() == ownerPID) {
                PageTableEntry[] table = process.getPageTableEntry();
                if (vPageToEvict >= 0 && vPageToEvict < table.length) {
                    PageTableEntry pte = table[vPageToEvict];
                    if (pte != null && pte.isPresence()) {
                        pte.setPresence(false);
                        pte.setPhysicalPageNumber(-1);

                        if (pte.isModification()) {
                            pte.setInSaved(true);
                        }

                        pte.setModification(false);
                        pte.setReference(false);

                        System.out.println("[KERNEL] Evicted PID " + ownerPID + " VPage " + vPageToEvict + " from Frame " + frame.GetFrameId());
                    }
                }
                break;
            }
        }

        frame.SetOwnerId(-1);
        frame.SetVirtualPageNumberId(-1);
    }

    private void resetReferenceBits() {
        for (FrameImpl frame : frames) {
            if (!isFrameFree(frame)) {
                PageTableEntry pte = getPTEForFrame(frame);
                if (pte != null) {
                    pte.setReference(false);
                }
            }
        }
    }

    private PageTableEntry getPTEForFrame(FrameImpl frame) {
        if (isFrameFree(frame)) return null;

        for (Process process : processes) {
            if (process.GetId() == frame.GetOwnerId()) {
                int vPage = frame.GetVirtualPageNumberId();
                PageTableEntry[] table = process.getPageTableEntry();
                if (vPage >= 0 && vPage < table.length) {
                    return table[vPage];
                }
            }
        }
        return null;
    }

    private void makeMemoryFree(int pid) {
        for (Process process : processes) {
            if (process.GetId() == pid) {
                for (PageTableEntry pte : process.getPageTableEntry()) {
                    if (pte != null && pte.isPresence()) {
                        pte.setPresence(false);
                        pte.setPhysicalPageNumber(-1);
                    }
                }
                break;
            }
        }

        for (FrameImpl frame : frames) {
            if (frame.GetOwnerId() == pid) {
                frame.SetOwnerId(-1);
                frame.SetVirtualPageNumberId(-1);
            }
        }
    }
    private KernelResult calculateResult() {
        int successfulAccesses = TotalAccess - PageFault;
        
        double succesTime = (TotalAccess > 0) ? ((double) successfulAccesses / TotalAccess * 100.0) : 0.0;

        System.out.printf("Total Accesses: %d | Page Faults: %d | Success Rate: %.2f%%%n", TotalAccess, PageFault, succesTime);

        return new KernelResult(TotalAccess, PageFault, succesTime);
    }
}
