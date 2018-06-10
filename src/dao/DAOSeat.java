package dao;

import cinemadatabase.DBInit;
import entities.Screening;
import entities.Seat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mvc.Logic;

 // test whole
public class DAOSeat implements DAOGeneral{

    private final Logic logic;
    
    public DAOSeat (Logic l) {
        this.logic = l;
    }
    
    @Override
    public List getData() {
        return getDataBySQL("SELECT * FROM SEAT");
    }
    
    public List getSeatsByScreening(int screeningId) {
        return getDataBySQL("SELECT * FROM SEAT WHERE FK_SCREENING_ID=" + screeningId);
    }

    private List getDataBySQL(String sqlStatement) {
        List<Seat> seats = new ArrayList<>();
        
        try {
            Statement s = DBInit.getStatement();
            
            s.execute(sqlStatement);
            ResultSet rs = s.getResultSet();
            while(rs.next()){
                
                seats.add( new Seat(
                        rs.getInt("SEAT_ID"),
                        logic.getScreeningById(rs.getInt("FK_SCREENING_ID")),
                        rs.getInt("ROW_NUM"),
                        rs.getInt("COL_NUM")
                    )
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return seats;
    }
    
    /**
     *
     * @param data
     * expects a Object[3] as parameter
     * first: screening ID
     * second: row
     * third: col
     */
    @Override
    public void addData(Object data) {
        int[] rawData = (int[]) data;
        int screeningId = rawData[0];
        int row = rawData[1];
        int col = rawData[2];
        
        try {
            Statement s = DBInit.getStatement();
            String sqlStatement = new StringBuilder(
                "INSERT INTO SEAT (FK_SCREENING_ID, ROW_NUM, COL_NUM) VALUES (")
                .append(screeningId).append(", ")
                .append(row).append(", ").append(col).append(")")
                .toString();
            
            System.out.println("EXECUTING: " + sqlStatement);
            s.execute(sqlStatement);     
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        
    }

    @Override
    public void removeData(Object data) {
        // not necessary
    }
    
}
