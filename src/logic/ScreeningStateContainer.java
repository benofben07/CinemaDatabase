package logic;

public class ScreeningStateContainer {
    private ScreeningAdditionEnum state;
    
    public ScreeningStateContainer(ScreeningAdditionEnum state) {
        this.state = state;
    }

    public ScreeningAdditionEnum getState() {
        return state;
    }

    public void setState(ScreeningAdditionEnum state) {
        this.state = state;
    }
    
    @Override
    public String toString() {
        return getState().toString();
    }
}
