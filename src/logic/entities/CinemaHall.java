package logic.entities;

public class CinemaHall {
    private final String name;
    private final int id, numOfRows, numOfCols;

    public CinemaHall(int id, String name, int numOfRows, int numOfCols) {
        this.id = id;
        this.name = name;
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNumOfCols() {
        return numOfCols;
    }

    public int getFreeSpace(){
        return numOfCols * numOfRows;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(id).append(", ")
                .append(name).append(", ").append(numOfRows)
                .append(", ").append(numOfCols).toString();
    }
    
}
