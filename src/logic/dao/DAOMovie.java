package logic.dao;

import logic.entities.Movie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class DAOMovie extends DAOBase implements DAOGeneral {

    public DAOMovie() {}
    
    public Movie getById(int id) {
        List<Movie> mList = getDataBySQL("SELECT * FROM MOVIE WHERE MOVIE_ID=" + id);
        return mList.get(0);
    }
    
    public Movie getMovieWithoutId(Movie m) {
        List<Movie> mList = getDataBySQL( 
                    "SELECT * FROM MOVIE WHERE" + 
                    " TITLE=\'" + m.getTitle() + "\' AND" +
                    " ORIGIN=\'" + m.getOrigin() + "\' AND" +
                    " DUBBED=\'" + m.getDubbed() + "\' AND" +
                    " DIRECTOR=\'" + m.getDirector() + "\' AND" +                      
                    " DESCRIPTION=\'" + m.getDescription() + "\' AND" +
                    " DURATION=" + m.getDuration() + " AND" +
                    " MAX_PLAY=" + m.getMaxPlay() + " AND" +
                    " AGE_LIMIT=" + m.getAgeLimit() + " AND" +
                    " PICTURE=\'" + m.getPicture() + "\'"
                );
        return mList.get(0);
    }

    public List getByTitle(String title) {
        List<Movie> mList = getDataBySQL(
                "SELECT * FROM MOVIE WHERE TITLE=\'" + title + "\'");
        return mList;
    }
    
    @Override
    public List getData() {    
        return getDataBySQL("SELECT * FROM MOVIE");
    }
    
    // executes the given sql query and returns result as a list 
    private List getDataBySQL(String sqlStatement) {
        List<Movie> movies = new ArrayList<>();
        
        try {
            s = getStatement();
            //System.out.println("EXECUTING: " + sqlStatement);
            s.execute(sqlStatement);
            ResultSet rs = s.getResultSet();
            while(rs.next()){
                movies.add( movieFromRs(rs) );
            }
            
            rs.close();
            s.close();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return movies;
    }
    
    // creates a new Movie object from a given row in database
    private Movie movieFromRs(ResultSet rs) throws SQLException{
        boolean dubbed = rs.getString("DUBBED").equals("Y");
        return new Movie( 
                        rs.getInt("MOVIE_ID"),
                        rs.getString("TITLE"),
                        rs.getString("ORIGIN"),
                        dubbed,
                        rs.getString("DIRECTOR"),
                        rs.getString("DESCRIPTION"),
                        rs.getInt("DURATION"),
                        rs.getInt("MAX_PLAY"),
                        rs.getInt("AGE_LIMIT"),
                        rs.getString("PICTURE")
                );
    }

    @Override
    public void addData(Object data) {
        
        try {
            s = getStatement();
            
            StringBuilder sqlStatement = new StringBuilder(
                "INSERT INTO MOVIE (TITLE, ORIGIN, DUBBED, DIRECTOR, " + 
                "DESCRIPTION, DURATION, MAX_PLAY, AGE_LIMIT, PICTURE) VALUES (");
            s.execute(createInsert(sqlStatement, (Movie) data));

            s.close();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private String createInsert(StringBuilder sb, Movie movie) {
        sb = appendSQLParam(sb, movie.getTitle());
        sb = appendSQLParam(sb, movie.getOrigin());
        sb = appendSQLParam(sb, movie.getDubbed());
        sb = appendSQLParam(sb, movie.getDirector());
        sb = appendSQLParam(sb, movie.getDescription());
        sb = appendSQLParam(sb, movie.getDuration());
        sb = appendSQLParam(sb, movie.getMaxPlay());
        sb = appendSQLParam(sb, movie.getAgeLimit());
        sb = appendSQLParam(sb, movie.getPicture());
        
        // cutting last comma and appending ')'
        return sb.deleteCharAt(sb.length() - 1).append(")").toString();
    }
    
    private StringBuilder appendSQLParam(StringBuilder sb, String param) {
        return sb.append("\'").append(param).append("\',");
    }

    private StringBuilder appendSQLParam(StringBuilder sb, int param) {
        return sb.append(param).append(",");
    }
    
    @Override
    public void removeData(Object data) {
        // not needed to complete
        /* 
            getStatement().execute("delete from movies where title=param");
        */
    }
    
    public int getMaxPlay(Movie m) {
        try {
            
            s = getStatement();
            System.out.println("EXECUTING: SELECT MAX_PLAY FROM MOVIE WHERE MOVIE_ID=" + m.getId());
            s.execute("SELECT MAX_PLAY FROM MOVIE WHERE MOVIE_ID=" + m.getId());
            ResultSet rs = s.getResultSet();
            if (rs.next()) return rs.getInt("MAX_PLAY");
            
            rs.close();
            s.close();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // some error occured
        return -1;
    }
}
