package mvc;

import dao.DAOCinemaHall;
import dao.DAOGeneral;
import dao.DAOMovie;
import dao.DAOScreening;
import dao.DAOSeat;
import date.CustomDate;
import entities.CinemaHall;
import entities.Movie;
import entities.Screening;
import entities.Seat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Logic {
   
    private final Controller controller;
    
    private DAOGeneral daoScreening;
    private DAOGeneral daoMovie;
    private DAOGeneral daoSeat;
    private DAOGeneral daoCinemaHall;
    
    public Logic( Controller c ) {
         controller = c;
         createConnection();
    }
   
    private void createConnection() {
        daoMovie      = new DAOMovie();
        daoCinemaHall = new DAOCinemaHall();
        // screening and seat needs to communicate with logic
        daoScreening  = new DAOScreening(this);
        daoSeat       = new DAOSeat(this);
        
    }
    
    /* ======================= GETTERS ===================================== */
    
    /* ------------------------- MOVIE ----------------------------- */
    
    public List getMovies() {
        return daoMovie.getData();
    }

    public Map listMovies() {
        List<Movie> movies = getMovies();
        // param <movie, sold ticket amount>
        Map<Movie, Integer> movieSoldTicket = new HashMap<>();
        for (Movie m : movies) {
            movieSoldTicket.put(m, getMovieSoldTicket(m.getId()));
        }
        return movieSoldTicket;
    }
    
    private int getMovieSoldTicket(int movieId) {
        DAOScreening dScreening = (DAOScreening) daoScreening;
        DAOSeat dSeat = (DAOSeat) daoSeat;
        List<Screening> screenings = dScreening.getDataByMovieId(movieId);
       
        int soldTickets = 0;
        
        for (Screening s : screenings) {
            soldTickets += (dSeat.getSeatsByScreening(s.getId())).size();
        }
        
        return soldTickets;
    }
    
    public int getMovieMaxPlay(String movieRaw) {
        List<Movie> movies= daoMovie.getData();
        // title, origin, director
        String[] data = movieRaw.split(",");
        for (Movie m : movies) {
            if ( m.getTitle().equals(data[0]) && m.getOrigin().equals(data[1]) 
                    && m.getDirector().equals(data[2]) ) {
                return m.getMaxPlay();
            }
        }
        // no sufficient movie in given param
        return -1;
    }
    
    public Movie getMovieById(int id) {
        DAOMovie dm = (DAOMovie) daoMovie;
        return dm.getById(id);
    }
    
    public Movie getMovieWithoutId(Movie m) {
        DAOMovie dm = (DAOMovie) daoMovie;
        return dm.getMovieWithoutId(m);
    }
    
    /* ------------------------ CINEMA_HALL ----------------------------- */
    
    public CinemaHall getCinemaHallById(int id) {
        DAOCinemaHall dch = (DAOCinemaHall) daoCinemaHall;
        return dch.getById(id);
    }
    
    public List getCinemaHalls() {
        return daoCinemaHall.getData();
    }
    
    /* ------------------------ SCREENING ------------------------------ */
    
    // test - seems working
    public Map listScreenings() {
        List<Screening> screenings = getScreenings();
        // param <screening, sold ticket amount>
        Map<Screening, Integer> screeningSoldTicket = new HashMap<>();
        for (Screening s : screenings) {
            screeningSoldTicket.put(s, getScreeningSoldTicket(s.getId()));
        }
        return screeningSoldTicket;
    }
    
    private int getScreeningSoldTicket(int screeningId) {
        DAOSeat dSeat = (DAOSeat) daoSeat;
        return dSeat.getSeatsByScreening(screeningId).size();
    }
    
    public List getScreenings() {
        return daoScreening.getData();
    }
    
    // test
    public Screening getScreeningById(int id) {
        DAOScreening ds = (DAOScreening) daoScreening;
        return ds.getScreeningById(id);
    }
    
    /* ------------------------ SEATS --------------------------------- */
    
    // test -- seems working
    public Map getBookedSeats(Screening s) {
        List<Seat> seats = getSeats();
        // param<row, col>
        Map<Integer, Integer> bookedSeats = new HashMap<>();
        for (Seat seat : seats) {
            bookedSeats.put(seat.getRowNumber(), seat.getColNumber());
        }
        
        return bookedSeats;
    }
    
    public List getSeats() {
        return daoSeat.getData();
    }
    
    /* ======================= SETTERS ===================================== */
    
    public void addMovies(Movie movie) {
        daoMovie.addData((Object) movie);
    }
    
    public ScreeningStateContainer addScreening(Movie m, CinemaHall ch, String begin) {
        // checking constraintst
        ScreeningStateContainer state = new ScreeningStateContainer(
                ScreeningAdditionEnum.SUCCES);
        
        if ( !checkMaxPlay(m) ) {
            state.setState(ScreeningAdditionEnum.SUCCES);
            return state;
        } else if ( !checkCinemaHalls(m, ch, begin) ) {
            state.setState(ScreeningAdditionEnum.SUCCES);
            return state;
        } else if ( ! checkAgeLimit(m.getAgeLimit(), begin) ) {
            state.setState(ScreeningAdditionEnum.SUCCES);
            return state;
        }
        
        // all correct
        List<Object> data = new ArrayList<>();
        data.add(m.getId());
        data.add(ch);
        data.add(begin);
        daoScreening.addData(data);
        return state;
    }
    
    /**
     *
     * @param s
     * @return false if seats are already taken to given screening
     * else it removes screening and returns true
     */
    // test
    public boolean removeScreening(Screening s) {
        DAOSeat dSeat = (DAOSeat) daoSeat;
        if ( !dSeat.getSeatsByScreening(s.getId()).isEmpty() ) {
            return false;
        }
        
        daoScreening.removeData(s);
        return true;
    }
    
    private boolean checkMaxPlay(Movie movie) {
        DAOScreening ds = (DAOScreening) daoScreening;
        return ( movie.getMaxPlay() > ds.getMovieCount(movie.getId()) );
    }
    
    private boolean checkCinemaHalls(Movie m, CinemaHall ch, String begin) {
        return true;
    }
    
    private boolean checkAgeLimit(int agaLimit, String begin) {
        return true;
    }
    
    public void addSeat(Screening s, int row, int col) {
        Object o = new int[] {s.getId(), row, col};
        daoSeat.addData(o);
    }
    
    /* ======================= FILTERS ===================================== */
    
    // test
    public List filterScreeningByMovie(String title) {
        DAOMovie dm = (DAOMovie) daoMovie;
        DAOScreening ds = (DAOScreening) daoScreening;
        Movie movie = dm.getByTitle(title);
        return ds.getDataByMovieId(movie.getId());
    }
    
    // test
    public List filterScreeningByCinemaHall(String cinemaHallName) {
        DAOScreening ds = (DAOScreening) daoScreening;
        DAOCinemaHall dch = (DAOCinemaHall) daoCinemaHall;
        CinemaHall ch = dch.getByName(cinemaHallName);
        return ds.getScreeningsByCinemaHallId(ch.getId());
    }
    
    /* ======================= TEST ======================================= */
    
    
    
   /* public void addScreening(Movie movie, CinemaHall hall, String begin){
        Screening screening = new Screening(movie, hall, hall.getAllSpace(), begin);
        List<Screening> screeningList = daoScreening.getData();
        boolean screeningNotCorrect = false;
        for (Screening s : screeningList) {
            if (screeningsAreAtTheSameTime(screening, s)) {
                screeningNotCorrect = true;
                controller.errorMessage("Screenings are at the same time.");
            }
        }
        daoScreening.addData(screening);
    }
    
    private boolean screeningsAreAtTheSameTime(Screening first, Screening second) {
        if (((first.getStartTime().compareTo(second.getStartTime())) < 0) 
           && (first.getEndTimePlus30Minutes().compareTo(second.getStartTime())) < 0 ) {
            return false;
        } else if ((first.getStartTime().compareTo(second.getStartTime())) > 0  
            && second.getEndTimePlus30Minutes().compareTo(first.getStartTime()) < 0 )  {
            return false;
        }
        return true;
    }
    
    public void removeScreening(Screening screening) {
        if(screening.getFreeSpaces() != screening.getCinemaHall().getAllSpace()){
            controller.errorMessage("There are reserved seats.");
            return;
        }
        daoScreening.removeData(screening);
    }
    
    private boolean checkIfBookingIsValid() {
        return true;
    }*/
    
}