package com.insidecoding.opium.agent.rest.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class HealthCheckController {

  @ResponseStatus(code = HttpStatus.OK)
  @RequestMapping(value = "/ping", method = RequestMethod.HEAD)
  public void ping() {
  }
}
