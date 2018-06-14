package controller;

import logic.date.CustomDate;
import logic.date.InvalidCustomDateException;
import java.util.ArrayList;
import logic.ScreeningStateContainer;
import logic.MainLogic;
import logic.entities.Movie;
import logic.entities.Screening;
import java.util.List;
import javafx.util.Pair;
import logic.entities.CinemaHall;
import view.MainView;

public class Controller {
    
    private MainLogic logic;
    private MainView view;
    
    /* ======================= CONSTRUCTOR ================================= */

    /**
     * Creates controller.
     * Also creates a MainLogic and a MainView objects and connects them via itself.
     */
    public Controller() {
        createConnections();
    }
    
    private void createConnections() {
        logic = new MainLogic(this);
        
        view = new MainView(this);
        view.setVisible(true);
    }

    /* ======================= CORE ===================================== */

    /**
     * Calls logic's listMovies() method and returns its return.
     * @return List containing Movies and sold ticket pairs.
     */
    public List listMovies() {
        return logic.listMovies();
    }
    
    /**
     *
     * @param movieId
     * Movie ID
     * @param cinemaHallId
     * CinemaHall ID
     * @param begin
     * CustomDate object in raw string format.
     * For example '2010-12-21 16:25:00'
     * @return ScreeningStateContainer object to indicate whether operation
     * was successful or what mistake rose.
     */
    public ScreeningStateContainer newScreening
        (String movieTitle, String hallName, String begin) {
           
            
            
            return logic.addScreening(logic.getByTitle(movieTitle),
                                      logic.getByName(hallName),
                                      begin
                    );
    }
    
