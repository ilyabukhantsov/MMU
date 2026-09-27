package com.mycompany.app.kernel;

import com.mycompany.app.frame.*;
import com.mycompany.app.process.Process;
import com.mycompany.app.MemoryManagmentUnit.MemoryManagmentUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

public class kernelImpl implements kernel {
  private String algorithm;
  private Deque<Process> processes;
  private List<FrameImpl> frames;

  // return stuff
  private int TotalAccess;
  private int PageFault;
  private double successTime;

  public kernelImpl(String algorithm, int numberOfFrames) {
    this.algorithm = algorithm;
    this.PageFault = 0;
    this.TotalAccess = 0;
    this.successTime = 0;    

    this.processes = new ArrayDeque<>();
    this.frames = new ArrayList<>();

    for (int i = 0; i < numberOfFrames; i++) {
      this.frames.add(new FrameImpl(i));
    }
  }

  public void addProcess(Process process) throws Exception {
    if (process == null) {
      throw new Exception("Процесс не може бути null");
    } else {
      this.processes.push(process);
    }
  }

  public KernelResult start(int frame, String algorythm, MemoryManagmentUnit memoryManagmentUnit) {
    System.out.println("Your algorithm is " + algorythm);

    while (!processes.isEmpty()) {
      Process currentProcess = processes.pop(); 
      
    }
    
    return null;
  }
}
