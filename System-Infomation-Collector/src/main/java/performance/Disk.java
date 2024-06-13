package performance;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Disk extends JPanel {

    private TimeSeries readSeries;
    private TimeSeries writeSeries;

    private SystemInfo systemInfo;
    private List<HWDiskStore> diskStores;

    private long prevReadBytes = 0;
    private long prevWriteBytes = 0;
    private long prevTimeStamp = 0;

    public Disk() {
        systemInfo = new SystemInfo();
        diskStores = systemInfo.getHardware().getDiskStores();

        setLayout(new BorderLayout());

        readSeries = new TimeSeries("Read Speed (KB/s)");
        writeSeries = new TimeSeries("Write Speed (KB/s)");

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(readSeries);
        dataset.addSeries(writeSeries);

        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateDiskData();
            }
        }, 0, 1000);
    }

    private JFreeChart createChart(TimeSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Disk Read/Write Speeds",
                "Time",
                "Speed (KB/s)",
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);

        return chart;
    }

    private void updateDiskData() {
        long curReadBytes = 0;
        long curWriteBytes = 0;
        long curTimeStamp = System.currentTimeMillis();

        for (HWDiskStore diskStore : diskStores) {
            diskStore.updateAttributes();
            curReadBytes += diskStore.getReadBytes();
            curWriteBytes += diskStore.getWriteBytes();
        }

        if (prevTimeStamp == 0) {
            prevReadBytes = curReadBytes;
            prevWriteBytes = curWriteBytes;
            prevTimeStamp = curTimeStamp;
            return;
        }

        long readDiff = curReadBytes - prevReadBytes;
        long writeDiff = curWriteBytes - prevWriteBytes;
        long timeDiff = curTimeStamp - prevTimeStamp;

        double readSpeed = readDiff / (timeDiff / 1000.0) / 1024.0; // Convert to KB/s
        double writeSpeed = writeDiff / (timeDiff / 1000.0) / 1024.0; // Convert to KB/s

        Second now = new Second();
        readSeries.addOrUpdate(now, readSpeed);
        writeSeries.addOrUpdate(now, writeSpeed);

        prevReadBytes = curReadBytes;
        prevWriteBytes = curWriteBytes;
        prevTimeStamp = curTimeStamp;

        while (readSeries.getItemCount() > 0 && readSeries.getDataItem(0).getPeriod().getFirstMillisecond() < now.getFirstMillisecond() - 60000) {
            readSeries.delete(0, 0);
        }
        while (writeSeries.getItemCount() > 0 && writeSeries.getDataItem(0).getPeriod().getFirstMillisecond() < now.getFirstMillisecond() - 60000) {
            writeSeries.delete(0, 0);
        }
    }
}
