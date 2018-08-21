package view.tablemodels;

import java.util.List;
import javafx.util.Pair;
import javax.swing.table.AbstractTableModel;
import logic.entities.Screening;

public class ScreeningTableModel extends AbstractTableModel{
    private List<Pair<Screening, Integer>> screenings;
    private final String[] colNames;
    
    public ScreeningTableModel(List<Pair<Screening, Integer>> screenings) {
        this.colNames = new String[] {"Movie title", "Hall name", "Begin",
                "Available ticket"};
        this.screenings = screenings;
    }
    
    /* ============== ABSTRACT OVERRIDEN METHODS ================== */
    
    @Override
    public String getColumnName(int index) {
        return colNames[index];
    }
    
    @Override
    public int getRowCount() {
        return screenings.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Screening s = screenings.get(rowIndex).getKey();
        switch(columnIndex) {
            case 0:
                return s.getMovie().getTitle();
            case 1:
                return s.getCinemaHall().getName();
            case 2:
                return s.getInterval().getBegin().toString();
            case 3:
                return s.getAllSpaces() - screenings.get(rowIndex).getValue();
            default:
                return 0;
        }
    }
    
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    @Override
    public void setValueAt(Object o, int row, int column) {}

}
