package com.insidecoding.opium.agent.os;

import org.springframework.stereotype.Component;

@Component("lsof")
public class LsofCommand extends OsCommand {

  @Override
  public String getCommand() {
    return "lsof";
  }

}