    public Screening screeningFromRaw(String movieTitle, String chName, String begin) {
        List<Screening> screenings = logic.getScreenings();
        try {
            for (Screening s : screenings) {
                if (s.getMovie().getTitle().equals(movieTitle) &&
                    s.getCinemaHall().getName().equals(chName) &&
                    s.getInterval().getBegin().equals(
                            CustomDate.stringToCustomDate(begin))) {

                    return s;
                }
            }
        } catch (InvalidCustomDateException e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }
        
    /**
     * Removes given screening and returns if it was successful.
     * Unsuccessful action if there were already taken seats to given Screening.
     * @param s
     * Screening object
     * @return boolean value if operation was successful.
     */
    public boolean removeScreening(String movieTitle, String chName, String begin) {
        return logic.removeScreening(screeningFromRaw(movieTitle, chName, begin));
    }
    
    /**
     * Returns logic's listScreening() return as a List containing
     * Pair<Screening, sold ticket> objects.
     * @return List containins Screenings, and sold ticket pairs.
     */
    public List listScreening() {
        return logic.listScreenings();
    }

    /**
     * Returns booked Seat objects as a List represented by Row and Coloumn number
     * in Pair<row, col> objects.
     * @param s
     * @return List containing Seats <row,col> attributes.
     */
    public List getBookedSeats(Screening s) {
        System.out.println(logic.getBookedSeats(s));
        return logic.getBookedSeats(s);
    }
    
    /* ======================= FILTER ===================================== */

    public List filterMoviesByTitle(String title) {
        return logic.filterMoviesByTitle(title);
    }
    
    /**
     * Filters Movies via their title. Matching titles to arg return.
     * @param title
     * Movie title
     * @return filtered List containing Screenings.
     */
    public List filterScreeningByMovie(String title) {
        return logic.filterScreeningByMovie(title);
    }
    
    /**
     * Filters CinemaHalls via their respective names.
     * Returns matchgin objects as a List
     * @param name
     * @return List containig filtered Screenings
     */
    public List filterScreeningByCinemaHall(String name) {
        return logic.filterScreeningByCinemaHall(name);
    }
    
    public List filterScreeningByMovieAndCinemaHall(String movieTitle,
            String hallName) {
        List<Pair<Screening, Integer>> movieFiltered = 
                logic.filterScreeningByMovie(movieTitle);
        List<Pair<Screening, Integer>> chFiltered = 
                logic.filterScreeningByCinemaHall(hallName);
        List<Pair<Screening, Integer>> result = new ArrayList<>();
        
        for (Pair<Screening, Integer> pair : movieFiltered) {
            if (equalsAny(pair, chFiltered)) result.add(pair);
        }
        
        return result;
    }
    
    /* -------- additional methods */
    
    private boolean equalsAny(Pair<Screening, Integer> movPair, List<Pair<Screening, Integer>> chFiltered) {
        for (Pair<Screening, Integer> chPair : chFiltered) {
            if ( movPair.getKey().equals(chPair.getKey()) &&
                 movPair.getValue().equals(chPair.getValue())) return true;
        }
        
        return false;
    }
    
    /* ======================= GETTERS ===================================== */
    
    /* ------------------------- MOVIE ----------------------------- */

    /**
     * Returns all Movie object in a List
     * @return List containig Movies
     */
    public List getMovies() {
        return logic.getMovies();
    }
    
    public String getMoviePictureByName(String title) {
        return logic.getByTitle(title).getPicture();
    }
    
    /**
     * Returns max play limit of given Movie object.
     * @param movieRaw
     * Expects a movie represented in raw format.
     * For example 'Title,English,David Yates'.
     * @return
     */
    public int getMovieMaxPlay(String movieRaw) {
        return logic.getMovieMaxPlay(movieRaw);
    }
    
    /**
     * Returns Movie from database with given ID.
     * @param id
     * Movie id
     * @return Movie object with given ID 
     */
    public Movie getMovieById(int id) {
        return logic.getMovieById(id);
    }
    
    /* ------------------------ CINEMA_HALL ----------------------------- */

    /**
     * Returns all CinemaHall objects in a List.
     * @return List containing CinemaHall objects.
     */
    public List getCinemaHalls() {
        return logic.getCinemaHalls();
    }
    
    /**
     * Returns CinemaHall object with given ID.
     * @param id
     * CinemaHall ID.
     */
    public void getCHById(int id) {
        System.out.println(logic.getCinemaHallById(id).toString());
    }
    
    /* ------------------------ SCREENING ------------------------------ */

    /**
     * Returns all Screening object in a List.
     * @return List containing Screening objects.
     */
    public List getScreenings(){
        return logic.getScreenings();
    }
    
    /**
     * Returns Screening object with given ID.
     * @param id
     * Screening ID
     * @return Screening object.
     */
    public Screening getScreeningById(int id) {
        return logic.getScreeningById(id);
    }
    
    /* ------------------------ SEATS ---------------------------------- */

    /**
     * Returns all Seats from database in a List.
     * @return List containing Seat objects.
     */
    public List getSeats() {
        return logic.getSeats();
    }
    
    /* ======================= SETTERS ===================================== */

    /**
     * Adds given Movie to database.
     * @param m
     * Movie object.
     */
    public void addMovie(Movie m) {
        logic.addMovies(m);
        /* for example new Movie("Bleach", "Japo", true, 
                "Acsikawa", "Ez egy egesz jo dolog", 300, 5, 12, "soon")*/
    }
    
    /**
     * Adds to geiven Screening a Seat representated by its row and coloum number.
     * @param s
     * Screening object.
     * @param row
     * Row number.
     * @param col
     * Coloumn number.
     */
    public void addSeat(Screening s, int row, int col) {
        logic.addSeat(s, row, col);
    }
    
    // ==================  view  ====================================
    
    public List getMovieTitles() {
        List<String> titles = new ArrayList<>();
        List<Movie> movies = logic.getMovies();
        
        for (Movie m : movies) {
            titles.add(m.getTitle());
        }
        return titles;
    }
    
    public List getHallNames() {
        List<String> names = new ArrayList<>();
        List<CinemaHall> halls = logic.getCinemaHalls();
        
        for (CinemaHall ch : halls) {
            names.add(ch.getName());
        }
        return names;
    }
    
    public boolean validateDate(String date) {
        try {
            CustomDate.stringToCustomDate(date);
            return true;
        } catch (InvalidCustomDateException e) {
            return false;
        }
    }

}
