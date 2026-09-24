package com.mycompany.app.MemoryManagmentUnit;

import com.mycompany.app.pageTableEntry.PageTableEntry;

public interface MemoryManagmentUnit{
  void Access(int PID, PageTableEntry[] pte, int vpage) throws Exception;
}
