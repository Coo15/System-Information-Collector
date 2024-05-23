/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ADMIN
 */

import javax.swing.*;

public class Performance extends JPanel{
    public Performance(){
    JTabbedPane percTabs = new JTabbedPane();
    
    percTabs.addTab("CPU", new CPU());
    percTabs.addTab("Memory", new Memory());
    percTabs.addTab("Ethernet", new Ethernet());
    percTabs.addTab("Wi-Fi", new Wifi());
    percTabs.addTab("Disk", new Disk());
    
    add(percTabs);
    
    }
    
}
