package performance;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.XYDataset;

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
        JFreeChart memoryChart = createChart(dataset);

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

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Memory Usage Over Time",
            "Time",
            "Usage (%)",
            dataset,
            true,
            true,
            false
        );

        XYPlot plot = chart.getXYPlot();
        XYAreaRenderer renderer = new XYAreaRenderer();
        renderer.setSeriesPaint(0, new Color(0, 0, 255, 128)); 
        plot.setRenderer(renderer);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);

        return chart;
    }

    private void updateMemoryData() {
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();
        long usedMemory = totalMemory - availableMemory;
        double usagePercentage = (double) usedMemory / totalMemory * 100;

        SwingUtilities.invokeLater(() -> {
            usageLabel.setText(String.format("Usage: %.2f GB", usedMemory / (1024.0 * 1024 * 1024)));
            availableMemoryLabel.setText(String.format("Available Memory: %.2f GB", availableMemory / (1024.0 * 1024 * 1024)));
            maxMemoryLabel.setText(String.format("Max Memory: %.2f GB", totalMemory / (1024.0 * 1024 * 1024)));
            memoryUsageSeries.addOrUpdate(new Second(), usagePercentage);

            while (memoryUsageSeries.getItemCount() > 0 && memoryUsageSeries.getDataItem(0).getPeriod().getFirstMillisecond() < new Second().getFirstMillisecond() - 60000) {
                memoryUsageSeries.delete(0, 0);
            }
        });
    }

}
