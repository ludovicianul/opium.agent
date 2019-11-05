package com.insidecoding.opium.agent.rest.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.insidecoding.opium.agent.service.AppiumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.constraints.Pattern;
import java.io.IOException;

@Controller
public class AppiumController {
    private static final Logger LOG = LoggerFactory.getLogger(AppiumController.class);

    @Autowired
    @Qualifier("appiumService")
    private AppiumService appiumService;

    @RequestMapping(value = "/appium/{port}", method = RequestMethod.DELETE)
    public ResponseEntity<String> stop(@PathVariable @Validated int port) {
        String result = "";
        if (port != 0) {
            try {
                result = this.appiumService.stopServer(String.valueOf(port));
                LOG.info("Command completed successful: " + result);
            } catch (IOException e) {
                LOG.error("Could not stop service on port: " + port, e);
                return new ResponseEntity<>("Error while stopping appium: " + e.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }

        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/appium", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> execute(@RequestBody Appium data) {
        LOG.info("Got command: " + data);
        String result = "";

        if (!StringUtils.isEmpty(data.getCommand())) {
            try {
                result = appiumService.startServer(data.getCommand());
                LOG.info("Command completed successful: " + result);
            } catch (IOException e) {
                LOG.error("Command not executed: " + data, e);
                return new ResponseEntity<>("Error while starting appium: " + e.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    static class Appium {

        @Pattern(regexp = "\\d+")
        private String command;

        @JsonCreator
        public Appium(@JsonProperty("command") String cmd) {
            this.command = cmd;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        @Override
        public String toString() {
            return "Appium [command=" + command + "]";
        }
    }
}