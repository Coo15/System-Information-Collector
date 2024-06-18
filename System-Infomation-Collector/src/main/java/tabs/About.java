package tabs;

import javax.swing.*;
import java.awt.*;

public class About extends JPanel {
    
    public About() {
        setLayout(new BorderLayout());

        JTextArea aboutTextArea = new JTextArea();
        aboutTextArea.setEditable(false);
        aboutTextArea.setText(
            "   System Information Collector:\n" +
            "This program is designed to collect and display system information, including CPU performance, memory usage,\n" +
            "network activity, and more. It provides an overview of the system's status, helping users to monitor and \n" +
            "manage their system resources effectively.\n\n" +
            "   Team Members:\n" +
            " - Dao Minh Quang - https://github.com/Coo15 \n" +
            " - Vu Duc Thang - https://github.com/thoanggg \n" +
            "We are students at the School of Information and Communication Technology.\n" +
            "This is our project for the Project I course.\n\n" +
            "   APIs Used:\n" +
            " - OSHI (https://oshi.github.io/oshi/): A free Java API to obtain operating system and hardware information.\n" +
            " - JFreeChart (https://www.jfree.org/jfreechart/): A free chart library for the Java platform.\n" +
            " - Java Swing: A GUI widget toolkit for Java.\n"
        );
  
        add(aboutTextArea, BorderLayout.CENTER);
 
        setBorder(BorderFactory.createTitledBorder("About"));
        
    }
    

}
