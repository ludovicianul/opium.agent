package com.insidecoding.opium.agent.service;

import com.insidecoding.opium.agent.os.OsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

public abstract class AppiumService {
    private static final Logger LOG = LoggerFactory.getLogger(AppiumService.class);

    private static final String WAIT_FOR = "Appium REST http interface";

    @Autowired
    @Qualifier("appium")
    private OsCommand appiumCmd;

    public String startServer(String command) throws IOException {
        LOG.info("Starting server...");
        return appiumCmd.executeAndWaitFor(WAIT_FOR, command);
    }

    public abstract String stopServer(String port) throws IOException;
}
