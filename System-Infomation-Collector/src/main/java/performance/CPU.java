package performance;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
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

    private JLabel frequencyLabel;
    private JLabel usageLabel;
    private TimeSeries cpuUsageSeries;

    private SystemInfo systemInfo;
    private HardwareAbstractionLayer hal;
    private CentralProcessor processor;
    private JPanel corePanel;
    private JLabel[] coreUsageLabels;
    private JLabel[] coreFrequencyLabels;
    
    private long[][] previousTicks;

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

        int logicalProcessorCount = processor.getLogicalProcessorCount();
        coreUsageLabels = new JLabel[logicalProcessorCount];
        coreFrequencyLabels = new JLabel[logicalProcessorCount];
        corePanel = new JPanel();
        corePanel.setLayout(new BoxLayout(corePanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < logicalProcessorCount; i++) {
            JPanel coreInfoPanel = new JPanel(new GridLayout(0, 1));
            coreUsageLabels[i] = new JLabel();
            coreFrequencyLabels[i] = new JLabel();
            coreInfoPanel.add(new JLabel("CPU " + (i + 1)));
            coreInfoPanel.add(coreUsageLabels[i]);
            coreInfoPanel.add(coreFrequencyLabels[i]);
            corePanel.add(coreInfoPanel);
            corePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(corePanel);
        scrollPane.setPreferredSize(new Dimension(200, 0));
        add(scrollPane, BorderLayout.WEST);

        previousTicks = processor.getProcessorCpuLoadTicks();

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
        long[][] newTicks = processor.getProcessorCpuLoadTicks();
        double[] perCoreLoad = processor.getProcessorCpuLoadBetweenTicks(previousTicks);
        previousTicks = newTicks;

        long currentFreq = 0;
        if (frequencies.length > 0) {
            currentFreq = frequencies[0];
        }
        double currentFreqGHz = currentFreq / 1000000000.0;

        long[] loadTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        double load = processor.getSystemCpuLoadBetweenTicks(loadTicks) * 100;

        // Update labels
        SwingUtilities.invokeLater(() -> {
            frequencyLabel.setText(String.format("Current Frequency: %.2f GHz", currentFreqGHz));
            usageLabel.setText(String.format("Usage: %.2f%%", load));
            Millisecond now = new Millisecond();
            cpuUsageSeries.addOrUpdate(now, load);

            while (cpuUsageSeries.getItemCount() > 0 && cpuUsageSeries.getDataItem(0).getPeriod().getFirstMillisecond() < now.getFirstMillisecond() - 30000) {
                cpuUsageSeries.delete(0, 0);
            }

            for (int i = 0; i < coreUsageLabels.length; i++) {
                int usagePercent = (int) (perCoreLoad[i] * 100);
                double coreFreqGHz = frequencies[i] / 1000000000.0;
                coreUsageLabels[i].setText(String.format("Usage: %d%%", usagePercent));
                coreFrequencyLabels[i].setText(String.format("Frequency: %.2f GHz", coreFreqGHz));
            }
        });
    }
}
