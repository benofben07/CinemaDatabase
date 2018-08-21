package controller;

import logic.date.*;
import logic.*;
import logic.entities.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.util.Pair;
import view.MainView;

public class CinemaController {
    private CinemaService service;
    private MainView view;

    public CinemaController() {
        createConnections();
    }
    
    private void createConnections() {
        service = new CinemaService();        
        view = new MainView(this);
        view.setVisible(true);
    }

    /* ======================= CORE ===================================== */

    /**
     * @return List containing Movies and sold ticket pairs.
     */
    public List<Pair<Movie, Integer>> listMovies() {
        return service.getMoviesWithSoldTickets();
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
            
        return service.addScreening(service.getByTitle(movieTitle),
                                  service.getCinemaHallByName(hallName),
                                  begin);
    }
    
    public Screening screeningFromRaw(String movieTitle, String chName,
                                      String begin) {
        final List<Screening> screenings = service.getScreenings();
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
     * 
     * @return boolean value if operation was successful.
     */
    public boolean removeScreening(String movieTitle, String chName, String begin) {
        return service.removeScreening(screeningFromRaw(movieTitle, chName, begin));
    }
    
    /**
     * Returns logic's listScreening() return as a List containing
     * Pair<Screening, sold ticket> objects.
     * 
     * @return List containins Screenings, and sold ticket pairs.
     */
    public List<Pair<Screening, Integer>> listScreening() {
        return service.getScreeningsWithSoldTickets();
    }

    /**
     * Returns booked Seat objects as a List represented by Row and Coloumn number
     * in Pair<row, col> objects.
     * @param s
     * @return List containing Seats <row,col> attributes.
     */
    public List getBookedSeats(Screening s) {
        return service.getBookedSeats(s);
    }
    
    /* ======================= FILTER ===================================== */

    public List filterMoviesByTitle(String title) {
        return service.filterMoviesByTitle(title);
    }
    
    /**
     * Filters Movies via their title. Matching titles to arg return.
     * @param title
     * Movie title
     * @return filtered List containing Screenings.
     */
    public List<Pair<Screening, Integer>> filterScreeningByMovie(String title) {
        return service.filterScreeningByMovie(title);
    }
    
    /**
     * Filters CinemaHalls via their respective names.
     * 
     * @return matching objects as a List
     */
    public List<Pair<Screening, Integer>> filterScreeningByCinemaHall(String name) {
        return service.filterScreeningByCinemaHall(name);
    }
    
    // TODO change in logic?
    public List<Pair<Screening, Integer>> filterScreeningByMovieAndCinemaHall(String movieTitle,
            String hallName) {
        final List<Pair<Screening, Integer>> movieFiltered = 
                filterScreeningByMovie(movieTitle);
        final List<Pair<Screening, Integer>> chFiltered = 
                filterScreeningByCinemaHall(hallName);
        
        final List<Pair<Screening, Integer>> result = movieFiltered.stream()
            .filter(pair -> equalsAny(pair, chFiltered))
            .collect(Collectors.toList());
             
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
        return service.getMovies();
    }
    
    public String getMoviePictureByName(String title) {
        return service.getByTitle(title).getPicture();
    }
    
    /**
     * Returns max play limit of given Movie object.
     * @param movieRaw
     * Expects a movie represented in raw format.
     * For example 'Title,English,David Yates'.
     */
    public int getMovieMaxPlay(String movieRaw) {
        return service.getMovieMaxPlay(movieRaw);
    }

    public Movie getMovieById(int id) {
        return service.getMovieById(id);
    }
    
    /* ------------------------ CINEMA_HALL ----------------------------- */

    /**
     * Returns all CinemaHall objects in a List.
     * @return List containing CinemaHall objects.
     */
    public List getCinemaHalls() {
        return service.getCinemaHalls();
    }
    
    /**
     * Returns CinemaHall object with given ID.
     * @param id
     * CinemaHall ID.
     */
    public CinemaHall getCHById(int id) {
        return service.getCinemaHallById(id);
    }
    
    /* ------------------------ SCREENING ------------------------------ */

    public List getScreenings(){
        return service.getScreenings();
    }
    
    public Screening getScreeningById(int id) {
        return service.getScreeningById(id);
    }
    
    /* ------------------------ SEATS ---------------------------------- */

    public List getSeats() {
        return service.getSeats();
    }
    
    /* ======================= SETTERS ===================================== */

    /**
     * for example Movie("Bleach", "Japo", true, "Acsikawa",
     *                   "Ez egy egesz jo dolog", 300, 5, 12, "soon")
     */
    public void addMovie(Movie m) {
        service.addMovies(m);
    }
    
    /**
     * Adds to geiven Screening a Seat representated by its row and coloum number.
     */
    public void addSeat(Screening s, int row, int col) {
        service.addSeat(s, row, col);
    }
    
    // ==================  VIEW  ====================================
    
    public List getMovieTitles() {
        final List<String> titles = new ArrayList<>();
        final List<Movie> movies = service.getMovies();
        movies.forEach(m -> titles.add(m.getTitle()));
        return titles;
    }
    
    public List getHallNames() {
        final List<String> names = new ArrayList<>();
        final List<CinemaHall> halls = service.getCinemaHalls();
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
