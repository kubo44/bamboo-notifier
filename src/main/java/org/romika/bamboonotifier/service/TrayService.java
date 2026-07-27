package org.romika.bamboonotifier.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrayService {

    private final ConfigurableApplicationContext context;

    @Value("${logging.file.name:bamboo-notifier.log}")
    private String logFileName;

    private TrayIcon trayIcon;

    @PostConstruct
    public void init() {
        if (!SystemTray.isSupported()) {
            log.warn("System tray is not supported on this platform.");
            return;
        }

        EventQueue.invokeLater(() -> {
            try {
                SystemTray tray = SystemTray.getSystemTray();

                PopupMenu popupMenu = new PopupMenu();

                MenuItem openLogItem = new MenuItem("View logfile");
                openLogItem.addActionListener(e -> openLogFile());

                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(e -> exitApplication());

                popupMenu.add(openLogItem);
                popupMenu.addSeparator();
                popupMenu.add(exitItem);

                trayIcon = new TrayIcon(loadTrayImage(), "Bamboo Notifier", popupMenu);
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);

                log.info("Tray icon initialized.");
            } catch (Exception ex) {
                log.error("Failed to initialize tray icon", ex);
            }
        });
    }

    public void showInfo(String title, String message) {
        showMessage(title, message, TrayIcon.MessageType.INFO);
    }

    public void showWarning(String title, String message) {
        showMessage(title, message, TrayIcon.MessageType.WARNING);
    }

    public void showError(String title, String message) {
        showMessage(title, message, TrayIcon.MessageType.ERROR);
    }

    public void showMessage(String title, String message, TrayIcon.MessageType type) {
        if (trayIcon == null) {
            log.info("Tray message [{}]: {}", title, message);
            return;
        }

        EventQueue.invokeLater(() -> {
            try {
                trayIcon.displayMessage(title, message, type);
            } catch (Exception ex) {
                log.error("Failed to display tray message", ex);
            }
        });
    }

    private Image loadTrayImage() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/tray-icon.png")) {
            if (is == null) {
                throw new IOException("Resource /tray-icon.png not found");
            }
            return ImageIO.read(is);
        }
    }

    private void openLogFile() {
        try {
            File logFile = new File(logFileName).getAbsoluteFile();

            if (!logFile.exists()) {
                showInfo("Bamboo Notifier", "Log file does not exist yet");
                return;
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(logFile);
            } else {
                log.warn("Desktop API is not supported.");
            }
        } catch (Exception ex) {
            log.error("Cannot open logfile", ex);
            showError("Bamboo Notifier", "Cannot open logfile");
        }
    }

    private void exitApplication() {
        try {
            if (trayIcon != null) {
                SystemTray.getSystemTray().remove(trayIcon);
            }
        } catch (Exception ex) {
            log.warn("Failed to remove tray icon", ex);
        }

        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }

    @PreDestroy
    public void cleanup() {
        if (trayIcon != null && SystemTray.isSupported()) {
            SystemTray.getSystemTray().remove(trayIcon);
        }
    }
}
