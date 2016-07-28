package com.insidecoding.opium.agent.os;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OsCommand {
  private static final int DEFAULT_PROC_TIMEOUT = 10 * 1000;
  private static final Logger LOG = LoggerFactory.getLogger(OsCommand.class);

  /**
   * Executes the given command and returns once the given "waitFor" string is found in the command
   * output.
   * 
   * @param waitFor
   *          - the String to wait for in the command output
   * @param commandParams
   *          the additional command params
   * @return the command output
   * @throws IOException
   *           if something goes wrong
   */
  public String executeAndWaitFor(String waitFor, String... commandParams) throws IOException {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      CommandLine commandLine = new CommandLine(this.getCommand());
      commandLine.addArguments(commandParams, false);

      DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
      DefaultExecutor executor = new DefaultExecutor();
      PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
      executor.setStreamHandler(streamHandler);

      executor.execute(commandLine, resultHandler);

      try {
        resultHandler.waitFor(DEFAULT_PROC_TIMEOUT);

        boolean found = false;
        while (!resultHandler.hasResult() && !found && !waitFor.isEmpty()) {
          if (outputStream.toString().indexOf(waitFor) != -1) {
            found = true;
          }
          Thread.sleep(50);
        }

        if (resultHandler.hasResult() && resultHandler.getExitValue() != 0) {
          throw new CommandFailedException(outputStream.toString());
        }

      } catch (InterruptedException e) {
        throw new CommandFailedException(e);
      }
      return outputStream.toString();
    }
  }

  public String execute(String... params) throws IOException {
    return this.executeAndWaitFor("", params);
  }

  public abstract String getCommand();

}
