package controller;

import view.MainView;
import logic.ScreeningStateContainer;
import logic.MainLogic;
import logic.entities.Movie;
import logic.entities.Screening;
import java.util.List;
import java.util.Map;

public class Controller {
    
    private MainView view;
    private MainLogic logic;
    
    /* ======================= CONSTRUCTOR ================================= */

    /**
     * Creates controller.
     * Also creates a MainLogic and a MainView objects and connects them via itself.
     */
    public Controller() {
        createConnections();
    }
    
    private void createConnections() {
        view = new MainView(this);
        logic = new MainLogic(this);
    }

    /* ======================= CORE ===================================== */

    /**
     * Calls logic's listMovies() method and returns its return.
     * @return Map containing Movies
     */
    public Map listMovies() {
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
        (int movieId, int cinemaHallId, String begin) {
           
            return logic.addScreening(logic.getMovieById(movieId),
                                      logic.getCinemaHallById(cinemaHallId),
                                      begin
                    );
    }
    
    /**
     * Removes given screening and returns if it was successful.
     * Unsuccessful action if there were already taken seats to given Screening.
     * @param s
     * Screening object
     * @return boolean value if operation was successful.
     */
    public boolean removeScreening(Screening s) {
        return logic.removeScreening(s); /*? "Successfully removed." : 
                                          "Seats already taken.";*/
    }
    
    /**
     * Returns logic's listScreening() return as a Map
     * @return Map containins Screenings, and sold tickets.
     */
    public Map listScreening() {
        return logic.listScreenings();
    }

    /**
     * Returns booked Seat objects as a Map represented by Row and Coloumn number.
     * @param s
     * @return Map containing Seats <row,col> attributes.
     */
    public Map getBookedSeats(Screening s) {
        return logic.getBookedSeats(s);
    }
    
    /* ======================= FILTER ===================================== */

    /**
     * Filters Movies via their title. Matching titles to arg return.
     * @param title
     * Movie title
     * @return filtered List containing Movies.
     */
    public List filterByMovie(String title) {
        return logic.filterScreeningByMovie(title);
    }
    
    /**
     * Filters CinemaHalls via their respective names.
     * Returns matchgin objects as a List
     * @param name
     * @return List containig filtered CinemaHalls
     */
    public List filterByCinemaHall(String name) {
        return logic.filterScreeningByCinemaHall(name);
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
    
    /* ======================= TEST ===================================== */
    
    public void errorMessage(String message){
        view.displayError(message);
    }
    
    public void printMovieWithoutId(Movie m) {
        System.out.println("Movie without id:" + logic.getMovieWithoutId(m).toString());
    }
    
}
