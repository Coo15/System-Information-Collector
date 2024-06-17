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
    private JPanel infoPanel;
    private JLabel modelLabel = new JLabel();
    private JLabel nameLabel = new JLabel();
    private JLabel readLabel = new JLabel();
    private JLabel writeLabel = new JLabel();
    
    private HWDiskStore diskStore;

    private long prevReadBytes = 0;
    private long prevWriteBytes = 0;
    private long prevTimeStamp = 0;
    private long curReadBytes;
    private long curWriteBytes;
    private long curTimeStamp;
    private double readSpeed;
    private double writeSpeed;
    private long timeDiff;

    public Disk(HWDiskStore diskStore) {
        this.diskStore = diskStore;

        setLayout(new BorderLayout());
        
        infoPanel = new JPanel(new GridLayout(0,1));
        infoPanel.add(nameLabel);
        infoPanel.add(modelLabel);
        infoPanel.add(readLabel);
        infoPanel.add(writeLabel);
        
        nameLabel.setText("Name: " + diskStore.getName());
        modelLabel.setText("Model: " + diskStore.getModel());
        
        add(infoPanel, BorderLayout.NORTH);

        readSeries = new TimeSeries("Read Speed (kB/s)");
        writeSeries = new TimeSeries("Write Speed (kB/s)");

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
                "Speed (kB/s)",
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
        

        diskStore.updateAttributes();

        curReadBytes = diskStore.getReadBytes();
        curWriteBytes = diskStore.getWriteBytes();
        curTimeStamp = diskStore.getTimeStamp();

        if (prevTimeStamp == 0) {
            prevReadBytes = curReadBytes;
            prevWriteBytes = curWriteBytes;
            prevTimeStamp = curTimeStamp;
            return;
        }

        timeDiff = curTimeStamp - prevTimeStamp;
        if (timeDiff == 0) {
            readSpeed = 0;
            writeSpeed = 0;
        } else {
            readSpeed = ((curReadBytes - prevReadBytes) * 1000 / timeDiff) / 1024; 
            writeSpeed = ((curWriteBytes - prevWriteBytes) * 1000 / timeDiff) / 1024; 
        }
        
        prevReadBytes = curReadBytes;
        prevWriteBytes = curWriteBytes;
        prevTimeStamp = curTimeStamp;

        SwingUtilities.invokeLater(() -> {
            readLabel.setText(String.format("Download Speed: %.2f kB/s", readSpeed));
            writeLabel.setText(String.format("Upload Speed: %.2f kB/s", writeSpeed));

            readSeries.addOrUpdate(new Second(), readSpeed);
            writeSeries.addOrUpdate(new Second(), writeSpeed);
        });
    }
}
