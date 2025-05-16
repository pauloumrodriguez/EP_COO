package game_items.entities.collidable_entities;

import config.Constants;
import enums.EntityState;
import game_items.entities.CollidableEntity;
import interfaces.ShootingCallback;

import java.awt.Color;

public abstract class Enemy extends CollidableEntity {
    protected double speed;
    protected double rotationSpeed;
    protected double radius;
    protected double angle;
    protected double projectileRadius;
    protected Color projectileColor;
    protected double projectileSpeed;
    protected ShootingCallback shootingCallback;
    protected double size;

    public Enemy(double x, double y, double radius, Color color, double explosionDuration, int livesTotal, long invulnerabilityDuration, double speed, double rotationSpeed, double angle,
                 double projectileRadius, Color projectileColor, double projectileSpeed, ShootingCallback shootingCallback) {
        super(x, y, radius, color, EntityState.ACTIVE, explosionDuration, livesTotal, invulnerabilityDuration);
        this.radius = radius;
        this.speed = speed;
        this.rotationSpeed = rotationSpeed;
        this.angle = angle;
        this.projectileRadius = projectileRadius;
        this.projectileColor = projectileColor;
        this.projectileSpeed = projectileSpeed;
        this.shootingCallback = shootingCallback;
        this.size = radius * 2;
    }

    @Override
    protected void checkBounds() {
        if (y < size || y > Constants.GAME_HEIGHT - size) {
            this.state = EntityState.INACTIVE;
        }
    }

    protected abstract void attemptToShoot(long delta);
}
