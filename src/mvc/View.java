/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import javax.swing.JFrame;

/**
 *
 * @author Bence™
 */
public class View extends JFrame{

    private Controller controller;
    
    public View(Controller c) {
        controller = c;
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,400);
        setVisible(true);
        
    }

    public void displayError(String message) {
        
    }
    
}
