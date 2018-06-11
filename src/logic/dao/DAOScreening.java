package logic.dao;

import cinemadatabase.DBInit;
import logic.entities.CinemaHall;
import logic.entities.Screening;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import logic.MainLogic;

public class DAOScreening implements DAOGeneral{
    
    private final MainLogic logic;
    
    public DAOScreening(MainLogic logic) {
        this.logic = logic;
    }
    
    @Override
    public List getData() {
        return getDataBySQL("SELECT * FROM SCREENING");
    }
    
    public Screening getScreeningById(int id) {
        List<Screening> screening = getDataBySQL(
                "SELECT * FROM SCREENING WHERE SCREENING_ID=" + id);
        return screening.get(0);
    }
    
    public List getDataByMovieId(int id) {
        return getDataBySQL( "SELECT * FROM SCREENING WHERE FK_MOVIE_ID=" + id);
    }
    public List getScreeningsByCinemaHallId(int id) {
        return getDataBySQL( "SELECT * FROM SCREENING WHERE FK_CINEMA_HALL_ID=" + id);
    }
    
    private List getDataBySQL(String sqlStatement) {
        List<Screening> screenings = new ArrayList<>();
        try {
            Statement s = DBInit.getStatement();
            s.execute(sqlStatement);
            ResultSet rs = s.getResultSet();
            while(rs.next()){
                screenings.add( new Screening(
                        rs.getInt("SCREENING_ID"),
                        logic.getMovieById(rs.getInt("FK_MOVIE_ID")),
                        logic.getCinemaHallById(rs.getInt("FK_CINEMA_HALL_ID")),
                        rs.getString("BEGIN")
                    )
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return screenings;
    }
    
    public int getMovieCount(int movieId) {
        List<Screening> screenings = getDataBySQL(
                "SELECT * FROM SCREENING WHERE FK_MOVIE_ID=" + movieId);
        return screenings.size();
    }
    
    /**
     *
     * @param rawData
     * Accepts a List as a param containing 3 elements, first one a movie id,
     * second one a cinemahall object, 3rd one a beginning time (string)
     */
    @Override
    public void addData(Object rawData) {
        List<Object> data = (List) rawData;
        
        Integer movie_id      = (Integer) data.get(0);
        CinemaHall cinemaHall = (CinemaHall) data.get(1);
        String begin          = (String)  data.get(2);
        
        insertData(movie_id, cinemaHall, begin);
        
    }

    private void insertData(int m_id, CinemaHall ch, String begin) {
        try {
            Statement s = DBInit.getStatement();
            System.out.println("EXECUTING: " + 
                       "INSERT INTO SCREENING (FK_MOVIE_ID, FK_CINEMA_HALL_ID, " + 
                       "BEGIN) VALUES (" + 
                        m_id + ", " + ch.getId() + ", \'" + begin + "\')");
            s.execute("INSERT INTO SCREENING (FK_MOVIE_ID, FK_CINEMA_HALL_ID, " + 
                      " BEGIN) VALUES (" + 
                       m_id + ", " + ch.getId() + ", \'" + begin + "\')");
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     *
     * @param data Screening object
     * Removes given screening from database
     */
    @Override
    public void removeData(Object data) {
        try {
            Statement s = DBInit.getStatement();
            Screening screening = (Screening) data;
            s.execute("DELETE FROM SCREENING WHERE SCREENING_ID=" + screening.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
