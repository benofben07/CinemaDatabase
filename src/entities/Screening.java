package entities;

import date.CustomDate;

public class Screening {
    private final Movie movie;
    private final CinemaHall cinemaHall;
    private final int id;
   // private CustomDate begin;
    private final String begin;
    private CustomDate endTimePlus30Minutes;

    public Screening(int id, Movie movie, CinemaHall cinemaHall, String begin) {
        this.id         = id;
        this.movie      = movie;
        this.cinemaHall = cinemaHall;
        this.begin      = begin;//CustomDate.stringToCustomDate(begin);
        //endTimePlus30Minutes.calculate(startTime, movie.getDuration());
    }
    
    public int getId() {
        return id;
    }
    
    public Movie getMovie() {
        return movie;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }

    public int getFreeSpaces() {
        return cinemaHall.getNumOfCols() * cinemaHall.getNumOfRows();
    }

    /*public CustomDate getStartTime() {
        return startTime;
    }*/

    public CustomDate getEndTimePlus30Minutes() {
        return endTimePlus30Minutes;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(id).append(", ").append(movie.toString())
                .append(", ").append(cinemaHall.toString()).append(", ")
                /*.append(freeSpaces).append(", ")*/.append(begin).toString();
    }
    
}
