package com.mycompany.app.process;

import com.mycompany.app.pageTableEntry.PageTableEntry;

public interface Process {
    void Work() throws Exception;
    void InitWorkingSet(int HowMany) throws Exception;
    boolean isLifeLeft();
    int GetId();

    int GetLifeLeft();
    void LessLifeLeft(int number) throws Exception;
    PageTableEntry[] getPageTableEntry();
}
