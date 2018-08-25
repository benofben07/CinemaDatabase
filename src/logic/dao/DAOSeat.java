package logic.dao;

import logic.entities.Seat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logic.CinemaService;

public class DAOSeat extends DAOBase implements DAOGeneral{

    private final CinemaService service;
    
    public DAOSeat (CinemaService service) {
        this.service = service;
    }
    
    @Override
    public List<Seat> getData() {
        return getDataBySQL("SELECT * FROM SEAT");
    }
    
    /**
     * @return seats belonging to given screening
     */
    public List<Seat> getSeatsByScreening(int screeningId) {
        return getDataBySQL("SELECT * FROM SEAT WHERE FK_SCREENING_ID=" + screeningId);
    }

    private List<Seat> getDataBySQL(String sqlStatement) {
        final List<Seat> seats = new ArrayList<>();
        try {
            statement = getStatement();
            statement.execute(sqlStatement);
            try (ResultSet rs = statement.getResultSet()) {
                while(rs.next()){
                    seats.add( new Seat(
                            rs.getInt("SEAT_ID"),
                            service.getScreeningById(rs.getInt("FK_SCREENING_ID")),
                            rs.getInt("ROW_NUM"),
                            rs.getInt("COL_NUM")
                    )
                    );
                }
            }
            
            statement.close();
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
    // TODO refactor
    @Override
    public void addData(Object data) {
        final int[] rawData = (int[]) data;
        final int screeningId = rawData[0];
        final int row = rawData[1];
        final int col = rawData[2];
        
        try {
            statement = getStatement();
            final String sqlStatement = new StringBuilder(
                "INSERT INTO SEAT (FK_SCREENING_ID, ROW_NUM, COL_NUM) VALUES (")
                .append(screeningId).append(", ")
                .append(row).append(", ").append(col).append(")")
                .toString();
            
            System.out.println("EXECUTING: " + sqlStatement);
            statement.execute(sqlStatement);     
            
            statement.close();
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
