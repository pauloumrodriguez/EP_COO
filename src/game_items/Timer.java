package game_items;

import core.GameLib;
import interfaces.GameItem;

public class Timer implements GameItem {
    protected long currentTime;
    protected long initialTime;

    public Timer() {
        this.initialTime = System.currentTimeMillis();
    }
    @Override
    public void update(long delta) {
        if (System.currentTimeMillis() - this.currentTime > 1000) {
            this.currentTime = System.currentTimeMillis() - this.initialTime;
        }
    }

    @Override
    public void render() {
        GameLib.drawTimer(currentTime);
    }
}
