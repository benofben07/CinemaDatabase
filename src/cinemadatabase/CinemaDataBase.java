package cinemadatabase;

import date.CustomDate;
import date.InvalidCustomDateException;
import entities.Movie;
import entities.Screening;
import entities.Seat;
import java.util.List;
import java.util.Map;
import mvc.Controller;

public class CinemaDataBase {

    public static void main(String[] args) {
        DBInit.setupDB();
        Controller c = new Controller(); 
        
        //c.getMovies();
        /*c.addMovie();
        c.getMovies();
        System.out.println("");
        c.getCinemaHalls();
        c.getMovieById(1);
        System.out.println("");
        c.getCHById(2);
        c.getScreenings();
        c.addScreening(1, 2, "most");
        System.out.println("After modification");
        c.getScreenings();
        Movie m = Movie.createMovieFromString("SZAMURAJ2,MAGYAR2,Y,BELA2,EZ EGY SZAR2,52,2,2,NINCS2");
        System.out.println(m.toString());
        c.printMovieWithoutId(m);*/
        
        //System.out.println("Max play of movie Szamuraj2: " + c.getMovieMaxPlay("SZAMURAJ2,MAGYAR2,BELA2"));
        
        /*printMap(c.listMovies());
        System.out.println("");
        System.out.println(c.filterByMovie("SZAMURAJ2").toString());
        System.out.println(c.filterByCinemaHall("ANYAD MOZI2").toString());
        System.out.println("Screenings: " + c.getScreenings());
        c.getSeats();*/
        
        //System.out.println(c.listScreening());
        List<Screening> s = c.getScreenings();
        c.addSeat(s.get(0), 3, 4);
        System.out.println(c.getBookedSeats(s.get(0)));
        
    }
    
    public static void printMap(Map m) {
        System.out.println(m.toString());
    }
    
}
