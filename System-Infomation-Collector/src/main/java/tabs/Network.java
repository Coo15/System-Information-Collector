package tabs;


import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import network.*;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

public class Network extends JPanel{
    private SystemInfo systemInfo = new SystemInfo();
    private HardwareAbstractionLayer hardware = systemInfo.getHardware();;
    List<NetworkIF> networkIFs = hardware.getNetworkIFs(true);
    
    private String networkNameList[];
    
    public Network() {
        setLayout(new BorderLayout());
        JTabbedPane netTabs = new JTabbedPane();
        String osName = System.getProperty("os.name").toLowerCase();
        
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            for (NetworkIF networkIF: networkIFs) {
                netTabs.addTab(networkIF.getName(), new NetworkIFTab(networkIF));
            }
        } else {
            networkNameList = fetchNetworkCardDescriptions();
            for (String name : networkNameList) {
                for (NetworkIF networkIF: networkIFs) {
                    if (networkIF.getDisplayName().equalsIgnoreCase(name)) {
                        netTabs.addTab(name, new NetworkIFTab(networkIF));
                    }
                }
            }
        }
        
        add(netTabs, BorderLayout.CENTER);
    }
    
    private String[] fetchNetworkCardDescriptions() {
        ArrayList<String> descriptionsList = new ArrayList<>();
        
        try {
            Process process = Runtime.getRuntime().exec("ipconfig /all");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.startsWith("Description")) {
                    String description = getValue(line);
                    descriptionsList.add(description);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return descriptionsList.toArray(new String[0]);
    }

    private static String getValue(String line) {
        String[] parts = line.split(":", 2);
        return parts.length > 1 ? parts[1].trim() : "";
    }
}
