package view.tablemodels;

import java.util.List;
import javafx.util.Pair;
import javax.swing.table.AbstractTableModel;
import logic.entities.Movie;

public class MovieTableModel extends AbstractTableModel{
    
    private List<Pair<Movie, Integer>> movies;
    private final String[] colNames;
 
    
    public MovieTableModel(List movies) {
        this.colNames = new String[] {"Title", "Origin", "Director", "Description",
            "Dubbed", "Duration", "Age limit", "Sold ticket"};
        this.movies = movies;
    }
    
    private void printMovies() {
        movies.forEach((p) -> {
            System.out.println("key:" + p.getKey() + "value: " + p.getValue());
        });
    }
    
    /* ============== ABSTRACT OVERRIDEN METHODS ================== */
    
    @Override
    public String getColumnName(int index) {
        return colNames[index];
    }
    
    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Movie m = movies.get(rowIndex).getKey();
        switch(columnIndex) {
            case 0:
                return m.getTitle();
            case 1:
                return m.getOrigin();
            case 2:
                return m.getDirector();
            case 3:
                return m.getDescription();
            case 4:
                return m.isDubbed();
            case 5:
                return m.getDuration();
            case 6:
                return m.getAgeLimit();
            case 7:
                return movies.get(rowIndex).getValue();
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
