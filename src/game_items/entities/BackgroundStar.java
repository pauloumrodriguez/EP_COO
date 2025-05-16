package game_items.entities;

import config.Constants;
import core.GameLib;
import enums.EntityState;
import game_items.Entity;

import java.awt.Color;

public class BackgroundStar extends Entity {

    private double speed;
    private int width;
    private int height;

    public BackgroundStar(double x, double y, double radius, Color color, double speed, int width, int height) {
        super(x, y, radius, color, EntityState.ACTIVE);
        this.speed = speed;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void updateActiveEntity(long delta, long currentTime) {
        this.y = (this.y + this.speed * delta) % Constants.GAME_HEIGHT;
    }

    @Override
    protected void renderActiveEntity() {
        GameLib.setColor(this.color);
        GameLib.fillRect(this.x, this.y, this.width, this.height);
    }

    @Override
    protected void checkBounds() {
        if (this.y > Constants.GAME_HEIGHT - height) {
            this.y = 0;
            this.x = Math.random() * Constants.GAME_WIDTH;
        }
        if (this.x > Constants.GAME_WIDTH - width) {
            this.x = this.x - 10;
        }
    }
}
