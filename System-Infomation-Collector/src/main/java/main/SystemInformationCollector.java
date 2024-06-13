package main;

import tabs.*;
import java.awt.*;
import javax.swing.*;

public class SystemInformationCollector extends JFrame {

    public SystemInformationCollector() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Performance", new Performance());
        tabs.addTab("Network", new Network());
        tabs.addTab("Processes", new Processes());
        tabs.addTab("Services", new Services());
        tabs.addTab("Startup", new Startup());
        tabs.addTab("Overview", new SystemOverview());

        cp.add(tabs, BorderLayout.CENTER);

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
