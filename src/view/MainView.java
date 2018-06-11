package view;

import javax.swing.JFrame;
import controller.Controller;

public class MainView extends JFrame{

    private Controller controller;
    
    public MainView(Controller c) {
        controller = c;
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,400);
        setVisible(true);
        
    }

    public void displayError(String message) {
        
    }
    
}
