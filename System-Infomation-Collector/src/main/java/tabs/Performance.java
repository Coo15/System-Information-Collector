package tabs;

import performance.CPU;
import performance.Memory;
import javax.swing.*;
import java.awt.*;
import performance.Disk;

public class Performance extends JPanel {
    public Performance() {
        setLayout(new BorderLayout());
        JTabbedPane percTabs = new JTabbedPane();

        percTabs.addTab("CPU", new CPU());
        percTabs.addTab("Memory", new Memory());
        percTabs.addTab("Disk", new Disk());
        
        add(percTabs, BorderLayout.CENTER);
    }
}
