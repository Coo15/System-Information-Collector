/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hust.soict.cs.system.infomation.collector;

/**
 *
 * @author ADMIN
 */

import java.util.List;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class Processes {
    private SystemInfo si = new SystemInfo();
    
    
    public Processes() {
        HardwareAbstractionLayer hardware = si.getHardware();
        OperatingSystem operatingSystem = si.getOperatingSystem();
        ComputerSystem computerSystem = hardware.getComputerSystem();
        
        List<OSProcess> processes = operatingSystem.getProcesses();
        
        
        System.out.println("Name - CPU - MEMORY - PID - PPID - User - Architecture - State");
        for (OSProcess p : processes) {
            StringBuilder info = new StringBuilder();
            info.append(p.getName());
            info.append(" - ").append(p.getProcessCpuLoadCumulative());
            info.append(" - ").append(p.getResidentSetSize());
            info.append(" - ").append(p.getProcessID());
            info.append(" - ").append(p.getParentProcessID());
            info.append(" - ").append(p.getUser());
            info.append(" - ").append(p.getBitness());
            info.append(" - ").append(p.getState());
            System.out.println(info.toString());
        }
    }
    
}
