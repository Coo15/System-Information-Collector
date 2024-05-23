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
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class CPU extends JPanel {

    private JLabel frequencyLabel;
    private JLabel usageLabel;
    private TimeSeries cpuUsageSeries;

    private SystemInfo systemInfo;
    private HardwareAbstractionLayer hal;
    private CentralProcessor processor;

    public CPU() {
        systemInfo = new SystemInfo();
        hal = systemInfo.getHardware();
        processor = hal.getProcessor();

        setLayout(new BorderLayout());

        frequencyLabel = new JLabel();
        usageLabel = new JLabel();

        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        labelPanel.add(frequencyLabel);
        labelPanel.add(usageLabel);

        add(labelPanel, BorderLayout.NORTH);

        cpuUsageSeries = new TimeSeries("CPU Usage");
        TimeSeriesCollection dataset = new TimeSeriesCollection(cpuUsageSeries);
        JFreeChart cpuChart = ChartFactory.createTimeSeriesChart(
            "CPU Usage Over Time",
            "Time",
            "Usage (%)",
            dataset,
            true,
            true,
            false
        );

        ChartPanel chartPanel = new ChartPanel(cpuChart);
        add(chartPanel, BorderLayout.CENTER);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCpuData();
            }
        }, 0, 1000);
    }

    private void updateCpuData() {
        long[] frequencies = processor.getCurrentFreq();
        long currentFreq = 0;
        if (frequencies.length > 0) {
            currentFreq = frequencies[0]; 
        }
        double currentFreqMHz = currentFreq / 1000000000.0; 

        long[] loadTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000); 
        double load = processor.getSystemCpuLoadBetweenTicks(loadTicks) * 100;

        // Update labels
        SwingUtilities.invokeLater(() -> {
            frequencyLabel.setText(String.format("Current Frequency: %.2f MHz", currentFreqMHz));
            usageLabel.setText(String.format("Usage: %.2f%%", load));
            cpuUsageSeries.addOrUpdate(new Second(), load);
        });
    }
}
