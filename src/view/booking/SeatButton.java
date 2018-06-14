package view.booking;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;

public class SeatButton extends JButton {
    
    private final int row,col;
    private Color color;
    private Action clicked = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            SeatButton seat = (SeatButton) e.getSource();
            System.out.println(seat.getRow() + ", " + seat.getCol());
            if (seat.color.equals(Color.YELLOW)) {
                seat.color = Color.GREEN;
                seat.setBackground(Color.GREEN);
            } else if (seat.color.equals(Color.GREEN)) {
                seat.color = Color.YELLOW;
                seat.setBackground(Color.YELLOW);
            } else {
                System.out.println("Something's wrong.");
            }
        }
    };
    
    public SeatButton(int row, int col, Color c) {
        this.row = row;
        this.col = col;
        this.color = c;
        this.setBackground(color);
        this.setAction(clicked);
        if (color.equals(Color.RED)) this.setEnabled(false);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    
    
}
