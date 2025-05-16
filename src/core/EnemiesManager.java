package core;

import config.Constants;
import game_items.entities.collidable_entities.Enemy;
import game_items.entities.enemies.BossEnemy;
import game_items.entities.enemies.DefaultEnemy;
import game_items.entities.enemies.StrongerEnemy;
import interfaces.ShootingCallback;

import java.util.ArrayList;
import java.util.List;

public class EnemiesManager {
    private ShootingCallback shootingCallback;
    private long nextDefaultEnemySpawnInterval;
    private long nextStrongerEnemySpawnInterval;
    private long nextBossEnemySpawnInterval;
    private int strongerEnemiesCount = 0;
    private double strongerEnemyX = Constants.GAME_WIDTH * 0.20;

    public EnemiesManager(ShootingCallback shootingCallback) {
        this.shootingCallback = shootingCallback;

        long currentTime = System.currentTimeMillis();
        this.nextDefaultEnemySpawnInterval = currentTime + Constants.DEFAULT_ENEMY_SPAWN_INTERVAL;
        this.nextStrongerEnemySpawnInterval = currentTime + Constants.STRONGER_ENEMY_SPAWN_INTERVAL;
        this.nextBossEnemySpawnInterval = currentTime + Constants.BOSS_ENEMY_SPAWN_INTERVAL;
    }

    public List<Enemy> getEnemiesToSpawn(long currentTime) {
        List<Enemy> enemiesToSpawn = new ArrayList<>();

        if (currentTime > nextDefaultEnemySpawnInterval){
            enemiesToSpawn.add(getDefaultEnemy());
            this.nextDefaultEnemySpawnInterval = currentTime + 500;
        }

        if (currentTime > nextStrongerEnemySpawnInterval) {
            enemiesToSpawn.add(getStrongerEnemy());
            this.strongerEnemiesCount++;

            if (this.strongerEnemiesCount < 10) {
                this.nextStrongerEnemySpawnInterval = currentTime + 120;
            } else {
                this.strongerEnemiesCount = 0;
                this.strongerEnemyX = Math.random() > 0.5 ? Constants.GAME_WIDTH * 0.2 : Constants.GAME_WIDTH * 0.8;
                this.nextStrongerEnemySpawnInterval = (long) (currentTime + 3000 + Math.random() * 3000);
            }
        }

        if (currentTime > nextBossEnemySpawnInterval) {
            enemiesToSpawn.add(getBossEnemy());
            this.nextBossEnemySpawnInterval = currentTime + Constants.BOSS_ENEMY_SPAWN_INTERVAL;
            this.nextDefaultEnemySpawnInterval = currentTime + 4000;
            this.nextStrongerEnemySpawnInterval = currentTime + 4000;
        }

        return enemiesToSpawn;
    }

    private DefaultEnemy getDefaultEnemy() {
        return new DefaultEnemy(
                Math.random() * (Constants.GAME_WIDTH - 20.0) + 10.0,
                -10.0,
                0.20 + Math.random() * 0.15,
                0.0,
                0.45,
                this.shootingCallback
        );
    }

    private StrongerEnemy getStrongerEnemy() {
        return new StrongerEnemy(
                this.strongerEnemyX,
                -10.0,
                0.42,
                0.0,
                0.30,
                this.shootingCallback
        );
    }

    private BossEnemy getBossEnemy() {
        return new BossEnemy(
                0,
                Constants.GAME_HEIGHT * 0.2,
                0.5,
                0.0,
                1.0,
                this.shootingCallback
        );
    }
}
