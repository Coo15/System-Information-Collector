package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NetworkName {
    private String networkNameList[];
    
    public NetworkName() {
        networkNameList = fetchNetworkCardDescriptions();
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
