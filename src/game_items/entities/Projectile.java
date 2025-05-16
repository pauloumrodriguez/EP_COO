package game_items.entities;

import config.Constants;
import core.GameLib;
import enums.EntityState;
import game_items.Entity;

import java.awt.Color;

public class Projectile extends Entity {
    protected double vx, vy;
    private Class<? extends Entity> originEntityType;

    public Projectile(double x, double y, double vx, double vy, double radius, Color color, Class<? extends Entity> originEntityType) {
        super(x, y, radius, color, EntityState.ACTIVE);
        this.vx = vx;
        this.vy = vy;
        this.originEntityType = originEntityType;
    }

    @Override
    protected void checkBounds() {
        double size = this.radius * 2;

        if (this.x < size || this.x > Constants.GAME_WIDTH - size || this.y < size || this.y > Constants.GAME_HEIGHT - size) {
            this.state = EntityState.INACTIVE;
        }
    }

    @Override
    protected void updateActiveEntity(long delta, long currentTime) {
        this.x += this.vx * delta;
        this.y += this.vy * delta;
    }

    @Override
    protected void renderActiveEntity() {
        GameLib.setColor(color);
        GameLib.drawLine(x, y - 5, x, y + 5);
        GameLib.drawLine(x - 1, y - 3, x - 1, y + 3);
        GameLib.drawLine(x + 1, y - 3, x + 1, y + 3);
    }

    public Class<? extends Entity> getOriginEntityType() {
        return originEntityType;
    }
}
