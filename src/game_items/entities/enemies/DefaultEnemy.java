package game_items.entities.enemies;

import config.Constants;
import core.GameLib;
import enums.EntityState;
import game_items.entities.collidable_entities.Enemy;
import interfaces.ShootingCallback;

import java.awt.Color;

public class DefaultEnemy extends Enemy {
    protected long nextShoot;
    public DefaultEnemy(double x, double y, double speed, double rotationSpeed, double projectileSpeed, ShootingCallback shootingCallback) {
        super(
                x,
                y,
                Constants.DEFAULT_ENEMY_RADIUS,
                Constants.DEFAULT_ENEMY_COLOR,
                Constants.DEFAULT_ENEMY_EXPLOSION_DURATION,
                Constants.DEFAULT_ENEMY_TOTAL_HEALTH,
                Constants.DEFAULT_ENEMY_INVULNERABILITY_DURATION,
                speed,
                rotationSpeed,
                Constants.DEFAULT_ENEMY_ANGLE,
                Constants.DEFAULT_ENEMY_PROJECTILE_RADIUS,
                Constants.DEFAULT_ENEMY_PROJECTILE_COLOR,
                projectileSpeed,
                shootingCallback
        );
    }

    @Override
    protected void checkBounds() {
        if (this.y < -10 || this.y > Constants.GAME_HEIGHT - this.size) {
            this.state = EntityState.INACTIVE;
        }
    }

    @Override
    protected void updateActiveEntity(long delta, long currentTime) {
        this.updatePositionAndAngle(delta);

        if (this.readyForShoot(currentTime)) {
            this.attemptToShoot(delta);
            this.nextShoot = currentTime + (long) (200 + Math.random() * 500);
        }
    }

    @Override
    protected void renderActiveEntity() {
        GameLib.setColor(color);
        GameLib.drawCircle(this.x, this.y, this.radius);
    }

    protected void updatePositionAndAngle(long delta) {
        this.x += this.speed * Math.cos(this.angle) * 5 * (-1);
        this.y += this.speed * Math.sin(this.angle) * 5 * (-1);
        this.angle += this.rotationSpeed * delta;
    }

    protected Boolean readyForShoot(long currentTime) {
        return currentTime > this.nextShoot;
    }

    @Override
    protected void attemptToShoot(long delta) {
        if (this.shootingCallback != null) {
            double projectileX = this.x;
            double projectileY = this.y + this.size;
            double projectileVx = Math.cos(this.angle) * this.projectileSpeed * (-1.0);
            double projectileVy = Math.sin(this.angle) * this.projectileSpeed * (-1.0);
            this.shootingCallback.shoot(projectileX, projectileY, projectileVx, projectileVy, this.projectileRadius, this.projectileColor, this.getClass());
        }
    }

    @Override
    public void renderTakingDamage() {
        GameLib.setColor(Color.WHITE);
        GameLib.fillOval(this.x, this.y, this.size, this.size);
    }
}
