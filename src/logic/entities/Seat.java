package logic.entities;

public class Seat {
    private final Screening screening;
    private final int id, rowNumber, colNumber;

    public Seat(int id, Screening screening, int rowNumber, int colNumber) {
        this.id = id;
        this.screening = screening;
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
    }

    public Screening getScreening() {
        return screening;
    }
    
    public Movie getMovie() {
        return screening.getMovie();
    }
    
    public CinemaHall getCinemaHall() {
        return screening.getCinemaHall();
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColNumber() {
        return colNumber;
    }
    
    @Override
    public String toString() {
        return new StringBuilder()
                .append(id).append(",")
                .append(screening.getId()).append(",")
                .append(rowNumber).append(",")
                .append(colNumber)
                .toString();
    }
}
