/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author ADMIN
 */

import cn.hutool.core.io.unit.DataSizeUtil;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.lang3.StringUtils;
import oshi.hardware.*;
import oshi.SystemInfo;
import oshi.software.os.*;

public class SystemOverview extends JPanel{
    private SystemInfo si = new SystemInfo();
    
    public SystemOverview() {
        HardwareAbstractionLayer hardware = si.getHardware();
        OperatingSystem operatingSystem = si.getOperatingSystem();
        ComputerSystem computerSystem = hardware.getComputerSystem();
        
        String pcName = computerSystem.getManufacturer() + " " + computerSystem.getModel();
        String os = operatingSystem.toString();
        String cpu = hardware.getProcessor().getProcessorIdentifier().getName();
        String memory = infoMemory(hardware.getMemory());
        String graphicsCards = infoGraphicsCard(hardware.getGraphicsCards());
        String disk = infoDisk(hardware.getDiskStores());
        String soundCard = infoSoundCard(hardware.getSoundCards());
        String power = infoPower(hardware.getPowerSources());
        
        setLayout(new GridLayout(8,2,0,0));
        
        add(new JLabel("Name: "));
        add(new JLabel(pcName));
        
        add(new JLabel("Operating System: "));
        add(new JLabel(os));
        
        add(new JLabel("CPU: "));
        add(new JLabel(cpu));
        
        add(new JLabel("Memory: "));
        add(new JLabel(memory));
        
        add(new JLabel("Graphics Card: "));
        add(new JLabel(graphicsCards));
        
        add(new JLabel("Disk: "));
        add(new JLabel(disk));
        
        add(new JLabel("Soundcard: "));
        add(new JLabel(soundCard));
        
        add(new JLabel("Power: "));
        add(new JLabel(power));
        
    }
    
    private String infoMemory(GlobalMemory memory) {
        StringBuilder info = new StringBuilder();
        List<String> memoryList = new ArrayList<>();
        List<PhysicalMemory> physicalMemories = memory.getPhysicalMemory();
        long total = 0;
        
        StringBuilder detailMemory;
        for (PhysicalMemory physicalMemory : physicalMemories) {
            detailMemory = new StringBuilder();
            total += physicalMemory.getCapacity();
            detailMemory.append(" ").append(physicalMemory.getMemoryType());
            detailMemory.append(" ").append((long) physicalMemory.getClockSpeed() / Math.pow(10, 6)).append(" Mhz");
            detailMemory.append(" ").append(DataSizeUtil.format(physicalMemory.getCapacity()));
            
            memoryList.add(detailMemory.toString());
        }    
        
        info.append(DataSizeUtil.format(total));
        info.append(" (").append(StringUtils.join(memoryList, " + ")).append(")");
        return info.toString();
    }
    
    private String infoGraphicsCard(List<GraphicsCard> graphicsCards) {
        StringBuilder info = new StringBuilder();
        List<String> graphicsCardList = new ArrayList<>();
        StringBuilder detailGraphics;
        
        for (GraphicsCard graphicsCard : graphicsCards) {
            detailGraphics = new StringBuilder();
            detailGraphics.append(graphicsCard.getName());
            detailGraphics.append(" ").append(DataSizeUtil.format(graphicsCard.getVRam()));
            
            graphicsCardList.add(detailGraphics.toString());
        }
        
        info.append(StringUtils.join(graphicsCardList, " + "));
        return info.toString();
    }
    
    private String infoDisk(List<HWDiskStore> disks) {
        StringBuilder info = new StringBuilder();
        StringBuilder detailDisk;
        List<String> diskList = new ArrayList<>();
        
        for (HWDiskStore disk : disks) {
            detailDisk = new StringBuilder();
            detailDisk.append(disk.getModel());
            
            diskList.add(detailDisk.toString());
        }
        info.append(StringUtils.join(diskList, " + "));
        return info.toString();
    }
    
    private String infoSoundCard(List<SoundCard> soundCards) {
        StringBuilder info = new StringBuilder();
        StringBuilder detailSoundCard;
        List<String> soundCardList = new ArrayList<>();
        
        for (SoundCard soundCard : soundCards) {
            detailSoundCard = new StringBuilder();
            detailSoundCard.append(soundCard.getName());
            
            soundCardList.add(detailSoundCard.toString());
        }
        
        info.append(StringUtils.join(soundCardList,  " + "));
        return info.toString();
    }
    
    private String infoPower(List<PowerSource> powers) {
        StringBuilder info = new StringBuilder();
        StringBuilder detailPower;
        List<String> powerList = new ArrayList<>();
        
        for (PowerSource power: powers) {
            detailPower = new StringBuilder();
            detailPower.append(power.getName());
            detailPower.append(" ").append(power.getManufacturer());
            detailPower.append(" ").append(power.getDeviceName());
            detailPower.append(" ").append(power.getMaxCapacity()).append("(" + power.getCapacityUnits() + ")");
            
            powerList.add(detailPower.toString());
        }
        
        info.append(StringUtils.join(powerList, " + "));
        return info.toString();
    }
}
