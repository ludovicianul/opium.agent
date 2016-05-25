package com.insidecoding.opium.agent.os;

import org.springframework.stereotype.Component;

@Component("linuxTaskKill")
public class LinuxTaskKill extends OsCommand {

  @Override
  public String getCommand() {
    return "kill";
  }

}
