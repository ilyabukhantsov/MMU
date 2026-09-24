package com.mycompany.app.pageTableEntry;

public class PteImpl implements PageTableEntry{
    private boolean presence;
    private boolean reference;
    private boolean modification;
    private boolean inSaved;
    private int physicalPageNumber;

    public PteImpl(int physicalPageNumber) {
        this.presence = false;
        this.reference = false;
        this.modification = false;
        this.inSaved = false;
        this.physicalPageNumber = physicalPageNumber;
    }


    //Geters and Setters
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
