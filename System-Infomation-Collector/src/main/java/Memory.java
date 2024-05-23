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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Memory extends JPanel {

    private JLabel usageLabel;
    private JLabel maxMemoryLabel;
    private JLabel availableMemoryLabel;
    private TimeSeries memoryUsageSeries;

    private SystemInfo systemInfo;
    private GlobalMemory memory;

    public Memory() {
        systemInfo = new SystemInfo();
        memory = systemInfo.getHardware().getMemory();

        setLayout(new BorderLayout());

        usageLabel = new JLabel();
        maxMemoryLabel = new JLabel();
        availableMemoryLabel = new JLabel();

        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        labelPanel.add(usageLabel);
        labelPanel.add(maxMemoryLabel);
        labelPanel.add(availableMemoryLabel);

        add(labelPanel, BorderLayout.NORTH);

        memoryUsageSeries = new TimeSeries("Memory Usage");
        TimeSeriesCollection dataset = new TimeSeriesCollection(memoryUsageSeries);
        JFreeChart memoryChart = ChartFactory.createTimeSeriesChart(
            "Memory Usage Over Time",
            "Time",
            "Usage (%)",
            dataset,
            true,
            true,
            false
        );

        ChartPanel chartPanel = new ChartPanel(memoryChart);
        add(chartPanel, BorderLayout.CENTER);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateMemoryData();
            }
        }, 0, 1000);
    }

    private void updateMemoryData() {
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();
        long usedMemory = totalMemory - availableMemory;
        double usagePercentage = (double) usedMemory / totalMemory * 100;

        SwingUtilities.invokeLater(() -> {
            usageLabel.setText(String.format("Usage: %.2f GB", usedMemory / (1024.0 * 1024 * 1024) ));
            maxMemoryLabel.setText(String.format("Max Memory: %.2f GB", totalMemory / (1024.0 * 1024 * 1024)));
            availableMemoryLabel.setText(String.format("Available Memory: %.2f GB", availableMemory / (1024.0 * 1024 * 1024)));
            memoryUsageSeries.addOrUpdate(new Second(), usagePercentage);
        });
    }

}
