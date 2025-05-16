package game_items.entities;

import core.GameLib;
import enums.EntityState;
import game_items.Entity;

import java.awt.Color;

public abstract class CollidableEntity extends Entity {
    protected double explosionStart = 0.0;
    protected double explosionEnd = 0.0;
    protected double explosionDuration;
    protected int totalHealth;
    protected int currentHealth;
    protected long lastDamage;
    protected boolean invulnerable = false;
    protected long invulnerabilityDuration;

    public CollidableEntity(double x, double y, double radius, Color color, EntityState status, double explosionDuration, int totalHealth, long invulnerabilityDuration) {
        super(x, y, radius, color, status);
        this.explosionDuration = explosionDuration;
        this.totalHealth = totalHealth;
        this.currentHealth = totalHealth;
        this.invulnerabilityDuration = invulnerabilityDuration;
    }

    @Override
    public void update(long delta) {
        long currentTime = System.currentTimeMillis();

        this.checkTakingDamage(currentTime);
        this.checkExploded(currentTime);
        this.checkBounds();

        if (this.state == EntityState.ACTIVE) {
            this.updateActiveEntity(delta, currentTime);
        }
    }

    @Override
    public void render() {
        long currentTime = System.currentTimeMillis();

        this.attemptToRenderExplosion(currentTime);

        if (this.state == EntityState.ACTIVE) {
            this.renderActiveEntity();
        }

        if (this.state == EntityState.TAKING_DAMAGE) {
            this.renderTakingDamage();
        }
    }

    public void explode(long currentTime) {
        this.state = EntityState.EXPLODING;
        this.explosionStart = currentTime;
        this.explosionEnd = this.explosionStart + this.explosionDuration;
    }

    public void takeDamage(long currentTime) {
        if (!invulnerable) {
            this.lastDamage = currentTime;
            this.currentHealth--;
            this.state = EntityState.TAKING_DAMAGE;
            this.invulnerable = true;
        }
    }

    protected void attemptToRenderExplosion(long currentTime) {
        if (this.state == EntityState.EXPLODING && currentTime < this.explosionEnd) {
            this.renderExplosion(currentTime);
        }
    }

    public abstract void renderTakingDamage();

    protected void renderExplosion(long currentTime) {
        double alpha = (currentTime - this.explosionStart) / (this.explosionEnd - this.explosionStart);
        GameLib.drawExplosion(x, y, alpha);
    }

    protected void checkExploded(long currentTime) {
        if (this.state == EntityState.EXPLODING) {
            if (currentTime > this.explosionEnd) {
                this.state = EntityState.INACTIVE;
            }
        }
    }

    protected void checkTakingDamage(long currentTime) {
        if (this.state == EntityState.TAKING_DAMAGE) {
            if (this.currentHealth <= 0) {
                this.explode(currentTime);
            } else if (currentTime > (lastDamage + invulnerabilityDuration)) {
                    this.state = EntityState.ACTIVE;
                    this.invulnerable = false;
                }
        }
    }

    protected boolean isAlive() {
        return this.currentHealth > 0;
    }
}
