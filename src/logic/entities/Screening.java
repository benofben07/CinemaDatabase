package logic.entities;

import date.CustomDate;
import date.CustomInterval;
import date.InvalidCustomDateException;

public class Screening {
    private final Movie movie;
    private final CinemaHall cinemaHall;
    private final int id;
    private CustomInterval interval;

    public Screening(int id, Movie movie, CinemaHall cinemaHall, String begin) {
        this.id         = id;
        this.movie      = movie;
        this.cinemaHall = cinemaHall;
        try {
            this.interval  = new CustomInterval(
                    CustomDate.stringToCustomDate(begin), movie.getDuration());
        } catch (InvalidCustomDateException e) {
            System.out.println(e.getMessage());
        }
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

    public CustomInterval getInterval() {
        return interval;
    }
    
    public void setInterval(CustomInterval interval) {
        this.interval = interval;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(id).append(", ").append(movie.toString())
                .append(", ").append(cinemaHall.toString()).append(", ")
                .append(interval.beginToString()).toString();
    }
    
}
