package logic;

import logic.dao.*;
import logic.date.*;
import logic.entities.*;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import java.util.stream.Collectors;

/* TODO don't allow movie title duplicate */
public class CinemaService {
   
    private DAOGeneral daoScreening;
    private DAOGeneral daoMovie;
    private DAOGeneral daoSeat;
    private DAOGeneral daoCinemaHall;
    
    public CinemaService() {
         createConnection();
    }
   
    private void createConnection() {
        daoMovie = new DAOMovie();
        daoCinemaHall = new DAOCinemaHall();
        
        // screening and seat needs to communicate with logic 
        // TODO Should not
        daoScreening = new DAOScreening(this);
        daoSeat = new DAOSeat(this);
    }
        
    /* ------------------------- MOVIE ----------------------------- */

    public List<Movie> getMovies() {
        return daoMovie.getData();
    }

    /**
     * Gets all movies and solt tickets to them as a list with Pairs.
     * 
     * @return List with Pairs<Movie, sold ticket amount>
     */
    public List<Pair<Movie, Integer>> getMoviesWithSoldTickets() { 
        return addSoldTicketToMovie(getMovies());
    }

    private int getMovieSoldTicket(int movieId) {
        // casting to use DAOScreening, DAOSeat specific methods
        final DAOScreening dScreening = (DAOScreening) daoScreening;
        final DAOSeat dSeat = (DAOSeat) daoSeat;
        final List<Screening> screenings = dScreening.getDataByMovieId(movieId);
       
        int soldTickets = 0;
        soldTickets = screenings.stream()
                .map(s -> dSeat.getSeatsByScreening(s.getId()).size())
                .reduce(soldTickets, Integer::sum);
        
        return soldTickets;
    }
    
