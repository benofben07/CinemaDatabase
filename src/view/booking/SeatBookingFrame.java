package view.booking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import view.MainView;

public class SeatBookingFrame extends JFrame{
    
    private final JFrame parentFrame;
    private JPanel seatPanel, bookPanel;
    private final int row, col;
    private SeatButton[][] seats;
    private final List<Pair<Integer, Integer>> bookedSeats;
    
    public SeatBookingFrame(JFrame parent, int row, int col, List bookedSeats) {
        this.parentFrame = parent;
        this.bookedSeats = bookedSeats;
        this.row = row;
        this.col = col;
        
        setTitle("Booking");
        setSize(400, 510);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createButtonPanel(row, col);
        createBookPanel();
    }
    
    private void createBookPanel() {
        bookPanel = new JPanel();
        bookPanel.setPreferredSize(new Dimension(400, 50));
        JButton verify = new JButton("Book seat!");
        verify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(seatPanel, "Sure?") == 0) {
                    List<Pair<Integer, Integer>> bookedSeats = new ArrayList<>();
                    for (int i = 0; i < row; ++i) {
                        for (int j = 0; j < col; ++j) {
                            if (seats[i][j].getColor().equals(Color.YELLOW)) {
                                bookedSeats.add(new Pair(i+1,j+1));
                            }
                        }
                    }
                    ((MainView) parentFrame).bookSeat(bookedSeats);
                    JOptionPane.showMessageDialog(seatPanel, "Successfully booked!");
                    disposeWindow();
                }
        }});
        
        bookPanel.add(verify);
        add(bookPanel, BorderLayout.SOUTH);
    }
    
    private void disposeWindow() {
        this.dispose();
    }
    
    private void createButtonPanel(int row, int col) {
        seatPanel = new JPanel(new GridLayout(row, col));
        seatPanel.setPreferredSize(new Dimension(400, 400));
        seats = new SeatButton[row][col];
        
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                seats[i][j] = new SeatButton(
                        i + 1, j + 1, taken(i+1,j+1) ? Color.RED : Color.GREEN);
                
                //seats[i][j].setPreferredSize(new Dimension(10*row, 10*col));
                seatPanel.add(seats[i][j], BorderLayout.PAGE_START);
            }
        }
        
        add(seatPanel, BorderLayout.NORTH);
    }
    
    private boolean taken(int row, int col) {
        for (Pair<Integer, Integer> pair : bookedSeats) {
            if (pair.getKey() == row && pair.getValue() == col) return true;
        }
        
        return false;
    }
    
}
