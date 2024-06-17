package main;

import tabs.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SystemInformationCollector extends JFrame {

    public SystemInformationCollector() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        
        JMenuBar menu = new JMenuBar();
        
        JMenu aboutMenu = new JMenu("About");
        menu.add(aboutMenu);
        aboutMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("About");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(620, 360);
                frame.add(new About());
                frame.setLocationRelativeTo(null); 
                frame.setVisible(true);
            }
        });
        
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Performance", new Performance());
        tabs.addTab("Network", new Network());
        tabs.addTab("Processes", new Processes());
        tabs.addTab("Services", new Services());
        tabs.addTab("Startup", new Startup());
        tabs.addTab("Overview", new SystemOverview());

        cp.add(tabs, BorderLayout.CENTER);
        
        setJMenuBar(menu);
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
