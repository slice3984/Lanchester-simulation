package utils;

public class Clock {
    private long snapshotTime;
    public Clock() {
        snapshotTime = 0;
    }

    public void start() {
        snapshotTime = System.currentTimeMillis();
    }

    public double getDeltaSeconds() {
        double delta = System.currentTimeMillis() - snapshotTime;
        return delta / 1000;
    }

    public void takeSnapshot() {
        snapshotTime = System.currentTimeMillis();
    }

    public double getDeltaMillis() {
        return System.currentTimeMillis() - snapshotTime;
    }
}
