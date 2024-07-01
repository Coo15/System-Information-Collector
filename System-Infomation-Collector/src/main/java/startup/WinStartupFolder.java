package startup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WinStartupFolder extends JPanel {

    private JTable startupTable;
    private DefaultTableModel tableModel;

    public WinStartupFolder() {
        setLayout(new BorderLayout());
        
        String[] columnNames = {"Name", "Path"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        startupTable = new JTable(tableModel);
        startupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        startupTable.setCellSelectionEnabled(true);

        JScrollPane scrollPane = new JScrollPane(startupTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchStartupFolderEntries();
    }

    private void fetchStartupFolderEntries() {
        List<String[]> startupEntries = getStartupFolderEntries();
        for (String[] entry : startupEntries) {
            tableModel.addRow(entry);
        }
    }

    private List<String[]> getStartupFolderEntries() {
        List<String[]> entries = new ArrayList<>();
        String startupFolderPath = System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";

        File startupFolder = new File(startupFolderPath);
        File[] files = startupFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    String path = file.getAbsolutePath();
                    entries.add(new String[]{name, path});
                }
            }
        }

        return entries;
    }
}
