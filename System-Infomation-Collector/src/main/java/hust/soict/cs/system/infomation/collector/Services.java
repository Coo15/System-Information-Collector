/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hust.soict.cs.system.infomation.collector;

import java.util.List;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSService;
import oshi.software.os.OperatingSystem;

/**
 *
 * @author ADMIN
 */
public class Services {
    private SystemInfo si = new SystemInfo();
    
    public Services() {
        HardwareAbstractionLayer hardware = si.getHardware();
        OperatingSystem operatingSystem = si.getOperatingSystem();
        ComputerSystem computerSystem = hardware.getComputerSystem();
        
        List<OSService> services = operatingSystem.getServices();
        StringBuilder info;
        System.out.println("Name - PID - State");
        for (OSService service : services) {
            info = new StringBuilder();
            
            info.append(service.getName());
            info.append(" - ").append(service.getProcessID());
            info.append(" - ").append(service.getState());
            
            System.out.println(info.toString());
        }
    }
}
