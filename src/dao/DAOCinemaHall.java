package dao;

import cinemadatabase.DBInit;
import entities.CinemaHall;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAOCinemaHall implements  DAOGeneral {

    public CinemaHall getById(int id) {
        List<CinemaHall> ch = getDataBySQL(
                "SELECT * FROM CINEMA_HALL WHERE CINEMA_HALL_ID=" + id);
        return ch.get(0);
    }
    
    //test
    public CinemaHall getByName(String name) {
        List<CinemaHall> ch = getDataBySQL(
                "SELECT * FROM CINEMA_HALL WHERE NAME=\'" + name + "\'");
        return ch.get(0);
    }
    
    @Override
    public List getData() {
        return getDataBySQL("SELECT * FROM CINEMA_HALL");
        
    }
    
    private List getDataBySQL(String sqlStatement) {
        List<CinemaHall> cinemaHalls = new ArrayList<>();
        try {
            Statement s = DBInit.getStatement();
            s.execute(sqlStatement);
            ResultSet rs = s.getResultSet();
            while(rs.next()){
                cinemaHalls.add( new CinemaHall(
                        rs.getInt("CINEMA_HALL_ID"),
                        rs.getString("NAME"),
                        rs.getInt("NUM_OF_ROW"),
                        rs.getInt("NUM_OF_COL")
                    )
                );
            }
            
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
