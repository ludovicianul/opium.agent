package com.insidecoding.opium.agent.os;

import org.springframework.stereotype.Component;

@Component("netstat")
public class NetstatCommand extends OsCommand {

  @Override
  public String getCommand() {
    return "netstat";
  }

}