    /**
     * @param movieRaw
     * Expects a movie in raw format from View
     * For example 'Title,English,David Yates'
     * @return max play value of given movie
     * or -1 if no movies found with given string.
     */
    public int getMovieMaxPlay(String movieRaw) {
        final List<Movie> movies = daoMovie.getData();
        // title, origin, director
        final String[] data = movieRaw.split(",");
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
     * 
     * @param id
     * Movie id
     * @return Movie object
     */
    public Movie getMovieById(int id) {
        // casting to use DAOMovie specific methods
        final DAOMovie dm = (DAOMovie) daoMovie;
        return dm.getById(id);
    }
    
    /**
     * Gets ID for a Movie object and returs completed object.
     * 
     * @param m
     * Movie without id (defaultly set to -1)
     * @return complete Movie object with id
     */
    public Movie getMovieWithoutId(Movie m) {
        // casting to use DAOMovie specific methods
        final DAOMovie dm = (DAOMovie) daoMovie;
        return dm.getMovieWithoutId(m);
    }
    
    public Movie getByTitle(String title) {
        return ((DAOMovie) daoMovie).getByTitle(title);
    }

    /**
     * @return list of pairs <movie, sold ticket amount>
     */    
    private List<Pair<Movie, Integer>> addSoldTicketToMovie(List<Movie> movies) {
        final List<Pair<Movie, Integer>> movieSoldTicket = new ArrayList<>();
        movies.forEach(m -> movieSoldTicket
            .add(new Pair(m, getMovieSoldTicket(m.getId()))));
        return movieSoldTicket;
    }
    
    /* ------------------------ CINEMA_HALL ----------------------------- */

    public CinemaHall getCinemaHallById(int id) {
        // casting to use DAOScreening, DAOCinemaHall specific methods
        final DAOCinemaHall dch = (DAOCinemaHall) daoCinemaHall;
        return dch.getById(id);
    }
    
    public CinemaHall getCinemaHallByName(String name) {
        return ((DAOCinemaHall) daoCinemaHall).getByName(name);
    }
    
    public List<CinemaHall> getCinemaHalls() {
        return daoCinemaHall.getData();
    }
    
    /* ------------------------ SCREENING ------------------------------ */

    /**
     * Gets every Screening with their respective sold ticket amount.
     * 
     * @return List with Pairs <Screening, sold ticket amount>
     */
    public List<Pair<Screening, Integer>> getScreeningsWithSoldTickets() {
        return addSoldTicketToScreening(getScreenings());
    }
    
    /**
     * @return sold tickets to given Screening
     */
    private int getScreeningSoldTicket(int screeningId) {
        // casting to use DAOSeat specific methods
        final DAOSeat dSeat = (DAOSeat) daoSeat;
        return dSeat.getSeatsByScreening(screeningId).size();
    }
    
    public List<Screening> getScreenings() {
        return daoScreening.getData();
    }
    
    public Screening getScreeningById(int id) {
        // casting to use DAOScreening specific methods
        final DAOScreening ds = (DAOScreening) daoScreening;
        return ds.getScreeningById(id);
    }
   
    /**
     * @return List of pairs of <screening, sold ticket amount>.
     */
    private List<Pair<Screening, Integer>> addSoldTicketToScreening(
            List<Screening> screenings) {
        final List<Pair<Screening, Integer>> screeningAndSoldTicket = new ArrayList<>();
        screenings.forEach(s -> screeningAndSoldTicket
                .add(new Pair(s, getScreeningSoldTicket(s.getId()))));
        return screeningAndSoldTicket;
    }
    
    /* ------------------------ SEATS --------------------------------- */

    /**
     * @return List with Pairs with structure <row, col>
     */
    public List<Pair<Integer, Integer>> getBookedSeats(Screening s) {
        final List<Seat> seats = ((DAOSeat) daoSeat).getSeatsByScreening(s.getId());
        final List<Pair<Integer, Integer>> bookedSeats = new ArrayList<>();
        seats.forEach(seat -> bookedSeats
                .add(new Pair(seat.getRowNumber(), seat.getColNumber())));
    
        return bookedSeats;
    }
    
    public List<Seat> getSeats() {
        return daoSeat.getData();
    }
    
    /* ======================= SETTERS ===================================== */

    public void addMovies(Movie movie) {
        daoMovie.addData((Object) movie);
    }
    
    /**
     * @param begin
     * CustomDate in raw format (e.g. '2015-12-21 12:30:00')
     * @return ScreeningStateContainer object containing any problem
     * or success.
     */
    public ScreeningStateContainer addScreening(Movie m, CinemaHall ch,
                                                String begin) {
        // checking w/ constraints
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
        final List<Object> data = asList(m.getId(), ch, begin);
        daoScreening.addData(data);
        return state;
    }
    
    private boolean checkMaxPlay(Movie movie) {
        // casting to use DAOScreening specific methods
        final DAOScreening ds = (DAOScreening) daoScreening;
        return ( movie.getMaxPlay() > ds.getMovieCount(movie.getId()) );
    }
    
    // getting every screening with given movie
    // checking if there are more than {overlapAmount} overlaps with this movie
    // if there are return false else true
    private boolean checkScreenedInTooManyHalls(Movie m, String begin) {
        int overlapAmount = 3;
        
        // casting to use DAOScreening specific methods
        final DAOScreening ds = (DAOScreening) daoScreening;

        final List<Screening> screenings = ds.getDataByMovieId(m.getId());
        try {
            final CustomInterval movieInterval = new CustomInterval(
                    CustomDate.stringToCustomDate(begin), m.getDuration());
            // counting overlaps with every screening with given movie
            // setting counter to 1 so when it finds an overlap
            // counter will be 2
            int overlapCounter = 1;
            overlapCounter = screenings.stream()
                    .filter(s -> s.getInterval().overlapsWith(movieInterval))
                    /* todo a map mi a fasznak?*/
                    .map(item -> 1).reduce(overlapCounter, Integer::sum);
            return overlapCounter <= overlapAmount;
            
        } catch (InvalidCustomDateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // test with cleaning
    private boolean checkScreeningsOverlap(Movie m, CinemaHall ch, String begin) {
        // casting to use DAOScreening specific methods
        final DAOScreening dScreening = (DAOScreening) daoScreening;
        final List<Screening> screenings = dScreening
                .getScreeningsByCinemaHallId(ch.getId());
        
        CustomInterval movieInterval = null;
        try {
             // adding 30 minutes to both intervals because of cleaning
            movieInterval = new CustomInterval(
                    CustomDate.stringToCustomDate(begin), m.getDuration() + 30);
        } catch (InvalidCustomDateException e) {
            System.out.println(e.getMessage());
            return false;
        } 
        
        for (Screening s : screenings) {
            // adding 30 minutes of cleaning time
            s.setInterval( addCleaning( s.getInterval(), 30 ) );
            // if the two overlap return false
            if( s.getInterval().overlapsWith(movieInterval) ) return false;
        }

        return true;
    }
    
    private CustomInterval addCleaning(CustomInterval interval, int cleaningTime) {
        return new CustomInterval(interval.getBegin(), 
                                  interval.getEnd().addMinutes(cleaningTime));
    }
    
    private boolean checkAgeLimit(int ageLimit, String begin) {
        try {
            final CustomDate cd = CustomDate.stringToCustomDate(begin);
            switch (ageLimit) {
                // can be screened any time
                case 1:
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
     * @return false if seats are already taken to given screening
     * else it removes screening and returns true
     */
    public boolean removeScreening(Screening s) {
        // casting to use DAOSeat specific methods
        final DAOSeat dSeat = (DAOSeat) daoSeat;
        if ( !dSeat.getSeatsByScreening(s.getId()).isEmpty() ) {
            return false;
        }
        
        daoScreening.removeData(s);
        return true;
    }

    // TODO change
    public void addSeat(Screening s, int row, int col) {
        final Object o = new int[] {s.getId(), row, col};
        daoSeat.addData(o);
    }
    
    /* ======================= FILTERS ===================================== */

    public List<Movie> filterMoviesByTitle(String title) {
        return addSoldTicketToMovie(singletonList(((DAOMovie) daoMovie).getByTitle(title)))
                .stream().map(Pair::getKey).collect(Collectors.toList());
    }
    
    // TODO check if deez nuts good
    /**
     * @return List containing filtered Screenings.
     */
    public List<Pair<Screening, Integer>> filterScreeningByMovie(String title) {
        // casting to use DAOScreening, DAOMovie specific methods
        final DAOMovie dm = (DAOMovie) daoMovie;
        final DAOScreening ds = (DAOScreening) daoScreening;
        
        return addSoldTicketToScreening(
                ds.getDataByMovieId(dm.getByTitle(title).getId()));
    }
    
    /**
     * @return List containing filtered CinemaHalls.
     */
    public List<Pair<Screening, Integer>> filterScreeningByCinemaHall(String cinemaHallName) {
        // casting to use DAOScreening, DAOMovie specific methods
        DAOScreening ds = (DAOScreening) daoScreening;
        DAOCinemaHall dch = (DAOCinemaHall) daoCinemaHall;
        
        CinemaHall ch = dch.getByName(cinemaHallName);
        return addSoldTicketToScreening(
                ds.getScreeningsByCinemaHallId(ch.getId()));
    }
}
