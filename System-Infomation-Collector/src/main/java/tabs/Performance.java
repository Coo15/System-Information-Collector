package tabs;

import performance.CPU;
import performance.Memory;
import javax.swing.*;
import java.awt.*;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import performance.Disk;

public class Performance extends JPanel {
    private SystemInfo systemInfo = new SystemInfo();
    private java.util.List<HWDiskStore> diskStores = systemInfo.getHardware().getDiskStores();
    
    public Performance() {
        setLayout(new BorderLayout());
        JTabbedPane percTabs = new JTabbedPane();

        percTabs.addTab("CPU", new CPU());
        percTabs.addTab("Memory", new Memory());
        for (HWDiskStore diskStore: diskStores) {
            percTabs.addTab(diskStore.getModel(), new Disk(diskStore));
        }
        
        add(percTabs, BorderLayout.CENTER);
    }
}
