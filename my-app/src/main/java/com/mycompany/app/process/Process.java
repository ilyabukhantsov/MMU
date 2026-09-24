package com.mycompany.app.process;

public interface Process{
  void Work() throws Exception;
  void InitWorkingSet(int HowMany) throws Exception;
}
