package core;

import config.Constants;
import game_items.entities.BonusItem;
import game_items.entities.bonus_items.BonusItemShield;

import java.util.ArrayList;
import java.util.List;

public class BonusItemsManager {
    private long nextBonusItemInterval;

    public BonusItemsManager() {
        this.nextBonusItemInterval = System.currentTimeMillis() + Constants.BONUS_ITEM_SHIELD_SPAWN_INTERVAL;
    }

    public List<BonusItem> getBonusItemsToSpawn(long currentTime) {
        List<BonusItem> bonusItemsToSpawn = new ArrayList<>();

        if (currentTime > nextBonusItemInterval){
            bonusItemsToSpawn.add(getBonusItemShield());
            this.nextBonusItemInterval = currentTime + Constants.BONUS_ITEM_SHIELD_SPAWN_INTERVAL;
        }

        return bonusItemsToSpawn;
    }

    private BonusItem getBonusItemShield() {
        return new BonusItemShield(
                Math.random() * (Constants.GAME_WIDTH - 20.0) + 10.0,
                Math.random() * (Constants.GAME_HEIGHT - 20.0) + 10.0,
                12,
                Constants.BONUS_ITEM_SHIELD_COLOR
        );
    }
}
