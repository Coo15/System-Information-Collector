package startup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class App extends JPanel {

    private JTable startupTable;
    private DefaultTableModel tableModel;

    public App() {
        setLayout(new BorderLayout());

        // Create the table model with column names
        String[] columnNames = {"Name", "Path", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        startupTable = new JTable(tableModel);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(startupTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchStartupApplications();

        // Set column widths
        TableColumn statusColumn = startupTable.getColumnModel().getColumn(2);
        statusColumn.setPreferredWidth(80);

        TableColumn nameColumn = startupTable.getColumnModel().getColumn(0);
        nameColumn.setPreferredWidth(200);

        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        startupTable.setRowSorter(sorter);
        
        // Set default sorting on the "Status" column in descending order
        sorter.setSortKeys(List.of(new RowSorter.SortKey(2, SortOrder.DESCENDING)));
    }

    private void fetchStartupApplications() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            fetchWindowsStartupApplications();
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            fetchLinuxStartupApplications();
        }
    }

    private void fetchWindowsStartupApplications() {
        try {
            Process process = Runtime.getRuntime().exec("wmic startup get Caption, Command, Location");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.contains("Caption")) {
                    continue;
                }
                String[] parts = line.split("\\s{2,}");
                if (parts.length >= 3) {
                    String name = parts[0];
                    String path = parts[1];
                    String location = parts[2];
                    String status = location.equals("HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run") ? "Enabled" : "Disabled";
                    tableModel.addRow(new Object[]{name, path, status});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    tableModel.addRow(new Object[]{name, path, "Enabled"}); // Assume enabled for Linux startup scripts
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Startup Applications");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new App());
        frame.setVisible(true);
    }
}
