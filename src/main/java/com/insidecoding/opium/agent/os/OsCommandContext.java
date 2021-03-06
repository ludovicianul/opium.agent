package com.insidecoding.opium.agent.os;

import com.insidecoding.opium.agent.service.AppiumService;
import com.insidecoding.opium.agent.service.AppiumServiceLinux;
import com.insidecoding.opium.agent.service.AppiumServiceWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OsCommandContext {

    @Bean(name = "appiumService")
    public AppiumService windowsTaskKill() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return new AppiumServiceWindows();
        }

        return new AppiumServiceLinux();
    }
}
