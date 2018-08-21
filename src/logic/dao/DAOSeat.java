package logic.dao;

import logic.entities.Seat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logic.CinemaService;

public class DAOSeat extends DAOBase implements DAOGeneral{

    private final CinemaService logic;
    
    public DAOSeat (CinemaService l) {
        this.logic = l;
    }
    
    /**
     * Returns a list containins all objects
     * @return all Seat object contained in database as a List<Seat>
     */
    @Override
    public List getData() {
        return getDataBySQL("SELECT * FROM SEAT");
    }
    
    /**
     * Returns a list containing Seats belonging to given Screening object
     * @param screeningId
     * ID of a screening
     * @return List<Seat> belonging to given screening
     */
    public List getSeatsByScreening(int screeningId) {
        return getDataBySQL("SELECT * FROM SEAT WHERE FK_SCREENING_ID=" + screeningId);
    }

    private List getDataBySQL(String sqlStatement) {
        List<Seat> seats = new ArrayList<>();
        
        try {
            s = getStatement();
            
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
            
            rs.close();
            s.close();
            closeConnection();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return seats;
    }
    
    /**
     *
     * @param data
     * expects an Object[3] as parameter
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
            s = getStatement();
            String sqlStatement = new StringBuilder(
                "INSERT INTO SEAT (FK_SCREENING_ID, ROW_NUM, COL_NUM) VALUES (")
                .append(screeningId).append(", ")
                .append(row).append(", ").append(col).append(")")
                .toString();
            
            System.out.println("EXECUTING: " + sqlStatement);
            s.execute(sqlStatement);     
            
            s.close();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Not implemented, not necessary.
     * @param data
     */
    @Override
    public void removeData(Object data) {
        // not necessary
    }
    
}
