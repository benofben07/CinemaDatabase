package logic.dao;

import logic.entities.CinemaHall;
import logic.entities.Screening;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logic.CinemaService;

public class DAOScreening extends DAOBase implements DAOGeneral{
    
    private final CinemaService logic;
    
    public DAOScreening(CinemaService logic) {
        this.logic = logic;
    }
    
    @Override
    public List getData() {
        return getDataBySQL("SELECT * FROM SCREENING");
    }
    
    public Screening getScreeningById(int id) {
        final List<Screening> screening = getDataBySQL(
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
        final List<Screening> screenings = new ArrayList<>();
        try {
            statement = getStatement();
            statement.execute(sqlStatement);
            final ResultSet rs = statement.getResultSet();
            while(rs.next()){
                screenings.add( new Screening(
                        rs.getInt("SCREENING_ID"),
                        logic.getMovieById(rs.getInt("FK_MOVIE_ID")),
                        logic.getCinemaHallById(rs.getInt("FK_CINEMA_HALL_ID")),
                        rs.getString("BEGIN")
                    )
                );
            }
            rs.close();
            statement.close();
            closeConnection();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return screenings;
    }
    
    public int getMovieCount(int movieId) {
        final List<Screening> screenings = getDataBySQL(
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
        final List<Object> data = (List) rawData;
        
        final Integer movieId = (Integer) data.get(0);
        final CinemaHall cinemaHall = (CinemaHall) data.get(1);
        final String begin = (String)  data.get(2);
        
        insertData(movieId, cinemaHall, begin);   
    }

    private void insertData(int m_id, CinemaHall ch, String begin) {
        try {
            statement = getStatement();
            System.out.println("EXECUTING: " + 
                       "INSERT INTO SCREENING (FK_MOVIE_ID, FK_CINEMA_HALL_ID, " + 
                       "BEGIN) VALUES (" + 
                        m_id + ", " + ch.getId() + ", \'" + begin + "\')");
            statement.execute("INSERT INTO SCREENING (FK_MOVIE_ID, FK_CINEMA_HALL_ID, " + 
                      " BEGIN) VALUES (" + 
                       m_id + ", " + ch.getId() + ", \'" + begin + "\')");
            
            statement.close();
            closeConnection();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void removeData(Object data) {
        try {
            statement = getStatement();
            final Screening screening = (Screening) data;
            statement.execute("DELETE FROM SCREENING WHERE SCREENING_ID=" + screening.getId());
            
            statement.close();
            closeConnection();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
