/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.Arrays;
import java.util.List;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.NetworkParams;
import oshi.software.os.OperatingSystem;

/**
 *
 * @author ADMIN
 */
public class Network {
    private SystemInfo si = new SystemInfo();
    
    
    public Network() {
        HardwareAbstractionLayer hardware = si.getHardware();
        OperatingSystem operatingSystem = si.getOperatingSystem();
        ComputerSystem computerSystem = hardware.getComputerSystem();
        
        List<NetworkIF> networkIFs = hardware.getNetworkIFs(true);
        
        System.out.println(networkParameters(operatingSystem.getNetworkParams()));
        
        StringBuilder info;
        System.out.println("Name - Index - Speed - IPv4 - IPv6 - Mac");
        for (NetworkIF iF : networkIFs) {
            info = new StringBuilder();
            
            info.append(iF.getName());
            info.append(" - ").append(iF.getIndex());
            info.append(" - ").append(iF.getSpeed());
            info.append(" - ").append(formatIPaddr(iF.getIPv4addr()));
            info.append(" - ").append(formatIPaddr(iF.getIPv6addr()));
            info.append(" - ").append(iF.getMacaddr());
            
            System.out.println(info.toString());
        }
    }
    
    private String networkParameters(NetworkParams networkParams) {
        StringBuilder info = new StringBuilder();
        
        info.append("*****\n");
        info.append("Domain Name: ").append(networkParams.getDomainName());
        info.append("\nHost Name: ").append(networkParams.getHostName());
        info.append("\nIPv4 Default Gateway: ").append(networkParams.getIpv4DefaultGateway());
        info.append("\nIPv6 Default Gateway: ").append(networkParams.getIpv6DefaultGateway());
        info.append("\nDNS Server: ").append(Arrays.toString(networkParams.getDnsServers()));
        info.append("\n*****\n");
        
        return info.toString();
    }
    private String formatIPaddr(String[] ipAddress) {
        StringBuilder ip = new StringBuilder();
        boolean first = true;
        
        for (String c : ipAddress) {
            if (first) {
                first = false;
            } else {
                ip.append(";");
            }
            ip.append(c);
        }
        
        return ip.toString();
    }
}
