package performance;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class CPU extends JPanel {

    private TimeSeries[] coreUsageSeries;
    private JLabel[] coreFrequencyLabels;

    private SystemInfo systemInfo;
    private HardwareAbstractionLayer hal;
    private CentralProcessor processor;
    private JPanel corePanel;
    private long[][] previousTicks;

    public CPU() {
        systemInfo = new SystemInfo();
        hal = systemInfo.getHardware();
        processor = hal.getProcessor();

        setLayout(new BorderLayout());

        int logicalProcessorCount = processor.getLogicalProcessorCount();
        coreUsageSeries = new TimeSeries[logicalProcessorCount];
        coreFrequencyLabels = new JLabel[logicalProcessorCount];
        corePanel = new JPanel(new GridLayout(0, 4, 10, 10));  // 4 charts per row

        for (int i = 0; i < logicalProcessorCount; i++) {
            JPanel coreInfoPanel = new JPanel(new BorderLayout());

            // Create TimeSeries and Chart for each core
            coreUsageSeries[i] = new TimeSeries("CPU " + (i + 1) + " Usage");
            TimeSeriesCollection dataset = new TimeSeriesCollection(coreUsageSeries[i]);
            JFreeChart coreChart = createChart(dataset);
            ChartPanel chartPanel = new ChartPanel(coreChart);
            chartPanel.setPreferredSize(new Dimension(200, 150));  // Set preferred size for each chart panel
            coreInfoPanel.add(chartPanel, BorderLayout.CENTER);

            // Add frequency label below the chart
            coreFrequencyLabels[i] = new JLabel();
            coreFrequencyLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            coreInfoPanel.add(coreFrequencyLabels[i], BorderLayout.SOUTH);

            corePanel.add(coreInfoPanel);
        }

        JScrollPane scrollPane = new JScrollPane(corePanel);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel cpuInfoPanel = createCpuInfoPanel();
        add(cpuInfoPanel, BorderLayout.NORTH);

        previousTicks = processor.getProcessorCpuLoadTicks();

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCpuData();
            }
        }, 0, 1000);
    }
    
    private JPanel createCpuInfoPanel() {
        JPanel cpuInfoPanel = new JPanel(new GridLayout(0,2));

        cpuInfoPanel.add(new JLabel("Name:" + processor.getProcessorIdentifier().getName()));

        cpuInfoPanel.add(new JLabel("Base Frequency:" + String.format("%.2f GHz", processor.getProcessorIdentifier().getVendorFreq() / 1e9)));

        cpuInfoPanel.add(new JLabel("Identifier:" + processor.getProcessorIdentifier().getIdentifier()));
        
        cpuInfoPanel.add(new JLabel("Physical Processor Count:" + String.valueOf(processor.getPhysicalProcessorCount())));

        cpuInfoPanel.add(new JLabel("ProcessorID:" + processor.getProcessorIdentifier().getProcessorID()));

        cpuInfoPanel.add(new JLabel("Logical Processor Count:" + String.valueOf(processor.getLogicalProcessorCount())));

        return cpuInfoPanel;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "",
            "",
            "",
            dataset,
            false,
            true,
            false
        );

        XYPlot plot = chart.getXYPlot();
        XYAreaRenderer renderer = new XYAreaRenderer();
        plot.setRenderer(renderer);

        // Set the range of the Y-axis to 0-100%
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);

        // Remove x-axis values
        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelsVisible(false);

        return chart;
    }

    private void updateCpuData() {
        long[] frequencies = processor.getCurrentFreq();
        long[][] newTicks = processor.getProcessorCpuLoadTicks();
        double[] perCoreLoad = processor.getProcessorCpuLoadBetweenTicks(previousTicks);
        previousTicks = newTicks;

        // Update labels and charts
        SwingUtilities.invokeLater(() -> {
            Millisecond now = new Millisecond();

            for (int i = 0; i < coreUsageSeries.length; i++) {
                double coreFreqGHz = frequencies[i] / 1000000000.0;
                int usagePercent = (int) (perCoreLoad[i] * 100);

                coreUsageSeries[i].addOrUpdate(now, usagePercent);
                coreFrequencyLabels[i].setText(String.format("Cpu %d: %.2f GHz", i,coreFreqGHz));

                // Remove data points older than 30 seconds
                while (coreUsageSeries[i].getItemCount() > 0 && coreUsageSeries[i].getDataItem(0).getPeriod().getFirstMillisecond() < now.getFirstMillisecond() - 30000) {
                    coreUsageSeries[i].delete(0, 0);
                }
            }
        });
    }
}
