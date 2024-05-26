/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author ADMIN
 */

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OSProcess;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OSProcess;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Processes extends JPanel {
    private SystemInfo si = new SystemInfo();
    private String[] columnNames = {"Name", "CPU (%)", "MEMORY (MB)", "PID", "PPID", "User", "Architecture", "State"};
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane sp;
    private HardwareAbstractionLayer hardware = si.getHardware();
    private OperatingSystem operatingSystem = si.getOperatingSystem();

    // Store the previous ticks for CPU calculation
    private Map<Integer, OSProcess> previousProcessMap = new HashMap<>();

    public Processes() {
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        sp = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(sp, BorderLayout.CENTER);

        // Add sorting functionality
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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

        // Add mouse listener for right-click context menu
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

        Timer timer = new Timer(1000, e -> loadData());
        timer.start();
    }

    private void showContextMenu(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        table.setRowSelectionInterval(row, row);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem killMenuItem = new JMenuItem("Kill");
        killMenuItem.addActionListener(ae -> killProcess(row));
        contextMenu.add(killMenuItem);
        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void killProcess(int row) {
        int pid = (int) table.getValueAt(row, 3); // Get PID from the table
        boolean success = executeKillCommand(pid);
        if (success) {
            JOptionPane.showMessageDialog(this, "Process killed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to kill the process.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean executeKillCommand(int pid) {
        String osName = System.getProperty("os.name").toLowerCase();
        String command;
        if (osName.contains("win")) {
            command = "taskkill /PID " + pid;
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
        List<OSProcess> processes = operatingSystem.getProcesses();
        Map<Integer, OSProcess> currentProcessMap = new HashMap<>();

        for (OSProcess process : processes) {
            OSProcess prevProcess = previousProcessMap.get(process.getProcessID());
            double cpuLoad = prevProcess == null ? 0 : process.getProcessCpuLoadBetweenTicks(prevProcess) * 100;
            long memoryInBytes = process.getResidentSetSize();
            long memoryInMB = memoryInBytes / (1024 * 1024);

            // Cap the CPU usage to 100% per core and exclude the "idle" process
            int logicalProcessorCount = hardware.getProcessor().getLogicalProcessorCount();
            cpuLoad = Math.min(cpuLoad, 100.0 * logicalProcessorCount);

            // Exclude "idle" process by checking if the process name contains "idle"
            if (!process.getName().toLowerCase().contains("idle")) {
                tableModel.addRow(new Object[]{
                        process.getName(),
                        String.format("%.2f", cpuLoad),
                        memoryInMB,
                        process.getProcessID(),
                        process.getParentProcessID(),
                        process.getUser(),
                        process.getBitness(),
                        process.getState()
                });
            }

            currentProcessMap.put(process.getProcessID(), process);
        }

        previousProcessMap = currentProcessMap;
    }

}


