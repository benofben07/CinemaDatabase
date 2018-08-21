package cinemadatabase;

import controller.CinemaController;

public class CinemaDataBase {

    public static void main(String[] args) throws InterruptedException {
        DBInit.setupDB();
        CinemaController c = new CinemaController(); 
    }
}
