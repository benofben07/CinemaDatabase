package mvc;

import entities.Movie;
import entities.Screening;
import entities.Seat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    
    private View view;
    private Logic logic;
    
    /* ======================= CONSTRUCTOR ================================= */
    
    public Controller() {
        createConnections();
    }
    
    private void createConnections() {
        //view = new View(this);
        logic = new Logic(this);
    }

    /* ======================= CORE ===================================== */
    
    public Map listMovies() {
        return logic.listMovies();
    }
    
    // todo custom date + constaint kiegészítés
    public ScreeningStateContainer newScreening
        (int movieId, int cinemaHallId, String begin) {
           
            return logic.addScreening(logic.getMovieById(movieId),
                                      logic.getCinemaHallById(cinemaHallId),
                                      begin
                    );
    }
    
    public boolean removeScreening(Screening s) {
        return logic.removeScreening(s); /*? "Successfully removed." : 
                                          "Seats already taken.";*/
    }
    
    public Map listScreening() {
        return logic.listScreenings();
    }

    public Map getBookedSeats(Screening s) {
        return logic.getBookedSeats(s);
    }
    
    /* ======================= GETTERS ===================================== */
    
    /* ------------------------- MOVIE ----------------------------- */
    
    public List getMovies() {
        return logic.getMovies();
    }
    
    public int getMovieMaxPlay(String movieRaw) {
        return logic.getMovieMaxPlay(movieRaw);
    }
    
    public void getMovieById(int id) {
        System.out.println(logic.getMovieById(id).toString());
    }
    
    /* ------------------------ CINEMA_HALL ----------------------------- */
    
    public List getCinemaHalls() {
        return logic.getCinemaHalls();
    }
    
    public void getCHById(int id) {
        System.out.println(logic.getCinemaHallById(id).toString());
    }
    
    /* ------------------------ SCREENING ------------------------------ */
    
    public List getScreenings(){
        return logic.getScreenings();
    }
    
    /* ------------------------ SEATS ---------------------------------- */
    
    public List getSeats() {
        return logic.getSeats();
    }
    
    /*public void getSeats() {
        List<Seat> seats =  logic.getSeats();
        for (Seat s : seats) {
            System.out.println(s.toString());
        }
    }*/
    
    /* ======================= SETTERS ===================================== */
    
    public void addMovie(Movie m) {
        logic.addMovies(m);
        /* for example new Movie("Bleach", "Japo", true, 
                "Acsikawa", "Ez egy egesz jo dolog", 300, 5, 12, "soon")*/
    }
    
    public void addSeat(Screening s, int row, int col) {
        logic.addSeat(s, row, col);
    }
    
    /* ======================= FILTER ===================================== */
    
    public List filterByMovie(String title) {
        return logic.filterScreeningByMovie(title);
    }
    
    public List filterByCinemaHall(String name) {
        return logic.filterScreeningByCinemaHall(name);
    }
    
    /* ======================= TEST ===================================== */
    
    public void errorMessage(String message){
        view.displayError(message);
    }
    
    public void printMovieWithoutId(Movie m) {
        System.out.println("Movie without id:" + logic.getMovieWithoutId(m).toString());
    }
    
}
