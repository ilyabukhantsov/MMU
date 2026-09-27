package com.mycompany.app.pageTableEntry;

public interface PageTableEntry{
  void setReference(boolean reference);
  void setModification(boolean reference);
  boolean isPresence();
  boolean isModification();
  void setPresence(boolean presence);
  void setPhysicalPageNumber(int physicalPageNumber);
  

}
