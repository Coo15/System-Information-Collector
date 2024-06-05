package tabs;


import cn.hutool.core.io.unit.DataSizeUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
        
        setLayout(new BorderLayout());
        
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        
        JLabel pcLabel = new JLabel(pcName);
        pcLabel.setFont(new Font(pcLabel.getFont().getName(), Font.PLAIN, 50));
        pcLabel.setForeground(Color.black);

        northPanel.add(Box.createRigidArea(new Dimension(10,10)));
        northPanel.add(pcLabel);
        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(Box.createRigidArea(new Dimension(10,10)));
        
        add(northPanel, BorderLayout.NORTH);
        
         JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        centerPanel.add(createLabelBlock("Operating System:", os), gbc);
        centerPanel.add(createLabelBlock("CPU:", cpu), gbc);
        centerPanel.add(createLabelBlock("Memory:", memory), gbc);
        centerPanel.add(createLabelBlock("Graphics Card:", graphicsCards), gbc);
        centerPanel.add(createLabelBlock("Disk:", disk), gbc);
        centerPanel.add(createLabelBlock("Sound Card:", soundCard), gbc);
        centerPanel.add(createLabelBlock("Power:", power), gbc);

        JScrollPane sp = new JScrollPane(centerPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(sp, BorderLayout.CENTER);
    }

    private JPanel createLabelBlock(String labelText, String valueText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 14));
        label.setPreferredSize(new Dimension(150, 25));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel value = new JLabel(valueText);
        value.setFont(new Font(value.getFont().getName(), Font.PLAIN, 14));
        value.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(value);

        return panel;
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
