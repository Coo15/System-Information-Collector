package tabs;

import startup.*;
import javax.swing.*;
import java.awt.*;


public class Startup extends JPanel {
    public Startup() {
        setLayout(new BorderLayout());
        JTabbedPane startupTabs = new JTabbedPane();
        
        String osName = System.getProperty("os.name").toLowerCase();
        
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            startupTabs.addTab("App", new LinuxApps());
            startupTabs.addTab("Cron", new CronTab());
        } else {
            startupTabs.addTab("App", new WinApps());
            startupTabs.addTab("Task", new TaskSchedule());
        }
        
        
        
        add(startupTabs, BorderLayout.CENTER);
    }
}
