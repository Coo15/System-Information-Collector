/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ADMIN
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.HWDiskStore;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Disk extends JPanel {
    private JLabel readSpeedLabel;
    private JLabel writeSpeedLabel;
    private TimeSeries readSpeedSeries;
    private TimeSeries writeSpeedSeries;

    private SystemInfo systemInfo;
    private HardwareAbstractionLayer hal;
    private HWDiskStore diskStore;

    public Disk() {
        systemInfo = new SystemInfo();
        hal = systemInfo.getHardware();
        diskStore = getActiveDiskStore();

        setLayout(new BorderLayout());

        readSpeedLabel = new JLabel();
        writeSpeedLabel = new JLabel();

        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        labelPanel.add(readSpeedLabel);
        labelPanel.add(writeSpeedLabel);

        add(labelPanel, BorderLayout.NORTH);

        readSpeedSeries = new TimeSeries("Read Speed");
        writeSpeedSeries = new TimeSeries("Write Speed");
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(readSpeedSeries);
        dataset.addSeries(writeSpeedSeries);
        JFreeChart speedChart = ChartFactory.createTimeSeriesChart(
            "Disk Speed Over Time",
            "Time",
            "Speed (MB/sec)",
            dataset,
            true,
            true,
            false
        );

        ChartPanel chartPanel = new ChartPanel(speedChart);
        add(chartPanel, BorderLayout.CENTER);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateDiskData();
            }
        }, 0, 1000);

        updateDiskData();
    }

    private HWDiskStore getActiveDiskStore() {
        java.util.List<HWDiskStore> diskStores = hal.getDiskStores();
        if (!diskStores.isEmpty()) {
            return diskStores.get(0);
        }
        return null;
    }

    private void updateDiskData() {
        if (diskStore == null) {
            return;
        }

        diskStore.updateAttributes();
        
        double readSpeed = diskStore.getReadBytes() / 1024 / 1024 / 1024;
        double writeSpeed = diskStore.getWriteBytes() / 1024 / 1024 / 1024;

        SwingUtilities.invokeLater(() -> {
            readSpeedLabel.setText(String.format("Read Speed: %.2f MB/sec", readSpeed));
            writeSpeedLabel.setText(String.format("Write Speed: %.2f MB/sec", writeSpeed));

            readSpeedSeries.addOrUpdate(new Second(), readSpeed);
            writeSpeedSeries.addOrUpdate(new Second(), writeSpeed);
        });
    }
}
