/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package startup;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class Test extends JPanel{
    private JTable startupTable;
    private DefaultTableModel tableModel;

    public Test() {
        setLayout(new BorderLayout());

        // Create the table model with column names
        String[] columnNames = {"Name", "Description", "Publisher", "Path","Timestamp"};
        tableModel = new DefaultTableModel(columnNames, 0);
        startupTable = new JTable(tableModel);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(startupTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchStartupApplications();
    }
    
    private void fetchStartupApplications() {
        List<String[]> startupEntries = getAutorunsEntries();
        for (String[] entry : startupEntries) {
            tableModel.addRow(entry);
        }
    }
    
    private List<String[]> getAutorunsEntries(){
        List<String[]> entries = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("autorunsc.exe l");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> data = new ArrayList<>();
            
            for (int i = 0; i < 8 && reader.readLine() != null; i++);
            
            while ((line = reader.readLine().trim()) != null) {
                if (line.isEmpty()) {
                    continue;  
                }
                
                if (line.length() > 3) {
                    System.out.println(line);
                    System.out.println(line.contains("HKLM"));
                }
                

                if (data.size() == 7) {
                    String name = data.get(0);
                    String description = data.get(2);
                    String publisher = data.get(3);
                    String path = data.get(5);
                    String timestamp = data.get(6);

                    entries.add(new String[]{name, description, publisher, path, timestamp});
                    data.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return entries;
    }
    
    public static void main(String[] args) {
        // Create a frame to test the Autoruns panel
        JFrame frame = new JFrame("Autoruns Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new Test());
        frame.setLocationRelativeTo(null);  // Center the frame
        frame.setVisible(true);
    }
}
