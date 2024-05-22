import javax.swing.*;
import java.awt.*;

public class SystemMonitor extends JFrame {

    public SystemMonitor() {
        // Set the title of the window
        setTitle("System Monitor");

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the window
        setSize(800, 600);

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create the "System Overview" panel with a grid layout
        JPanel systemOverviewPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        // Add labels to the "System Overview" panel
        systemOverviewPanel.add(new JLabel("Operating System:"));
        JLabel osLabel = new JLabel("Loading...");
        systemOverviewPanel.add(osLabel);

        systemOverviewPanel.add(new JLabel("CPU:"));
        JLabel cpuLabel = new JLabel("Loading...");
        systemOverviewPanel.add(cpuLabel);

        systemOverviewPanel.add(new JLabel("RAM:"));
        JLabel ramLabel = new JLabel("Loading...");
        systemOverviewPanel.add(ramLabel);

        systemOverviewPanel.add(new JLabel("Disk:"));
        JLabel diskLabel = new JLabel("Loading...");
        systemOverviewPanel.add(diskLabel);

        systemOverviewPanel.add(new JLabel("Network:"));
        JLabel networkLabel = new JLabel("Loading...");
        systemOverviewPanel.add(networkLabel);

        // Create the "Processes" panel
        JPanel processesPanel = new JPanel();
        processesPanel.setLayout(new BorderLayout());

        // Create a table to display processes
        String[] columnNames = {"PID", "Process Name", "CPU Usage", "Memory Usage"};
        Object[][] data = {
                {"", "", "", ""}
        };
        JTable processesTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(processesTable);
        processesPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the panels to the tabbed pane
        tabbedPane.addTab("System Overview", systemOverviewPanel);
        tabbedPane.addTab("Processes", processesPanel);

        // Add the tabbed pane to the frame
        add(tabbedPane);

        // Set the window to be visible
        setVisible(true);

        // Dummy data loading for demonstration, replace with actual system data
        loadSystemData(osLabel, cpuLabel, ramLabel, diskLabel, networkLabel);
    }

    private void loadSystemData(JLabel osLabel, JLabel cpuLabel, JLabel ramLabel, JLabel diskLabel, JLabel networkLabel) {
        // Replace with actual system data loading logic
        osLabel.setText(System.getProperty("os.name"));
        cpuLabel.setText("Intel Core i7"); // Replace with actual CPU info
        ramLabel.setText("16 GB"); // Replace with actual RAM info
        diskLabel.setText("512 GB SSD"); // Replace with actual Disk info
        networkLabel.setText("100 Mbps"); // Replace with actual Network info
    }

    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            new SystemMonitor();
        });
    }
}