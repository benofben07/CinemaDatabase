package controller;

import logic.date.CustomDate;
import logic.date.InvalidCustomDateException;
import java.util.ArrayList;
import logic.ScreeningStateContainer;
import logic.CinemaService;
import logic.entities.Movie;
import logic.entities.Screening;
import java.util.List;
import javafx.util.Pair;
import logic.entities.CinemaHall;
import view.MainView;

public class CinemaController {
    
    private CinemaService logic;
    private MainView view;

    public CinemaController() {
        createConnections();
    }
    
    private void createConnections() {
        logic = new CinemaService();        
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
     * @param begin
     * CustomDate object in raw string format.
     * For example '2010-12-21 16:25:00'
     *
     * @return ScreeningStateContainer object to indicate whether operation
     * was successful or what mistake arose.
     */
    public ScreeningStateContainer newScreening
        (String movieTitle, String hallName, String begin) {
            
        return logic.addScreening(logic.getByTitle(movieTitle),
                                  logic.getByName(hallName),
                                  begin);
    }
    
    public Screening screeningFromRaw(String movieTitle, String chName,
                                      String begin) {
        final List<Screening> screenings = logic.getScreenings();
        try {
            for (Screening s : screenings) {
                if (s.getMovie().getTitle().equals(movieTitle) &&
                    s.getCinemaHall().getName().equals(chName) &&
                    s.getInterval().getBegin()
                            .equals(CustomDate.stringToCustomDate(begin))) {
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
        final List<Pair<Screening, Integer>> movieFiltered = 
                logic.filterScreeningByMovie(movieTitle);
        final List<Pair<Screening, Integer>> chFiltered = 
                logic.filterScreeningByCinemaHall(hallName);
        final List<Pair<Screening, Integer>> result = new ArrayList<>();
        
        
        movieFiltered.stream().filter(pair -> equalsAny(pair, chFiltered))
                .forEach(result::add);
             
        return result;
    }
    
    private boolean equalsAny(Pair<Screening, Integer> movPair, 
                              List<Pair<Screening, Integer>> chFiltered) {
        return chFiltered.stream().anyMatch(chPair -> (
                movPair.getKey().equals(chPair.getKey()) &&
                movPair.getValue().equals(chPair.getValue())));
    }
        
    /* ------------------------- MOVIE ----------------------------- */

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
     */
    public int getMovieMaxPlay(String movieRaw) {
        return logic.getMovieMaxPlay(movieRaw);
    }

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
    public CinemaHall getCHById(int id) {
        return logic.getCinemaHallById(id);
    }
    
    /* ------------------------ SCREENING ------------------------------ */

    public List getScreenings(){
        return logic.getScreenings();
    }
    
    public Screening getScreeningById(int id) {
        return logic.getScreeningById(id);
    }
    
    /* ------------------------ SEATS ---------------------------------- */

    public List getSeats() {
        return logic.getSeats();
    }
    
    /* ======================= SETTERS ===================================== */

    /**
     * for example Movie("Bleach", "Japo", true, "Acsikawa",
     *                   "Ez egy egesz jo dolog", 300, 5, 12, "soon")
     */
    public void addMovie(Movie m) {
        logic.addMovies(m);
    }
    
    /**
     * Adds to geiven Screening a Seat representated by its row and coloum number.
     */
    public void addSeat(Screening s, int row, int col) {
        logic.addSeat(s, row, col);
    }
    
    // ==================  VIEW  ====================================
    
    public List getMovieTitles() {
        final List<String> titles = new ArrayList<>();
        final List<Movie> movies = logic.getMovies();
        movies.forEach(m -> titles.add(m.getTitle()));
        return titles;
    }
    
    public List getHallNames() {
        final List<String> names = new ArrayList<>();
        final List<CinemaHall> halls = logic.getCinemaHalls();
        halls.forEach(ch -> names.add(ch.getName()));
        return names;
    }
    
    public boolean validateDate(String date) {
        try {
            CustomDate.stringToCustomDate(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
