package tabs;


import performance.CPU;
import performance.Memory;
import javax.swing.*;

public class Performance extends JPanel{
    public Performance(){
        JTabbedPane percTabs = new JTabbedPane();

        percTabs.addTab("CPU", new CPU());
        percTabs.addTab("Memory", new Memory());
        add(percTabs);
    
    }
    
}
