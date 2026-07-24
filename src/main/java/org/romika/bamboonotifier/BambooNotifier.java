package org.romika.bamboonotifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class BambooNotifier {

    public static void main(String[] args) {
        // Deactivate headless to have SystemTray support
        System.setProperty("java.awt.headless", "false");

        SpringApplication app = new SpringApplication(BambooNotifier.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        app.run(args);
    }
}