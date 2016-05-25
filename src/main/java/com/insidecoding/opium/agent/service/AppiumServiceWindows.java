package com.insidecoding.opium.agent.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.insidecoding.opium.agent.os.CommandFailedException;
import com.insidecoding.opium.agent.os.OsCommand;

@Component("appiumServiceWindows")
public class AppiumServiceWindows extends AppiumService {
  private static final Logger LOG = LoggerFactory.getLogger(AppiumServiceWindows.class);

  @Autowired
  @Qualifier("netstat")
  private OsCommand netstatCmd;

  @Autowired
  @Qualifier("windowsTaskKill")
  private OsCommand taskKill;

  public String stopServer(String port) throws IOException {
    LOG.info("Stopping service on port: " + port);

    String netstat = netstatCmd.execute("-a", "-n", "-o");
    String[] lines = netstat.split(System.getProperty("line.separator"));
    String portLine = null;
    for (String line : lines) {
      if (line.indexOf(":" + port) != -1) {
        portLine = line;
        break;
      }
    }
    if (portLine != null) {
      String[] lineData = portLine.split(" +");
      String pid = lineData[lineData.length - 1].trim();

      LOG.info("Stopping pid: " + pid);
      return taskKill.execute("/F", "/PID", pid);
    }

    throw new CommandFailedException("Service not running on supplied port: " + port);
  }

}
