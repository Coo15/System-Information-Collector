/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSService;
import oshi.software.os.OperatingSystem;

/**
 *
 * @author ADMIN
 */
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
