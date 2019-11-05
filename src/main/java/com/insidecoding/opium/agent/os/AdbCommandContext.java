package com.insidecoding.opium.agent.os;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdbCommandContext {
    @Bean(name = "appium")
    public OsCommand windowsTaskKill() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return new AppiumCommandWindows();
        }

        return new AppiumCommandLinux();
    }
}
