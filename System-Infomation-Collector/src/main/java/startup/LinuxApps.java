package startup;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OSService;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class LinuxApps extends JPanel {

    private JTable startupTable;
    private DefaultTableModel tableModel;

    public LinuxApps() {
        setLayout(new BorderLayout());

        // Create the table model with column names
        String[] columnNames = {"Name", "Path"};
        tableModel = new DefaultTableModel(columnNames, 0);
        startupTable = new JTable(tableModel);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(startupTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchStartupApplications();
    }

    private void fetchStartupApplications() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();
        fetchLinuxStartupApplications();
        
    }

    private void fetchLinuxStartupApplications() {
        try {
            String[] directories = {"/etc/init.d/", "~/.config/autostart/"};
            for (String dir : directories) {
                Process process = Runtime.getRuntime().exec("ls " + dir);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String name = line;
                    String path = dir + line;
                    tableModel.addRow(new Object[]{name, path});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
