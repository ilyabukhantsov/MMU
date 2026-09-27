package com.mycompany.app.MemoryManagmentUnit;

import com.mycompany.app.kernel.Kernel;
import com.mycompany.app.pageTableEntry.PageTableEntry;
import java.util.Random;

public class MmuImpl implements MemoryManagmentUnit {
    private Kernel kernel;
    private Random random;

    public MmuImpl(Kernel kernel) {
        this.kernel = kernel;
        this.random = new Random();
    }

    public void Access(int PID, PageTableEntry[] pte, int virtualPageNumber) throws Exception {
        if (pte == null || virtualPageNumber < 0 || virtualPageNumber >= pte.length) {
            throw new IllegalArgumentException("Вийшло за межі допустимих значень сторінки: " + virtualPageNumber);
        }

        PageTableEntry tableEntry = pte[virtualPageNumber];

        if (!tableEntry.isPresence()) {
            System.out.printf("PAGE FAULT! Процес %d, Віртуальна сторінка %d\n", PID, virtualPageNumber);

            try {
                kernel.handlePageFault(PID, pte, virtualPageNumber);
            } catch (Exception err) {
                throw new Exception("Не вийшло опрацювати PAGE FAULT для PID " + PID, err);
            }
        }

        tableEntry.setReference(true);

        boolean isWriteOperation = random.nextInt(100) < 30;
        if (isWriteOperation) {
            tableEntry.setModification(true);
        }
    }
}
