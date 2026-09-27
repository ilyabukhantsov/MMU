package com.mycompany.app.pageTableEntry;

public interface PageTableEntry {
    int getVirtualPageNumber();
    int getPhysicalPageNumber();
    boolean isPresence();
    boolean isReference();
    boolean isModification();
    boolean isInSaved();

    void setPresence(boolean presence);
    void setReference(boolean reference);
    void setModification(boolean modification);
    void setInSaved(boolean inSaved);
    void setPhysicalPageNumber(int physicalPageNumber);
}
