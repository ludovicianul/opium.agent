package com.insidecoding.opium.agent.os;

import java.io.IOException;

public class CommandFailedException extends IOException {

  private static final long serialVersionUID = -4624446620468640949L;

  public CommandFailedException(int value) {
    super(String.valueOf(value));
  }

  public CommandFailedException(Exception e) {
    super(e);
  }

  public CommandFailedException(String message) {
    super(message);
  }
}
