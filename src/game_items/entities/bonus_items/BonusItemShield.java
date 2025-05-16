package game_items.entities.bonus_items;

import config.Constants;
import core.GameLib;
import enums.BonusType;
import models.BonusEffect;
import game_items.entities.BonusItem;

import java.awt.Color;

public class BonusItemShield extends BonusItem {
    public BonusItemShield(double x, double y, double radius, Color color) {
        super(x, y, radius, color, new BonusEffect(
                System.currentTimeMillis(),
                Constants.BONUS_ITEM_SHIELD_DURATION,
                BonusType.INVULNERABLE
        ));
    }

    @Override
    protected void renderActiveEntity() {
        GameLib.drawShieldItem(x, y,radius*2);
    }
}
