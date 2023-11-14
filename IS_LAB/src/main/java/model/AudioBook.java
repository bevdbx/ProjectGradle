package model;

public class AudioBook extends Book{
    private int runTime;

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return super.toString() + " | Run Time: " + runTime + " minutes";
    }
}
