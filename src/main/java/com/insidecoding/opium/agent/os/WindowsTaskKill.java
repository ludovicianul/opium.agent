package com.insidecoding.opium.agent.os;

import org.springframework.stereotype.Component;

@Component("windowsTaskKill")
public class WindowsTaskKill extends OsCommand {

  @Override
  public String getCommand() {
    return "TaskKill.exe";
  }

}
