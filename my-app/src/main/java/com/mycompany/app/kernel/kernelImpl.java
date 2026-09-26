package com.mycompany.app.kernel;

import com.mycompany.app.process.Process;
import java.util.List;

public class kernelImpl implements kernel{
  private String algorithm;
  private List<Process> processes;

  public kernelImpl(String algorithm){
    this.algorithm = algorithm;
  }

  public void addProcess(Process process) throws Exception{
    if (process != null){
      throw new Exception("Процесс не може бути null");
    } else{
      this.processes.add(process);
    }
  }
}
