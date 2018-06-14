package logic.entities;

import date.CustomDate;
import date.CustomInterval;
import date.InvalidCustomDateException;
import java.util.Objects;

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
    public boolean equals(Object other) {
        // lazy solution but must be working.
        return this.id == ((Screening) other).id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.movie);
        hash = 83 * hash + Objects.hashCode(this.cinemaHall);
        hash = 83 * hash + this.id;
        hash = 83 * hash + Objects.hashCode(this.interval);
        return hash;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(id).append(", ").append(movie.toString())
                .append(", ").append(cinemaHall.toString()).append(", ")
                .append(interval.beginToString()).toString();
    }
    
}
