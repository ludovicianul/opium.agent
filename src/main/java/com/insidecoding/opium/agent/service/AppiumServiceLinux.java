package com.insidecoding.opium.agent.service;

import com.insidecoding.opium.agent.os.CommandFailedException;
import com.insidecoding.opium.agent.os.OsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

public class AppiumServiceLinux extends AppiumService {
    private static final Logger LOG = LoggerFactory.getLogger(AppiumServiceLinux.class);
    @Autowired
    @Qualifier("lsof")
    private OsCommand lsofCmd;

    @Autowired
    @Qualifier("linuxTaskKill")
    private OsCommand taskKill;

    @Override
    public String stopServer(String port) throws IOException {
        LOG.info("Stopping server on port:" + port);
        String pid = "";
        try {
            /* If not found running on port the method will throw CommandFailedException */
            pid = lsofCmd.execute("-t", "-i:" + port);
        } catch (Exception e) {
            throw new CommandFailedException("Service not running on supplied port: " + port);
        }

        LOG.info("Stopping pid: " + pid);
        try {
            return taskKill.execute("-9", pid);
        } catch (Exception e) {
            throw new CommandFailedException("Service not running on supplied port: " + port);
        }

    }

}
