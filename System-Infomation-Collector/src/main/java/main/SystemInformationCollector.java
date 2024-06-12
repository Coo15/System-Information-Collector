package main;


import tabs.*;
import java.awt.*;
import javax.swing.*;
import tabs.Performance;

public class SystemInformationCollector extends JFrame{
    
    public SystemInformationCollector() {
        Container cp = getContentPane();
        
        JTabbedPane tabs = new JTabbedPane();
        
        tabs.addTab("Performance", new Performance());
        tabs.addTab("Network", new Network());
        tabs.addTab("Processes", new Processes());
        tabs.addTab("Services", new Services());
        tabs.addTab("Startup services", new Startup());
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            tabs.addTab("Cron", new CronTab());
        }
        tabs.addTab("Overview", new SystemOverview());
        
        add(tabs);
        
        setTitle("System Information Collector");
        setSize(1280, 960);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        new SystemInformationCollector();
    }
}
