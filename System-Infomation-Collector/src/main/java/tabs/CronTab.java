package tabs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CronTab extends JPanel {

    private JTable startupTable;
    private DefaultTableModel tableModel;

    public CronTab() {
        setLayout(new BorderLayout());

        // Create the table model with column names
        String[] columnNames = {"Schedule", "Command", "Comments"};
        tableModel = new DefaultTableModel(columnNames, 0);
        startupTable = new JTable(tableModel);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(startupTable);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch and display crontab entries
        fetchCrontabEntries();
    }

    private void fetchCrontabEntries() {
        try {
            // Execute the crontab -l command to list crontab entries
            Process process = Runtime.getRuntime().exec("crontab -l");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\s+");
                if (parts.length < 6) {
                    continue;
                }

                String schedule = String.join(" ", parts[0], parts[1], parts[2], parts[3], parts[4]);

                StringBuilder commandBuilder = new StringBuilder();
                for (int i = 5; i < parts.length; i++) {
                    commandBuilder.append(parts[i]).append(" ");
                }
                String command = commandBuilder.toString().trim();

                String comments = "";
                int commentIndex = command.indexOf('#');
                if (commentIndex != -1) {
                    comments = command.substring(commentIndex + 1).trim();
                    command = command.substring(0, commentIndex).trim();
                }

                tableModel.addRow(new Object[]{schedule, command, comments});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
