package game_items.entities;

import config.Constants;
import enums.EntityState;
import models.BonusEffect;
import game_items.Entity;

import java.awt.Color;

public abstract class BonusItem extends Entity {
    BonusEffect bonusEffect;

    public BonusItem(double x, double y, double radius, Color color, BonusEffect bonusEffect) {
        super(x, y, radius, color, EntityState.ACTIVE);
        this.bonusEffect = bonusEffect;
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
        if (currentTime > this.bonusEffect.getInitialTime() + this.bonusEffect.getDuration()) {
            this.state = EntityState.INACTIVE;
        }
    }

    public BonusEffect getBonus() {
        return bonusEffect;
    }
}
