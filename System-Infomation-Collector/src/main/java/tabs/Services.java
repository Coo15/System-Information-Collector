package tabs;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSService;
import oshi.software.os.OperatingSystem;

public class Services extends JPanel {
    private SystemInfo si = new SystemInfo();
    private String[] columnNames = {"Name", "PID", "State"};
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane sp;
    private TableRowSorter<DefaultTableModel> sorter;
    HardwareAbstractionLayer hardware = si.getHardware();
    OperatingSystem operatingSystem = si.getOperatingSystem();
    ComputerSystem computerSystem = hardware.getComputerSystem();

    public Services() {
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        sp = new JScrollPane(table);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        setLayout(new BorderLayout());
        add(sp, BorderLayout.CENTER);

        Timer timer = new Timer(1000, e -> loadData());
        timer.start();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && table.getSelectedRow() != -1) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && table.getSelectedRow() != -1) {
                    showContextMenu(e);
                }
            }
        });
    }

    private void showContextMenu(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        table.setRowSelectionInterval(row, row);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem killMenuItem = new JMenuItem("Kill");
        killMenuItem.addActionListener(ae -> killService(row));
        contextMenu.add(killMenuItem);
        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void killService(int row) {
        int pid = (int) table.getValueAt(row, 1); // Get the PID from the table
        boolean success = executeKillCommand(pid);
        if (success) {
            JOptionPane.showMessageDialog(this, "Service killed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to kill the service.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean executeKillCommand(int pid) {
        String osName = System.getProperty("os.name").toLowerCase();
        String command;
        if (osName.contains("win")) {
            command = "taskkill /F /PID " + pid;
        } else {
            command = "kill -9 " + pid;
        }

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<OSService> services = operatingSystem.getServices();
        for (OSService service : services) {
            tableModel.addRow(new Object[]{
                service.getName(),
                service.getProcessID(),
                service.getState()
            });
        }
    }

}
