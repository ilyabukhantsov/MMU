package com.mycompany.app.pageTableEntry;

public class PteImpl implements PageTableEntry {
    private int virtualPageNumber;
    private int physicalPageNumber;
    private boolean presence;
    private boolean reference;
    private boolean modification;
    private boolean inSaved;

    public PteImpl(int virtualPageNumber) {
        this.virtualPageNumber = virtualPageNumber;
        this.physicalPageNumber = -1;
        this.presence = false;
        this.reference = false;
        this.modification = false;
        this.inSaved = false;
    }

    public int getVirtualPageNumber() {
        return virtualPageNumber;
    }

    public boolean isPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }

    public boolean isModification() {
        return modification;
    }

    public void setModification(boolean modification) {
        this.modification = modification;
    }

    public boolean isInSaved() {
        return inSaved;
    }

    public void setInSaved(boolean inSaved) {
        this.inSaved = inSaved;
    }

    public int getPhysicalPageNumber() {
        return physicalPageNumber;
    }

    public void setPhysicalPageNumber(int physicalPageNumber) {
        this.physicalPageNumber = physicalPageNumber;
    }
}
