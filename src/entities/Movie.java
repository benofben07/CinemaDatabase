package entities;

public class Movie {
    
    private final String title, origin, director, description, picture;
    private final boolean dubbed;
    private final int id, duration, maxPlay, ageLimit;

    // constructor to add movie to database
    // id is generated by database so setting to invalid
    public Movie(String title, String origin, boolean dubbed, 
                 String director, String description, int duration, 
                 int maxPlay, int ageLimit, String picture) {
        this.id          = -1;
        this.title       = title;
        this.origin      = origin;
        this.director    = director;
        this.description = description;
        this.dubbed      = dubbed;
        this.duration    = duration;
        this.maxPlay     = maxPlay;
        this.ageLimit    = ageLimit;
        this.picture     = picture;
    }
    
    public Movie(int id, String title, String origin, boolean dubbed, 
                 String director, String description, int duration, 
                 int maxPlay, int ageLimit, String picture) {
        this.id          = id;
        this.title       = title;
        this.origin      = origin;
        this.director    = director;
        this.description = description;
        this.dubbed      = dubbed;
        this.duration    = duration;
        this.maxPlay     = maxPlay;
        this.ageLimit    = ageLimit;
        this.picture     = picture;
    }
    
    /*public static Movie createMovieFromString(String raw) {
        String[] data = raw.split(",");
        System.out.println("data in array: ");
        for (String s : data) {
            System.out.println(s + ",");
        }
        boolean dubbed = data[2].equals("Y");
        
        return new Movie(
            data[0],
            data[1],
            dubbed,
            data[3],
            data[4],
            Integer.parseInt(data[5]),
            Integer.parseInt(data[6]),
            Integer.parseInt(data[7]),
            data[8]
        );
    }*/

    public String getDubbed() {
        return dubbed ? "Y" : "N";
    }
    
    public String getTitle() {
        return title;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDirector() {
        return director;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public boolean isDubbed() {
        return dubbed;
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public int getMaxPlay() {
        return maxPlay;
    }

    public int getAgeLimit() {
        return ageLimit;
    }
    
    // for example 1,SZAMURAJ,MAGYAR,Y,BELA,EZ EGY SZAR,50,3,1,NINCS
    @Override
    public String toString() {
        char dubbedChar = (dubbed) ? 'Y' : 'N';
        
        return new StringBuilder()
        .append(id)         .append(",")
        .append(title)      .append(",")
        .append(origin)     .append(",")
        .append(dubbedChar) .append(",")
        .append(director)   .append(",")
        .append(description).append(",")
        .append(duration)   .append(",")
        .append(maxPlay)    .append(",")
        .append(ageLimit)   .append(",")
        .append(picture)
        .toString();
    }
    
}
