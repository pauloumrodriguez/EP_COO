package game_items.entities.collidable_entities;

import config.Constants;
import core.GameLib;
import enums.BonusType;
import enums.EntityState;
import models.BonusEffect;
import game_items.entities.CollidableEntity;
import interfaces.ShootingCallback;

import java.awt.Color;

public class Player extends CollidableEntity {
    protected double vx, vy;
    protected long nextShot;
    protected Color color;
    protected double projectileRadius;
    protected Color projectileColor;
    protected double projectileSpeed;
    protected ShootingCallback shootingCallback;
    protected double size;
    protected BonusEffect bonusEffect;

    public Player(ShootingCallback shootingCallback) {
        super(Constants.PLAYER_START_X, Constants.PLAYER_START_Y, Constants.PLAYER_RADIUS, Constants.PLAYER_COLOR, EntityState.ACTIVE, Constants.PLAYER_EXPLOSION_DURATION, Constants.PLAYER_TOTAL_HEALTH, Constants.PLAYER_INVULNERABILITY_DURATION);
        this.state = EntityState.ACTIVE;
        this.vx = Constants.PLAYER_VELOCITY_X;
        this.vy = Constants.PLAYER_VELOCITY_Y;
        this.nextShot = System.currentTimeMillis();
        this.color = Constants.PLAYER_COLOR;
        this.projectileRadius = Constants.DEFAULT_ENEMY_PROJECTILE_RADIUS;
        this.projectileColor = Constants.PLAYER_PROJECTILE_COLOR;
        this.projectileSpeed = Constants.PLAYER_PROJECTILE_SPEED;
        this.shootingCallback = shootingCallback;
        this.size = radius * 2;
        this.bonusEffect = new BonusEffect(0, 0, BonusType.NONE);
    }

    @Override
    public void update(long delta) {
        long currentTime = System.currentTimeMillis();

        this.checkExploded(currentTime);
        this.checkBonus(currentTime);
        this.checkTakingDamage(currentTime);
        this.checkBounds();

        if (this.state == EntityState.ACTIVE || this.state == EntityState.TAKING_DAMAGE) {
            this.updateActiveEntity(delta, currentTime);
        }
    }

    @Override
    protected void updateActiveEntity(long delta, long currentTime) {
        updatePositionByKeyPress(delta);
        checkBounds();
        shootIfNeeded(currentTime);
    }

    @Override
    public void render() {
        if (this.state != EntityState.EXPLODING && this.state != EntityState.DESTROYED) {
            this.renderHealthBar();
        }
        super.render();
    }


    @Override
    protected void renderActiveEntity() {
        if (this.bonusEffect.getType() == BonusType.NONE) {
            GameLib.setColor(color);
            GameLib.drawPlayer(this.x, this.y, this.radius);
        } else {
            GameLib.setColor(color);
            GameLib.drawPlayer(this.x, this.y, this.radius);
            GameLib.setColor(Color.YELLOW);
            GameLib.drawPlayer(this.x, this.y, (this.size + 5)/2);
        }
    }

    @Override
    public void renderTakingDamage() {
        GameLib.setColor(Color.LIGHT_GRAY);
        GameLib.fillPlayer(this.x, this.y, this.size/2);
    }

    protected void renderHealthBar() {
        GameLib.drawPlayerHealthBar(currentHealth, totalHealth);
    }

    @Override
    protected void attemptToRenderExplosion(long currentTime) {
        if (this.state == EntityState.EXPLODING) {
            if (currentTime < this.explosionEnd) {
                this.renderExplosion(currentTime);
            }
        }
    }

    @Override
    protected void checkBounds() {
        this.x = Math.max(this.size, Math.min(Constants.GAME_WIDTH - this.size, this.x));
        this.y = Math.max(this.size, Math.min(Constants.GAME_HEIGHT - this.size, this.y));
    }
    protected void checkExploded(long currentTime) {
        if (this.state == EntityState.EXPLODING) {
            if (currentTime > this.explosionEnd) {
                if (isAlive()) {
                    this.state = EntityState.ACTIVE;
                } else {
                    this.state = EntityState.DESTROYED;
                }
            }
        }
    }

    protected void checkBonus(long currentTime) {
        switch (this.bonusEffect.getType()) {
            case NONE:
                break;
            case INVULNERABLE:
                if (this.bonusEffect.hasFinished(currentTime)) {
                    this.invulnerable = false;
                    this.bonusEffect = new BonusEffect(0, 0, BonusType.NONE);
                } else {
                    this.invulnerable = true;
                }
        }
    }

    void updatePositionByKeyPress(long delta) {
        if(GameLib.iskeyPressed(GameLib.KEY_UP) || GameLib.iskeyPressed(GameLib.KEY_W)) this.y -= delta * this.vy;
        if(GameLib.iskeyPressed(GameLib.KEY_DOWN) || GameLib.iskeyPressed(GameLib.KEY_S)) this.y += delta * this.vy;
        if(GameLib.iskeyPressed(GameLib.KEY_LEFT) || GameLib.iskeyPressed(GameLib.KEY_A)) this.x -= delta * this.vx;
        if(GameLib.iskeyPressed(GameLib.KEY_RIGHT) || GameLib.iskeyPressed(GameLib.KEY_D)) this.x += delta * this.vx;
    }

    private void shootIfNeeded(long currentTime) {
        if(GameLib.iskeyPressed(GameLib.KEY_CONTROL) &&
                currentTime > this.nextShot &&
                this.state != EntityState.TAKING_DAMAGE
        ) {
            if (shootingCallback != null) {
                double x = this.x;
                double y = this.y - 2 * this.radius;
                double vx = 0.0;
                double vy = this.projectileSpeed;
                this.shootingCallback.shoot(x, y, vx, vy, this.projectileRadius, this.projectileColor, this.getClass());
                this.nextShot = currentTime + 100;
            }
        }
    }

    public void setBonus(BonusEffect bonusEffect) {
        this.bonusEffect = bonusEffect;
        this.bonusEffect.resetInitialTime();
    }
}

