package com.mycompany.app.frame;

public class FrameImpl implements frame{
  private int ownerID;
  private int frameID;
  private int virtualPageNumberId;

  public FrameImpl(int number){
    this.frameID = number;
    this.ownerID = -1;
    this.virtualPageNumberId = -1;
  }

  // Getters and Setters
  public int GetOwnerId(){
    return this.ownerID;
  }
  public int GetFrameId(){
    return this.frameID;
  }
  public int GetVirtualPageNumberId(){
    return this.virtualPageNumberId;
  }
  public void SetOwnerId(int id){
    this.ownerID = id;
  }
  public void SetFrameId(int id){
    this.frameID = id;
  }
  public void SetVirtualPageNumberId(int id){
    this.virtualPageNumberId = id;
  }
}
