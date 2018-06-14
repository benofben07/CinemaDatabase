package view;

import javax.swing.JFrame;
import controller.Controller;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainView extends JFrame{

    private Controller controller;
    private JTable table;
    
    public MainView(Controller c) {
        controller = c;
        
        createFrame();
        createTable();
        setVisible(true);
    }

    private void createFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500,400);
    }
    
    private void createTable() {
        JPanel panel = new JPanel();
        String[] columnNames = {"column1", "column2"};
        Object[][] data = {
            {"1","2"},
            {"3","4"}
        };
        
        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true); 
        panel.add(scrollPane);
        add(panel, BorderLayout.CENTER);
    }
    
    
    
    
    
    
    
    
    
    public void displayError(String message) {
        
    }
    
}
