package tabs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TaskSchedule extends JPanel {

    private String[] columnNames = {"Taskname", "Next run time", "Status", "Logon mode", "Hostname"};
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane sp;
    private TableRowSorter<TableModel> sorter;

    public TaskSchedule() {
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        sp = new JScrollPane(table);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        setLayout(new BorderLayout());
        add(sp, BorderLayout.CENTER);

        Timer timer = new Timer(1000, e -> loadData());
        timer.start();

        // Adjust column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(200); // Taskname
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Next run time
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Logon mode
        table.getColumnModel().getColumn(4).setPreferredWidth(50);  // Hostname

        // Implementing header click sorting like the Processes panel
        table.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                if (sorter.getSortKeys().size() > 0 &&
                        sorter.getSortKeys().get(0).getColumn() == column &&
                        sorter.getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING) {
                    sorter.setSortKeys(List.of(new RowSorter.SortKey(column, SortOrder.ASCENDING)));
                } else {
                    sorter.setSortKeys(List.of(new RowSorter.SortKey(column, SortOrder.DESCENDING)));
                }
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            Process process = Runtime.getRuntime().exec("schtasks /query /fo LIST");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> taskData = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    if (!taskData.isEmpty()) {
                        addRowToTable(taskData);
                        taskData.clear();
                    }
                } else {
                    taskData.add(line);
                }
            }

            if (!taskData.isEmpty()) {
                addRowToTable(taskData);
            }

            reader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRowToTable(List<String> taskData) {
        String hostname = getValue(taskData, "HostName:");
        String taskname = getValue(taskData, "TaskName:");
        String nextRunTime = getValue(taskData, "Next Run Time:");
        String status = getValue(taskData, "Status:");
        String logonMode = getValue(taskData, "Logon Mode:");

        tableModel.addRow(new Object[]{taskname, nextRunTime, status, logonMode, hostname});
    }

    private String getValue(List<String> taskData, String key) {
        for (String line : taskData) {
            if (line.startsWith(key)) {
                return line.substring(key.length()).trim();
            }
        }
        return "";
    }

    
}
