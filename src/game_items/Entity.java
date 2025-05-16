package game_items;

import enums.EntityState;
import interfaces.GameItem;

import java.awt.Color;

public abstract class Entity implements GameItem {
    protected double x, y;
    protected double radius;
    protected Color color;
    protected EntityState state;

    public Entity(double x, double y, double radius, Color color, EntityState state) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.state = state;
    }

    @Override
    public void update(long delta) {
        long currentTime = System.currentTimeMillis();

        this.checkBounds();

        if (this.state == EntityState.ACTIVE) {
            this.updateActiveEntity(delta, currentTime);
        }
    }

    @Override
    public void render() {
        if (this.state == EntityState.ACTIVE) {
            this.renderActiveEntity();
        }
    }

    protected abstract void checkBounds();

    protected abstract void updateActiveEntity(long delta, long currentTime);

    protected abstract void renderActiveEntity();

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getRadius() {
        return this.radius;
    }

    public EntityState getState() {
        return this.state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }
}


