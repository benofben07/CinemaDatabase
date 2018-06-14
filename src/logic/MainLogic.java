package logic;

import logic.dao.DAOCinemaHall;
import logic.dao.DAOGeneral;
import logic.dao.DAOMovie;
import logic.dao.DAOScreening;
import logic.dao.DAOSeat;
import logic.date.CustomDate;
import logic.date.CustomInterval;
import logic.date.InvalidCustomDateException;
import logic.entities.CinemaHall;
import logic.entities.Movie;
import logic.entities.Screening;
import logic.entities.Seat;
import java.util.ArrayList;
import java.util.List;
import controller.Controller;
import javafx.util.Pair;

public class MainLogic {
   
    private final Controller controller;
    
    private DAOGeneral daoScreening;
    private DAOGeneral daoMovie;
    private DAOGeneral daoSeat;
    private DAOGeneral daoCinemaHall;
    
    /**
     * Logic object constructor. Creates connections to dao objects.
     * @param c
     * Controller object linked to this Logic object
     */
    public MainLogic( Controller c ) {
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

    /**
     * Reads from database and returns all Movie objects as a List
     * @return Movie objects as a List.
     */
    public List getMovies() {
        return daoMovie.getData();
    }

    /**
     * Reads from database movies and sold tickets for every movie.
     * Returns value as a List containing Pairs.
     * @return List with Pairs<Movie, sold ticket amount>
     */
    public List listMovies() { 
        return addSoldTicketToMovie(getMovies());
    }

    // counting sold tickets using daoSeat and daoScreening objects
    private int getMovieSoldTicket(int movieId) {
        // casting to use DAOScreening, DAOSeat specific methods
        DAOScreening dScreening = (DAOScreening) daoScreening;
        DAOSeat dSeat = (DAOSeat) daoSeat;
        
        List<Screening> screenings = dScreening.getDataByMovieId(movieId);
       
        int soldTickets = 0;
        
        for (Screening s : screenings) {
            soldTickets += (dSeat.getSeatsByScreening(s.getId())).size();
        }
        
        return soldTickets;
    }
    
    /**
     *
     * @param movieRaw
     * Expects a movie in raw format from View
     * For example 'Title,English,David Yates'
     * @return max play value of given movie
     * or -1 if no movies found with given string.
     */
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
    
    /**
     * Searches for given Movie via its id in database then returns it.
     * @param id
     * Movie id
     * @return Movie object
     */
    public Movie getMovieById(int id) {
        // casting to use DAOMovie specific methods
        DAOMovie dm = (DAOMovie) daoMovie;
        return dm.getById(id);
    }
    
    /**
     * Gets ID for a Movie object from database and returs completed object.
     * @param m
     * Movie without id (defaultly set to -1)
     * @return complete Movie object with id
     */
    public Movie getMovieWithoutId(Movie m) {
        // casting to use DAOMovie specific methods
        DAOMovie dm = (DAOMovie) daoMovie;
        return dm.getMovieWithoutId(m);
    }
    
    public Movie getByTitle(String title) {
        List<Movie> movie = ((DAOMovie) daoMovie).getByTitle(title);
        return movie.get(0);
    }
    
    /* ----- additional function */
    
    private List addSoldTicketToMovie(List<Movie> movies) {
        // param <movie, sold ticket amount>
        List<Pair<Movie, Integer>> movieSoldTicket = new ArrayList<>();
        for (Movie m : movies) {
            movieSoldTicket.add(new Pair(m, getMovieSoldTicket(m.getId())));
        }
        return movieSoldTicket;
    }
    
    /* ------------------------ CINEMA_HALL ----------------------------- */

    /**
     * Gets CinemaHall object from database via its ID.
     * @param id
     * CinemaHall id
     * @return CinemaHall object
     */
    public CinemaHall getCinemaHallById(int id) {
        // casting to use DAOScreening, DAOCinemaHall specific methods
        DAOCinemaHall dch = (DAOCinemaHall) daoCinemaHall;
        return dch.getById(id);
    }
    
    public CinemaHall getByName(String name) {
        return ((DAOCinemaHall) daoCinemaHall).getByName(name);
    }
    
    /**
     * Reads every CinemaHall object from database and returns them
     * as a List<CinemaHall>
     * @return List containing CinemaHall objects.
     */
    public List getCinemaHalls() {
        return daoCinemaHall.getData();
    }
    
    /* ------------------------ SCREENING ------------------------------ */

    /**
     * Gets every Screening from database first, then get sold ticket to
     * each. Puts it into a List containing Pairs according to its result.
     * @return List with Pairs <Screening, sold ticket amount>
     */
    public List listScreenings() {
        return addSoldTicketToScreening(getScreenings());
    }
    
    // uses a DAOSeat object to get sold tickets to given Screening
    private int getScreeningSoldTicket(int screeningId) {
        // casting to use DAOSeat specific methods
        DAOSeat dSeat = (DAOSeat) daoSeat;
        return dSeat.getSeatsByScreening(screeningId).size();
    }
    
    /**
     * Reads from database and returns all Screening objects in a List.
     * @return List containing Screening objects.
     */
    public List getScreenings() {
        return daoScreening.getData();
    }
    
    /**
     * Reads from database and gets Screening with given ID.
     * @param id
     * Screening id
     * @return Screening object
     */
    public Screening getScreeningById(int id) {
        // casting to use DAOScreening specific methods
        DAOScreening ds = (DAOScreening) daoScreening;
        return ds.getScreeningById(id);
    }
    
    /* ----- additional function */
    
    private List addSoldTicketToScreening(List<Screening> screenings) {
        // param <screening, sold ticket amount>
        List<Pair<Screening, Integer>> screeningSoldTicket = new ArrayList<>();
        for (Screening s : screenings) {
            screeningSoldTicket.add(
                    new Pair(s, getScreeningSoldTicket(s.getId())));
        }
        return screeningSoldTicket;
    }
    
    /* ------------------------ SEATS --------------------------------- */

    /**
     * Reads database for given Screening and collects taken seats
     * into a List containing Pairs <row, col> then returns it.
     * @param s
     * Screening object
     * @return List with Pairs, param <row, col>
     */
    public List getBookedSeats(Screening s) {
        List<Seat> seats = ((DAOSeat) daoSeat).getSeatsByScreening(s.getId());
        // param<row, col>
        List<Pair<Integer, Integer>> bookedSeats = new ArrayList<>();
        for (Seat seat : seats) {
            
            bookedSeats.add(new Pair(seat.getRowNumber(), seat.getColNumber()));
        }
        
        return bookedSeats;
    }
    
    /**
     * Reads database and returns Seat objects.
     * @return List containins Seat objects.
     */
    public List getSeats() {
        return daoSeat.getData();
    }
    
    /* ======================= SETTERS ===================================== */

    /**
     * Adds a given Movie object to database.
     * @param movie
     * Movie to add
     */
    public void addMovies(Movie movie) {
        daoMovie.addData((Object) movie);
    }
    
    /**
     * Creates a new Screening. Adds screening to database as well.
     * @param m
     * Movie to add
     * @param ch
     * Cinema hall where Screening takes place
     * @param begin
     * CustomDate in raw format (e.g. '2015-12-21 12:30:00')
     * @return ScreeningStateContainer object containing any problem
     * or success.
     */
    public ScreeningStateContainer addScreening(Movie m, CinemaHall ch, String begin) {
        // checking constraintst
        ScreeningStateContainer state = new ScreeningStateContainer(
                ScreeningAdditionEnum.SUCCES);
        
        if ( !checkMaxPlay(m) ) {
            state.setState(ScreeningAdditionEnum.INCORRECT_MAX_PLAY);
            return state;
        } else if ( !checkScreeningsOverlap(m, ch, begin) ) {
            state.setState(ScreeningAdditionEnum.SCREENINGS_OVERLAP);
            return state;
        } else if ( !checkAgeLimit(m.getAgeLimit(), begin) ) {
            state.setState(ScreeningAdditionEnum.INCORRECT_AGE_LIMIT);
            return state;
        } else if ( !checkScreenedInTooManyHalls(m, begin) ) {
            state.setState(ScreeningAdditionEnum.SCREENED_IN_TOO_MANY_HALLS);
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
    
    private boolean checkMaxPlay(Movie movie) {
        // casting to use DAOScreening specific methods
        DAOScreening ds = (DAOScreening) daoScreening;
        return ( movie.getMaxPlay() > ds.getMovieCount(movie.getId()) );
    }
    
    // getting every screening with given movie
    // checking if there are more than {overlapAmount} overlaps with this movie
    // if there are return false else true
    private boolean checkScreenedInTooManyHalls(Movie m, String begin) {
        int overlapAmount = 3;
        
        // casting to use DAOScreening specific methods
        DAOScreening ds = (DAOScreening) daoScreening;

        List<Screening> screenings = ds.getDataByMovieId(m.getId());
        try {
            CustomInterval movieInterval = new CustomInterval(
                    CustomDate.stringToCustomDate(begin), m.getDuration());
            // counting overlaps with every screening with given movie
            // setting counter to 1 so when it finds an overlap
            // counter will be 2
            int overlapCounter = 1;
            for (Screening s : screenings) {
                if (s.getInterval().overlapsWith(movieInterval)) ++overlapCounter;
            }
            return overlapCounter <= overlapAmount;
            
        } catch (InvalidCustomDateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // test with cleaning
    private boolean checkScreeningsOverlap(Movie m, CinemaHall ch, String begin) {
        try {
            // adding 30 minutes to both intervals because of cleaning
            CustomInterval movieInterval = new CustomInterval(
                    CustomDate.stringToCustomDate(begin), m.getDuration() + 30);
            
            // casting to use DAOScreening specific methods
            DAOScreening dScreening = (DAOScreening) daoScreening;
            List<Screening> screenings = dScreening
                    .getScreeningsByCinemaHallId(ch.getId());
            
            for (Screening s : screenings) {
                // adding 30 minutes of cleaning time
                s.setInterval( addCleaning( s.getInterval(), 30 ) );
                // if the two overlap return false
                if( s.getInterval().overlapsWith(movieInterval) ) return false;
            }
            
        } catch (InvalidCustomDateException e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    private CustomInterval addCleaning(CustomInterval interval, int cleaningTime) {
        return new CustomInterval(interval.getBegin(), 
                                  interval.getEnd().addMinutes(cleaningTime));
    }
    
    private boolean checkAgeLimit(int ageLimit, String begin) {
        try {
            CustomDate cd = CustomDate.stringToCustomDate(begin);
            System.out.println(cd.toString());
            switch (ageLimit) {
                case 1:
                    // can be screened any time
                    return true;
                case 2:
                    return (cd.getHour() >= 17);
                case 3:
                    return (cd.getHour() >= 21);
                default:
                    System.out.println("Invalid age limit");
                    return false;
            }
        } catch (InvalidCustomDateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    /**
     * @param s
     * Screening object
     * @return false if seats are already taken to given screening
     * else it removes screening and returns true
     */
    public boolean removeScreening(Screening s) {
        // casting to use DAOSeat specific methods
        DAOSeat dSeat = (DAOSeat) daoSeat;
        if ( !dSeat.getSeatsByScreening(s.getId()).isEmpty() ) {
            return false;
        }
        
        daoScreening.removeData(s);
        return true;
    }
        
    /**
     * Adds a Seat to database for given Screening with given row and col
     * numbers.
     * @param s
     * Screening where booking is happening.
     * @param row
     * Row number of Seat.
     * @param col
     * Coloumn number of Seat.
     */
    public void addSeat(Screening s, int row, int col) {
        Object o = new int[] {s.getId(), row, col};
        daoSeat.addData(o);
    }
    
    /* ======================= FILTERS ===================================== */

    public List filterMoviesByTitle(String title) {
        // casting to use DAOMovie specific methods
        return addSoldTicketToMovie(((DAOMovie) daoMovie).getByTitle(title));
    }
    
    /**
     * Filters movies with given title arg. Reads from database then 
     * returns matching Movie objects.
     * @param title
     * String to filter with.
     * @return List containing filtered Screenings.
     */
    public List filterScreeningByMovie(String title) {
        // casting to use DAOScreening, DAOMovie specific methods
        DAOMovie dm = (DAOMovie) daoMovie;
        DAOScreening ds = (DAOScreening) daoScreening;
        
        // presuming there are unique titles
        List<Movie> movie = dm.getByTitle(title);
        return addSoldTicketToScreening(
                ds.getDataByMovieId(movie.get(0).getId()));
    }
    
    /**
     * Filters CinemaHalls with given name arg. Reads from database then 
     * returns matching CinemaHall objects.
     * @param cinemaHallName
     * @return List containing filtered CinemaHalls.
     */
    public List filterScreeningByCinemaHall(String cinemaHallName) {
        // casting to use DAOScreening, DAOMovie specific methods
        DAOScreening ds = (DAOScreening) daoScreening;
        DAOCinemaHall dch = (DAOCinemaHall) daoCinemaHall;
        
        CinemaHall ch = dch.getByName(cinemaHallName);
        return addSoldTicketToScreening(
                ds.getScreeningsByCinemaHallId(ch.getId()));
    }

}
