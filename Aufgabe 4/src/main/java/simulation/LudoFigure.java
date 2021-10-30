package simulation;

public class LudoFigure {

    public int currentPosition;
    public int distanceFromStart;

    public LudoFigure(int currentPosition) {
        this.currentPosition = currentPosition;
        this.distanceFromStart = 0;
    }

    public void move(int newPosition, int delta) {
        this.currentPosition = newPosition;
        this.distanceFromStart += delta;
    }

    public void moveToGoal(int positionInGoal) {
        this.currentPosition = - (positionInGoal + 1);
        this.distanceFromStart = LudoField.ROUND_COMPLETE + positionInGoal;
    }

    public void returnToHome() {
        this.currentPosition = -1;
        this.distanceFromStart = 0;
    }

}
