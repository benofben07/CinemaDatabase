package view.booking;

import view.booking.SeatButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javafx.util.Pair;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import view.TutorialTestTable;

public class SeatBookingFrame extends JFrame{
    
    private final JFrame parentFrame;
    private JPanel seatPanel, bookPanel;
    private int row, col;
    private SeatButton[][] seats;
    private final List<Pair<Integer, Integer>> bookedSeats;
    
    public SeatBookingFrame(JFrame parent, int row, int col, List bookedSeats) {
        this.parentFrame = parent;
        this.bookedSeats = bookedSeats;
        this.row = row;
        this.col = col;
        
        setTitle("Booking");
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createButtonPanel(row, col);
        createBookPanel();
    }
    
    private void createBookPanel() {
        bookPanel = new JPanel();
        JButton verify = new JButton("Book seat!");
        verify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(seatPanel, "Sure?") == 0) {
                    for (int i = 0; i < row; ++i) {
                        for (int j = 0; j < col; ++j) {
                            if (seats[i][j].getColor().equals(Color.YELLOW)) {
                                ((TutorialTestTable)parentFrame).bookSeat(i+1,j+1);
                            }
                        }
                    }
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
        seatPanel.setPreferredSize(new Dimension(300, 400));
        seats = new SeatButton[row][col];
        
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                seats[i][j] = new SeatButton(
                        i + 1, j + 1, taken(i+1,j+1) ? Color.RED : Color.GREEN);
                
                seatPanel.add(seats[i][j]);
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
