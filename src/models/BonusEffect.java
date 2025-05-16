package models;

import enums.BonusType;

public class BonusEffect {
    protected long initialTime;
    protected long duration;
    protected BonusType type;

    public BonusEffect(long initialTime, long duration, BonusType type) {
        this.initialTime = initialTime;
        this.duration = duration;
        this.type = type;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public long getDuration() {
        return duration;
    }

    public BonusType getType() {
        return type;
    }

    public boolean hasFinished(long currentTime) {
        return currentTime > initialTime + duration;
    }

    public void resetInitialTime() {
        this.initialTime = System.currentTimeMillis();
    }
}
