package startup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class TaskSchedule extends JPanel {
    private JTable startupTable;
    private DefaultTableModel tableModel;

    public TaskSchedule() {
        setLayout(new BorderLayout());

        // Create the table model with column names
        String[] columnNames = {"Name", "Description", "Publisher", "Path","Timestamp"};
        tableModel = new DefaultTableModel(columnNames, 0);
        startupTable = new JTable(tableModel);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(startupTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchStartupApplications();
    }

    private void fetchStartupApplications() {
        List<String[]> startupEntries = getAutorunsEntries();
        for (String[] entry : startupEntries) {
            tableModel.addRow(entry);
        }
    }

    private List<String[]> getAutorunsEntries() {
        List<String[]> entries = new ArrayList<>();
        Path tempDir = null;
        try {
            // Create a temporary directory
            tempDir = Files.createTempDirectory("autoruns");
            // Extract autorunsc.exe to the temporary directory
            Path autorunscPath = tempDir.resolve("autorunsc.exe");
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("autorunsc.exe")) {
                if (is == null) {
                    throw new IOException("autorunsc.exe not found in resources");
                }
                Files.copy(is, autorunscPath, StandardCopyOption.REPLACE_EXISTING);
            }

            
            Process process = new ProcessBuilder(autorunscPath.toString(), "-accepteula", "-t").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> appDetails = new ArrayList<>();

            // Skip the first 8 lines directly
            for (int i = 0; i < 8 && reader.readLine() != null; i++);

            while ((line = reader.readLine().trim()) != null) {
                if (line.isEmpty()) {
                    continue;  
                }
                if (line.length() > 3) {
                    System.out.println(line);
                    if (!(line.charAt(0) == 'H') && !(line.charAt(2) == 'K')) {
                        appDetails.add(line);
                    }     
                }

                if (appDetails.size() == 7) {
                    String name = appDetails.get(0);
                    String description = appDetails.get(2);
                    String publisher = appDetails.get(3);
                    String path = appDetails.get(5);
                    String timestamp = appDetails.get(6);

                    entries.add(new String[]{name, description, publisher, path, timestamp});
                    appDetails.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return entries;
    }

}
