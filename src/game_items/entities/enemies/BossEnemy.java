package game_items.entities.enemies;

import config.Constants;
import core.GameLib;
import enums.EntityState;
import game_items.entities.collidable_entities.Enemy;
import interfaces.ShootingCallback;

import java.awt.*;

enum BossEnemyState {
    MOVING_RIGHT,
    STOPPED_SHOOTING,
    MOVING_AFTER_SHOOTING
}
public class BossEnemy extends Enemy {
    private final double [] projectileAngles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
    private BossEnemyState currentState = BossEnemyState.MOVING_RIGHT;
    private long startShootTime;
    private long statesDuration = 2000;
    private long shootsDuration = 700;
    public BossEnemy(double x, double y, double speed, double rotationSpeed, double projectileSpeed, ShootingCallback shootingCallback) {
        super(
                x,
                y,
                Constants.BOSS_ENEMY_RADIUS,
                Constants.BOSS_ENEMY_COLOR,
                Constants.BOSS_ENEMY_EXPLOSION_DURATION,
                Constants.BOSS_ENEMY_TOTAL_HEALTH,
                Constants.BOSS_ENEMY_INVULNERABILITY_DURATION,
                speed,
                rotationSpeed,
                Constants.BOSS_ENEMY_ANGLE,
                Constants.BOSS_ENEMY_PROJECTILE_RADIUS,
                Constants.BOSS_ENEMY_PROJECTILE_COLOR,
                projectileSpeed,
                shootingCallback
        );
    }

    @Override
    protected void checkBounds() {
        if (x < -10 || x > Constants.GAME_WIDTH + 10) {
            this.state = EntityState.INACTIVE;
        }
    }

    @Override
    protected void updateActiveEntity(long delta, long currentTime) {
        switch (this.currentState) {
            case MOVING_RIGHT:
                // Movimento inicial para a direita
                this.x += this.speed * delta / 5.0;

                // Verifica se chegou ao meio da tela
                if (this.x >= Constants.GAME_WIDTH / 2) {
                    this.currentState = BossEnemyState.STOPPED_SHOOTING;
                    this.startShootTime = currentTime + statesDuration; // Tempo atual + 2000ms (2 segundos)
                }
                break;

            case STOPPED_SHOOTING:
                // Inimigo está parado e deve atirar
                if (currentTime >= this.startShootTime) {
                    this.attemptToShoot(delta);
                }

                // Verifica se já atirou por X segundos e reinicia o movimento
                if (currentTime >= this.startShootTime + this.shootsDuration) {
                    this.currentState = BossEnemyState.MOVING_AFTER_SHOOTING;
                }
                break;

            case MOVING_AFTER_SHOOTING:
                // Reinicia o movimento para a direita
                this.x += this.speed * delta / 5.0;
                break;
        }
    }

    @Override
    protected void attemptToShoot(long delta) {
        if (this.shootingCallback != null) {
            for (double projectileAngle : projectileAngles) {
                double angle = projectileAngle + Math.random() * Math.PI/6 - Math.PI/12;
                double vx = Math.cos(angle) * 0.30;
                double vy = Math.sin(angle) * 0.30;
                this.shootingCallback.shoot(x, y, vx, vy, this.projectileRadius, this.projectileColor, super.getClass());
            }
        }
    }

    protected void renderHealthBar() {
        GameLib.drawBossHealthBar(currentHealth, totalHealth);
    }

    @Override
    public void render() {
        if (state != EntityState.EXPLODING) {
            this.renderHealthBar();
        }
        super.render();
    }

    @Override
    public void renderTakingDamage() {
        GameLib.setColor(Color.WHITE);
        GameLib.fillSpaceship(this.x, this.y, this.size/2);
    }

    @Override
    protected void renderActiveEntity() {
        GameLib.setColor(color);
        GameLib.drawSpaceship(x, y, radius);
    }
}
