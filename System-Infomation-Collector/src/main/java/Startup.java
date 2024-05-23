/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ADMIN
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OSService;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Startup extends JPanel {

    private JTable startupTable;
    private DefaultTableModel tableModel;

    public Startup() {
        setLayout(new BorderLayout());

        // Create the table model with column names
        String[] columnNames = {"Name", "Path"};
        tableModel = new DefaultTableModel(columnNames, 0);
        startupTable = new JTable(tableModel);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(startupTable);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch and display startup applications
        fetchStartupApplications();
    }

    private void fetchStartupApplications() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            fetchWindowsStartupApplications();
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            fetchLinuxStartupApplications();
        }
    }

    private void fetchWindowsStartupApplications() {
        try {
            // Using WMIC to fetch startup items
            Process process = Runtime.getRuntime().exec("wmic startup get Caption, Command");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.contains("Caption")) {
                    continue;
                }
                String[] parts = line.split("\\s{2,}");
                if (parts.length >= 2) {
                    String name = parts[0];
                    String path = parts[1];
                    tableModel.addRow(new Object[]{name, path});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchLinuxStartupApplications() {
        try {
            // Check common startup directories
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

