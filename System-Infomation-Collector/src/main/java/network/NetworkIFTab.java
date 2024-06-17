
package network;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;


public class NetworkIFTab extends JPanel{
    private JLabel downloadSpeedLabel;
    private JLabel uploadSpeedLabel;
    private JLabel ipv4Label;
    private JLabel ipv6Label;
    private JLabel macLabel;
    private TimeSeries downloadSpeedSeries;
    private TimeSeries uploadSpeedSeries;
    
    private SystemInfo si = new SystemInfo();
    private HardwareAbstractionLayer hardware = si.getHardware();
    private NetworkIF networkIF;
    private List<NetworkIF> networkIFs = hardware.getNetworkIFs(true);
    
    private long previousBytesRecv = 0;
    private long previousBytesSent = 0;
    private long previousTimestamp = 0;
    private long currentBytesRecv;
    private long currentBytesSent;
    private long currentTimestamp;
    private long downloadSpeed;
    private long uploadSpeed;
    private long timeDiff;
    
    public NetworkIFTab(NetworkIF networkIF) {
        this.networkIF = networkIF;
        
        setLayout(new BorderLayout());

        downloadSpeedLabel = new JLabel();
        uploadSpeedLabel = new JLabel();
        ipv4Label = new JLabel();
        ipv6Label = new JLabel();
        macLabel = new JLabel();

        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        labelPanel.add(downloadSpeedLabel);
        labelPanel.add(uploadSpeedLabel);
        labelPanel.add(ipv4Label);
        labelPanel.add(ipv6Label);
        labelPanel.add(macLabel);

        add(labelPanel, BorderLayout.NORTH);

        downloadSpeedSeries = new TimeSeries("Download Speed");
        uploadSpeedSeries = new TimeSeries("Upload Speed");
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(downloadSpeedSeries);
        dataset.addSeries(uploadSpeedSeries);
        JFreeChart speedChart = ChartFactory.createTimeSeriesChart(
                "Network Speed",
                "Time",
                "Speed (kB/s)",
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
    
    private void updateNetworkData() {
        if (networkIF == null) {
            return;
        }

        networkIF.updateAttributes();

        currentBytesRecv = networkIF.getBytesRecv();
        currentBytesSent = networkIF.getBytesSent();
        currentTimestamp = networkIF.getTimeStamp();

        if (previousTimestamp == 0) {
            previousBytesRecv = currentBytesRecv;
            previousBytesSent = currentBytesSent;
            previousTimestamp = currentTimestamp;
            return;
        }

        timeDiff = currentTimestamp - previousTimestamp;
        if (timeDiff == 0) {
            downloadSpeed = 0;
            uploadSpeed = 0;
        } else {
            downloadSpeed = ((currentBytesRecv - previousBytesRecv) * 1000 / timeDiff) / 1024; 
            uploadSpeed = ((currentBytesSent - previousBytesSent) * 1000 / timeDiff) / 1024; 
        }
        
        previousBytesRecv = currentBytesRecv;
        previousBytesSent = currentBytesSent;
        previousTimestamp = currentTimestamp;

        SwingUtilities.invokeLater(() -> {
            downloadSpeedLabel.setText(String.format("Download Speed: %d kB/s", downloadSpeed));
            uploadSpeedLabel.setText(String.format("Upload Speed: %d kB/s", uploadSpeed));
            ipv4Label.setText(String.format("IPv4 Address: %s", networkIF.getIPv4addr()));
            ipv6Label.setText(String.format("IPv6 Address: %s", networkIF.getIPv6addr()));
            macLabel.setText(String.format("Physical Address: %s", networkIF.getMacaddr()));

            downloadSpeedSeries.addOrUpdate(new Second(), downloadSpeed);
            uploadSpeedSeries.addOrUpdate(new Second(), uploadSpeed);
        });
    }
}
