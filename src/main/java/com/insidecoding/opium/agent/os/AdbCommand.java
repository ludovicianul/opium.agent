package com.insidecoding.opium.agent.os;

import org.springframework.stereotype.Component;

@Component("adb")
public class AdbCommand extends OsCommand {

    @Override
    public String getCommand() {
        return "adb";
    }

}
