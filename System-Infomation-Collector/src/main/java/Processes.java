/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author ADMIN
 */

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class Processes extends JPanel{
    private SystemInfo si = new SystemInfo();
    private String[] columnNames = {"Name", "CPU", "MEMORY", "PID","PPID", "User", "Architecture","State"};
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane sp;
    
    public Processes() {
        HardwareAbstractionLayer hardware = si.getHardware();
        OperatingSystem operatingSystem = si.getOperatingSystem();
        ComputerSystem computerSystem = hardware.getComputerSystem();
        
        List<OSProcess> processes = operatingSystem.getProcesses();
        
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        sp = new JScrollPane(table);
        
        setLayout(new BorderLayout());
        add(sp, BorderLayout.CENTER);
        tableModel.setRowCount(0);
        
        for (OSProcess p : processes) {
            tableModel.addRow(new Object[]{p.getName(),p.getProcessCpuLoadCumulative(),p.getResidentSetSize(),p.getProcessID(),p.getParentProcessID(),p.getUser(),p.getBitness(),p.getState()});
            
        }
    }
    
}
