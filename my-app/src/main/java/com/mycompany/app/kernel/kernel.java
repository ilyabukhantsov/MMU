package com.mycompany.app.kernel;

import com.mycompany.app.MemoryManagmentUnit.MemoryManagmentUnit;
import com.mycompany.app.pageTableEntry.PageTableEntry;
import com.mycompany.app.process.Process;

public interface kernel{
  KernelResult start(int frame, String algorythm, MemoryManagmentUnit memoryManagmentUnit);
  void addProcess(Process process) throws Exception;
  void handlePageFault(int pid, PageTableEntry[] pte, int virtalpages) throws Exception;
}
