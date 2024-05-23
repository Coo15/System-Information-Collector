/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


/**
 *
 * @author QUANG
 */

import java.awt.*;
import javax.swing.*;

public class SystemInformationCollector extends JFrame{
    
    public SystemInformationCollector() {
        Container cp = getContentPane();
        
        JTabbedPane tabs = new JTabbedPane();
        
        tabs.addTab("Overview", new SystemOverview());
        tabs.addTab("Performance", new Performance());
        tabs.addTab("Processes", new Processes());
        tabs.addTab("Services", new Services());
        tabs.addTab("Startup services", new Startup());
                
        add(tabs);
        
        setTitle("System Information Collector");
        setSize(1080, 920);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        new SystemInformationCollector();
    }
}