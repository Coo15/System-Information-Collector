package tabs;


import network.Ethernet;
import network.Wifi;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Network extends JPanel{
    public Network() {
        JTabbedPane netTabs = new JTabbedPane();
        
        netTabs.addTab("Ethernet", new Ethernet());
        netTabs.addTab("Wi-Fi", new Wifi());
        add(netTabs);
    }
}
