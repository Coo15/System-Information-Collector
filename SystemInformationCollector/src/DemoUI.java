import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DemoUI extends JFrame{
	
	JButton buttonApp, buttonPro, buttonSer, buttonPer, buttonNet;
	JPanel panel;
	JLabel label;
	JTable proTable, perTable, appTable, serTable;
	JScrollPane proSP, perSP, appSP, serSP;
	
	final String[] proColumnName = {"Name", "Status", "Cpu", "Memory"},
				perColumnName = {"Name", "Information"},
				appColumnName = {"Name", "Publisher", "Status"},
				serColumnName = {"Name", "PID", "Description", "Status", "Group"}
				;
	String[][] testData1 = {{"test", "test" , "test", "test"},
							{"test2", "test2" , "test2", "test2"}},
			testData2 = {{"CPU", "test"},
						{"Memory", "test2"}},
			testData3 = {{"test", "test", "test"},
						{"test2", "test2", "test2"}},
			testData4 = {{"test", "test", "test", "test", "test"},
						{"test2", "test2", "test2", "test2", "test2"}};
	
	DemoUI frame = this;
	
	DemoUI() {
		this.setTitle("System Information Collector");	
		
		panel = new JPanel();
		panel.setBounds(0,50,600,500);
		this.add(panel);
		
		label = new JLabel();
		label.setText("Main Page");
		panel.add(label);
		
		proTable = new JTable(testData1, proColumnName);
		proSP = new JScrollPane(proTable);
		
		perTable = new JTable(testData2, perColumnName);
		perSP = new JScrollPane(perTable);
		
		serTable = new JTable(testData4, serColumnName);
		serSP = new JScrollPane(serTable);
		
		appTable = new JTable(testData3, appColumnName);
		appSP = new JScrollPane(appTable);
		
		buttonPro = new JButton();
		buttonPro.setBounds(0, 0, 120, 50);
		buttonPro.setText("Processes");
		buttonPro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					panel.remove(proSP);
					panel.remove(perSP);
					panel.remove(serSP);
					panel.remove(appSP);
				} catch (Exception e1) {}
				finally {
					panel.add(proSP);
				}
				label.setText("Information about processes");
				
			}
		});
		
		buttonPer = new JButton();
		buttonPer.setBounds(120, 0, 120, 50);
		buttonPer.setText("Performance");
		buttonPer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					panel.remove(proSP);
					panel.remove(perSP);
					panel.remove(serSP);
					panel.remove(appSP);
				} catch (Exception e1) {}
				finally {
					panel.add(perSP);
				}
				
				label.setText("Information about performance");
				
			}
		});
		
		buttonApp = new JButton();
		buttonApp.setBounds(240, 0, 120, 50);
		buttonApp.setText("Startup apps");
		buttonApp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					panel.remove(proSP);
					panel.remove(perSP);
					panel.remove(serSP);
					panel.remove(appSP);
				} catch (Exception e1) {}
				finally {
					panel.add(appSP);
				}
				label.setText("Information about startup apps");
				
			}
		});
		
		buttonSer = new JButton();
		buttonSer.setBounds(360, 0, 120, 50);
		buttonSer.setText("Services");
		buttonSer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					panel.remove(proSP);
					panel.remove(perSP);
					panel.remove(serSP);
					panel.remove(appSP);
				} catch (Exception e1) {}
				finally {
					panel.add(serSP);
				}
				label.setText("Information about services");
				
			}
		});
		
		buttonNet = new JButton();
		buttonNet.setBounds(480, 0, 120, 50);
		buttonNet.setText("Networking");
		buttonNet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					panel.remove(proSP);
					panel.remove(perSP);
					panel.remove(serSP);
					panel.remove(appSP);
				} catch (Exception e1) {}
				finally {
				label.setText("Information about networking");
				}
			}
		});
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		this.setSize(600, 600);
		this.setVisible(true);
		getContentPane().add(buttonApp);
		getContentPane().add(buttonPro);
		getContentPane().add(buttonSer);
		getContentPane().add(buttonPer);
		getContentPane().add(buttonNet);
		
		
	}


}