package cinemadatabase;

import date.CustomDate;
import date.CustomInterval;
import date.InvalidCustomDateException;
import logic.entities.Movie;
import logic.entities.Screening;
import logic.entities.Seat;
import java.util.List;
import java.util.Map;
import controller.Controller;
import logic.ScreeningAdditionEnum;
import logic.ScreeningStateContainer;

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
        /*List<Screening> s = c.getScreenings();
        c.addSeat(s.get(0), 3, 4);
        System.out.println(c.getBookedSeats(s.get(0)));*/
        
        /* beautifully working
        try {
            CustomDate cd = CustomDate.stringToCustomDate("2015-05-05 16:30:00");
            CustomDate cd2 = new CustomDate(cd);
            CustomDate cd3 = CustomDate.minutesToCustomDate(150);
            System.out.println(cd2.addMinutes(150).compareTo(cd.addHms(cd3)));
        } catch (InvalidCustomDateException e) {
            System.out.println(e.getMessage());
        }*/
        /* works beautifully
        try {
            CustomDate cd = CustomDate.minutesToCustomDate(300);
            CustomDate cd2 = CustomDate.minutesToCustomDate(299);
            CustomInterval ci = new CustomInterval(cd, 150);
            CustomInterval ci2 = new CustomInterval(cd2, 121);
            System.out.println(ci.overlapsWith(ci2));
        } catch (InvalidCustomDateException e) {
            System.out.println(e.getMessage());
        } */
        /*System.out.println(c.listScreening());
        System.out.println(c.listMovies());*/
        
        /* seems working
        System.out.println("Old screening: " + c.listScreening());
       // System.out.println("sc: " + c.newScreening(1, 2, "2018-06-20 17:00:00"));
        System.out.println("sc: " + c.newScreening(3, 3, "2018-06-10 17:00:00"));
        System.out.println("sc: " + c.newScreening(1, 3, "2018-06-10 19:59:00"));
        System.out.println("new screening: " + c.listScreening());
        */
        
    }
    
}
