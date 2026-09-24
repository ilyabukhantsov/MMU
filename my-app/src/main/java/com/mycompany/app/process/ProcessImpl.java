package com.mycompany.app.process;

import com.mycompany.app.MemoryManagmentUnit.MemoryManagmentUnit;
import com.mycompany.app.pageTableEntry.PageTableEntry;
import com.mycompany.app.pageTableEntry.PteImpl;

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Random;

public class ProcessImpl implements Process{
  private int id;
  private int lifeLeft;
  private PteImpl[] pageTableEntry;
  private ArrayList<Integer> workingSet = new ArrayList<>();
  private MemoryManagmentUnit mmu;

  public ProcessImpl(int id, int lifeLeft, int numberOfPages, MemoryManagmentUnit mmu){
    try {
      InitWorkingSet(8);
    } catch (Exception e){
      System.err.println("Помилка ініціалізації: " + e.getMessage());
    }
    PteImpl[] pte = new PteImpl[numberOfPages];
    this.mmu = mmu;
    for (int i = 0; i < numberOfPages; i++) {
      pte[i] = new PteImpl(i);
    }
    this.id = id;
    this.lifeLeft = lifeLeft;
    System.out.println("Успішно ініціалізовано Процесс!");
  }

  //Gettes and Setters
  public int GetId(){
    return this.id;
  }
  public int GetLifeLeft(){
    return this.lifeLeft;
  }
  public boolean isLifeLeft(){
    if (this.lifeLeft > 0){
      return true;
    } else{
      return false;
    }
  }
  public void LessLifeLeft(int number) throws Exception{
    if (this.lifeLeft <= 0){
      throw new Exception("No life left!");
    }
    this.lifeLeft--;
  }

  public void Work() throws Exception{
    if (!isLifeLeft()){
      System.out.println("Process finished his life time!");
      return;
    }
    int virtalPageId;
    Random rand = new Random();

    int randomNum = ThreadLocalRandom.current().nextInt(1, 101);
    if (!this.workingSet.isEmpty() && randomNum <= 90){
        int casino = rand.nextInt(this.workingSet.size());
        virtalPageId = this.workingSet.get(casino);
    } else {
        virtalPageId = rand.nextInt(this.pageTableEntry.length);
    }

    try {
      this.mmu.Access(this.id, this.pageTableEntry, virtalPageId);
    } catch (Exception e){
      System.err.println("Помилка доступу: " + e.getMessage());
    }
  }
  public void InitWorkingSet(int numberOfPage) throws Exception{
    if (numberOfPage > pageTableEntry.length){
      throw new Exception("Imposible situation!");
    }
    this.workingSet.clear();

    for (int i = 0; i < numberOfPage; i++) {
      this.workingSet.add(i);
    }
  }
}
