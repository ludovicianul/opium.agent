package com.insidecoding.opium.agent.os;

import org.springframework.stereotype.Component;

@Component("appium")
public class AppiumCommand extends OsCommand {

  @Override
  public String getCommand() {
    return "C:\\Users\\milie\\AppData\\Roaming\\npm\\appium.cmd";
  }

}
