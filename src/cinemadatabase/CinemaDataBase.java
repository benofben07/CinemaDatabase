package cinemadatabase;

import controller.Controller;

public class CinemaDataBase {

    public static void main(String[] args) throws InterruptedException {
        DBInit.setupDB();
        Controller c = new Controller(); 
    }
}
