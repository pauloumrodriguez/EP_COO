package game_items.entities.enemies;

import config.Constants;
import core.GameLib;
import enums.EntityState;
import game_items.entities.collidable_entities.Enemy;
import interfaces.ShootingCallback;

import java.awt.Color;

public class StrongerEnemy extends Enemy {
    private boolean shootNow = false;
    private final double [] projectileAngles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
    public StrongerEnemy(double x, double y, Double speed, double rotationSpeed, double projectileSpeed, ShootingCallback shootingCallback) {
        super(
                x,
                y,
                Constants.STRONGER_ENEMY_RADIUS,
                Constants.STRONGER_ENEMY_COLOR,
                Constants.STRONGER_ENEMY_EXPLOSION_DURATION,
                Constants.STRONGER_ENEMY_TOTAL_HEALTH,
                Constants.STRONGER_ENEMY_INVULNERABILITY_DURATION,
                speed,
                rotationSpeed,
                Constants.STRONGER_ENEMY_ANGLE,
                Constants.STRONGER_ENEMY_PROJECTILE_RADIUS,
                Constants.STRONGER_ENEMY_PROJECTILE_COLOR,
                projectileSpeed,
                shootingCallback
        );
    }

    @Override
    protected void updateActiveEntity(long delta, long currentTime) {
        double previousY = y;
        this.x += this.speed * Math.cos(this.angle) * delta;
        this.y += this.speed * Math.sin(this.angle) * delta * (-1.0);
        this.angle += this.rotationSpeed * delta;

        // Verifica se cruzou o threshold e ajusta a velocidade de rotação
        double threshold = Constants.GAME_HEIGHT * 0.30;
        if (previousY < threshold && this.y >= threshold) {
            this.rotationSpeed = x < Constants.GAME_WIDTH / 2 ? 0.003 : -0.003;
        }

        // Ajusta a rotação para alinhar com o eixo Y e prepara para disparo
        if (this.rotationSpeed > 0 && Math.abs(this.angle - 3 * Math.PI) < 0.05) {
            this.rotationSpeed = 0.0;
            this.angle = 3 * Math.PI;
            this.shootNow = true;
        }

        if (this.rotationSpeed < 0 && Math.abs(this.angle) < 0.05) {
            this.rotationSpeed = 0.0;
            this.angle = 0.0;
            this.shootNow = true;
        }

        this.attemptToShoot(delta);
    }

    @Override
    public void renderTakingDamage() {
        GameLib.setColor(Color.WHITE);
        GameLib.fillOval(this.x, this.y, this.size, this.size);
    }

    @Override
    protected void renderActiveEntity() {
        GameLib.setColor(color);
        GameLib.drawDiamond(x, y, radius);
    }

    @Override
    protected void checkBounds() {
        if (this.x < -10 || this.x >= Constants.GAME_WIDTH || this.y < -10 || this.y >= Constants.GAME_HEIGHT) {
            this.state = EntityState.INACTIVE;
        }
    }

    @Override
    protected void attemptToShoot(long delta) {
        if (this.shootNow && this.shootingCallback != null) {
            for (double projectileAngle : projectileAngles) {
                double angle = projectileAngle + Math.random() * Math.PI/6 - Math.PI/12;
                double vx = Math.cos(angle) * 0.30;
                double vy = Math.sin(angle) * 0.30;
                this.shootingCallback.shoot(x, y, vx, vy, this.projectileRadius, this.projectileColor, super.getClass());
            }
            shootNow = false;
        }
    }
}


