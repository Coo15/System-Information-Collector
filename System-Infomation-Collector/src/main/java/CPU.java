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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.xy.XYDataset;

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
        JFreeChart cpuChart = createChart(dataset);

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

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "CPU Usage Over Time",
            "Time",
            "Usage (%)",
            dataset,
            true,
            true,
            false
        );

        XYPlot plot = chart.getXYPlot();
        XYAreaRenderer renderer = new XYAreaRenderer();
        plot.setRenderer(renderer);

        // Set the range of the Y-axis to 0-100%
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);

        return chart;
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
            frequencyLabel.setText(String.format("Current Frequency: %.2f GHz", currentFreqMHz));
            usageLabel.setText(String.format("Usage: %.2f%%", load));
            Millisecond now = new Millisecond();
            cpuUsageSeries.addOrUpdate(now, load);

            // Remove data points older than 30 seconds
            while (cpuUsageSeries.getItemCount() > 0 && cpuUsageSeries.getDataItem(0).getPeriod().getFirstMillisecond() < now.getFirstMillisecond() - 30000) {
                cpuUsageSeries.delete(0, 0);
            }
        });
    }
}