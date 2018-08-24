package logic.dao;

import logic.entities.CinemaHall;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOCinemaHall extends DAOBase implements DAOGeneral {

    public CinemaHall getById(int id) {
        final List<CinemaHall> ch = getDataBySQL(
                "SELECT * FROM CINEMA_HALL WHERE CINEMA_HALL_ID=" + id);
        return ch.get(0);
    }
    
    public CinemaHall getByName(String name) {
        final List<CinemaHall> ch = getDataBySQL(
                "SELECT * FROM CINEMA_HALL WHERE NAME=\'" + name + "\'");
        return ch.get(0);
    }
    
    @Override
    public List<CinemaHall> getData() {
        return getDataBySQL("SELECT * FROM CINEMA_HALL");
        
    }
    
    private List<CinemaHall> getDataBySQL(String sqlStatement) {
        final List<CinemaHall> cinemaHalls = new ArrayList<>();
        try {
            statement = getStatement();
            statement.execute(sqlStatement);
            final ResultSet rs = statement.getResultSet();
            while(rs.next()){
                cinemaHalls.add( new CinemaHall(
                        rs.getInt("CINEMA_HALL_ID"),
                        rs.getString("NAME"),
                        rs.getInt("NUM_OF_ROW"),
                        rs.getInt("NUM_OF_COL")
                    )
                );
            }
            
            rs.close();
            statement.close();
            closeConnection();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return cinemaHalls;
    }

    @Override
    public void addData(Object data) {
             // not needed   
    }

    @Override
    public void removeData(Object data) {
        // not needed
    }
}
