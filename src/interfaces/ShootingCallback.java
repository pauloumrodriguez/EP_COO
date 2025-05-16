package interfaces;

import game_items.Entity;

import java.awt.Color;

public interface ShootingCallback {
    void shoot(double x, double y, double vx, double vy, double radius, Color color, Class<? extends Entity> entityType);
}