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
import oshi.hardware.NetworkIF;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Wifi extends JPanel {
    private JLabel downloadSpeedLabel;
    private JLabel uploadSpeedLabel;
    private JLabel ipv4Label;
    private JLabel ipv6Label;
    private TimeSeries downloadSpeedSeries;
    private TimeSeries uploadSpeedSeries;

    private SystemInfo systemInfo;
    private HardwareAbstractionLayer hal;
    private NetworkIF networkIF;

    public Wifi() {
        systemInfo = new SystemInfo();
        hal = systemInfo.getHardware();
        networkIF = getActiveNetworkInterface();

        setLayout(new BorderLayout());

        downloadSpeedLabel = new JLabel();
        uploadSpeedLabel = new JLabel();
        ipv4Label = new JLabel();
        ipv6Label = new JLabel();

        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        labelPanel.add(downloadSpeedLabel);
        labelPanel.add(uploadSpeedLabel);
        labelPanel.add(ipv4Label);
        labelPanel.add(ipv6Label);

        add(labelPanel, BorderLayout.NORTH);

        downloadSpeedSeries = new TimeSeries("Download Speed");
        uploadSpeedSeries = new TimeSeries("Upload Speed");
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(downloadSpeedSeries);
        dataset.addSeries(uploadSpeedSeries);
        JFreeChart speedChart = ChartFactory.createTimeSeriesChart(
            "Network Speed",
            "Time",
            "Speed (MB/s)",
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
                updateNetworkData();
            }
        }, 0, 1000);

        updateNetworkData();
    }

    private NetworkIF getActiveNetworkInterface() {
        java.util.List<NetworkIF> networkIFs = hal.getNetworkIFs();
        for (NetworkIF netIF : networkIFs) {
            if ((netIF.getIPv4addr().length > 0) && (netIF.getName().substring(0,8).equals("wireless"))) {
                return netIF;
            }
        }
        return null;
    }

    private void updateNetworkData() {
        if (networkIF == null) {
            return;
        }

        networkIF.updateAttributes();
        
        long downloadSpeed = networkIF.getBytesRecv() / 1000000;
        long uploadSpeed = networkIF.getBytesSent() / 1000000;

        SwingUtilities.invokeLater(() -> {
            downloadSpeedLabel.setText(String.format("Download Speed: %d MB/s", downloadSpeed));
            uploadSpeedLabel.setText(String.format("Upload Speed: %d MB/s", uploadSpeed));
            ipv4Label.setText(String.format("IPv4 Address: %s", getIPv4Address(networkIF)));
            ipv6Label.setText(String.format("IPv6 Address: %s", getIPv6Address(networkIF)));

            downloadSpeedSeries.addOrUpdate(new Second(), downloadSpeed);
            uploadSpeedSeries.addOrUpdate(new Second(), uploadSpeed);
        });
    }

    private String getIPv4Address(NetworkIF networkIF) {
        String[] ipv4Addresses = networkIF.getIPv4addr();
        if (ipv4Addresses.length > 0) {
            return ipv4Addresses[0];
        } else {
            return "No IPv4 Address";
        }
    }
    
    private String getIPv6Address(NetworkIF networkIF) {
        String[] ipv6Addresses = networkIF.getIPv6addr();
        if (ipv6Addresses.length > 0) {
            return ipv6Addresses[0];
        } else {
            return "No IPv6 Address";
        }
    }
 
}
