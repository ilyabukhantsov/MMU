package com.mycompany.app.MemoryManagmentUnit;

import com.mycompany.app.kernel.kernel;
import com.mycompany.app.pageTableEntry.PageTableEntry;

public class mmuImpl implements MemoryManagmentUnit{
  private kernel Kernel;

  public mmuImpl(kernel Kernel){
    this.Kernel = Kernel;

  }

  public void Access(int PID, PageTableEntry[] pte, int virtualPageNumber) throws Exception{
    if (virtualPageNumber >= pte.length || virtualPageNumber  < 0) {
      throw new Exception("Вийшло за межі допустимих значень " + virtualPageNumber);
    }

      PageTableEntry tableEntry = pte[virtualPageNumber];

        if (!tableEntry.isPresence()) {
            System.out.printf("PAGE FAULT! Процесс %d, Віртуальна сторінка %d\n", PID, virtualPageNumber);
            
            try {
            Kernel.handlePageFault(PID, pte, virtualPageNumber);
            } catch (Exception err){
              throw new Exception("Не вийшло опрацювати PAGE FAULT!");
            }
        }

        tableEntry.setReference(true);
        tableEntry.setModification(true);
    }
}
