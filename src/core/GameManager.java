package core;

import config.Constants;
import game_items.Background;
import game_items.Entity;
import game_items.Timer;
import game_items.entities.*;
import game_items.entities.collidable_entities.Enemy;
import game_items.entities.collidable_entities.Player;
import interfaces.GameItem;
import interfaces.ShootingCallback;
import enums.EntityState;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameManager implements ShootingCallback {
    private List<GameItem> gameItems = new ArrayList<>();
    private EnemiesManager enemiesManager;
    private BonusItemsManager bonusItemsManager;
    private long currentTime = System.currentTimeMillis();
    private long delta = 0;
    private boolean shouldSpawnEntities = true;
    private boolean ended = false;

    public GameManager() {
        startGame();
    }

    public void startGame() {
        init();
        GameLib.initGraphics();
        gameLoop();
    }

    public void init() {
        delta = calculateDelta();
        enemiesManager = new EnemiesManager(this);
        bonusItemsManager = new BonusItemsManager();
        initializeBackground();
        initializePlayer();
        initializeTimer();
        ended = false;
        shouldSpawnEntities = true;
    }

    private void reset() {
        gameItems.clear();
        init();
    }

    private void gameLoop() {
        while (true) {
            delta = calculateDelta();

            checkCollisions();
            updateGameItems();
            checkGameEndConditions();
            readUserInput();
            spawnEntities();
            renderGameItems();
            removeInactiveItems();
            busyWait(currentTime + 5);
        }
    }

    private void initializeBackground() {
        Background foreground = new Background(
                Constants.NUM_FOREGROUND_STARS,
                Constants.COLOR_FOREGROUND_STARS,
                Constants.STAR_WIDTH,
                Constants.STAR_HEIGHT,
                Constants.SPEED_FOREGROUND_STARS
        );
        Background background = new Background(
                Constants.NUM_BACKGROUND_STARS,
                Constants.COLOR_BACKGROUND_STARS,
                Constants.STAR_WIDTH,
                Constants.STAR_HEIGHT,
                Constants.SPEED_BACKGROUND_STARS
        );
        gameItems.add(foreground);
        gameItems.add(background);
    }

    private void initializeTimer() {
        Timer timer = new Timer();
        gameItems.add(timer);
    }

    private void initializePlayer() {
        Player player = new Player(this);
        gameItems.add(player);
    }

    private void readUserInput() {
        if(ended && GameLib.iskeyPressed(GameLib.KEY_ENTER)) {
            reset();
        }
    }

    private boolean checkCollision(Entity a, Entity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        return dist <= (a.getRadius() + b.getRadius()) * 0.8;
    }

    private long calculateDelta() {
        long delta = System.currentTimeMillis() - currentTime;
        this.currentTime = System.currentTimeMillis();
        return delta;
    }

    private void updateGameItems() {
        for (int i = 0; i < gameItems.size(); i++) {
            gameItems.get(i).update(delta);
        }
    }
    private void renderGameItems() {
        for (GameItem item : gameItems) {
            item.render();
        }
        GameLib.display();
    }
    private void checkCollisions() {
        for (GameItem itemA : gameItems) {
            for (GameItem itemB : gameItems) {
                // Evita colisão de um objeto consigo mesmo
                if (itemA == itemB) {
                    continue;
                }

                // Verifica se ambos são do tipo Entity
                if (!(itemA instanceof Entity) || !(itemB instanceof Entity)) {
                    continue;
                }

                Entity entityA = (Entity) itemA;
                Entity entityB = (Entity) itemB;

                // Verifica se ambos estão ativos
                if (entityA.getState() != EntityState.ACTIVE || entityB.getState() != EntityState.ACTIVE) {
                    continue;
                }


                // Verifica colisões de projéteis com inimigos
                if (entityA instanceof Projectile && (entityB instanceof Enemy)) {
                    if (checkCollision(entityA, entityB)) {
                        // Evita colisão de projéteis inimigos com inimigos
                        if (Enemy.class.isAssignableFrom(((Projectile) entityA).getOriginEntityType())) return;

                        entityA.setState(EntityState.INACTIVE); // Marca o projétil como inativo
                        ((Enemy) entityB).takeDamage(currentTime);
                    }
                }

                // Verifica colisões de projéteis com o jogador
                else if (entityA instanceof Projectile && entityB instanceof Player) {
                    if (checkCollision(entityA, entityB)) {
                        entityA.setState(EntityState.INACTIVE); // Marca o projétil como inativo
                        ((Player) entityB).takeDamage(currentTime);
                    }
                }

                // Verifica colisões de inimigos com o jogador
                else if (entityA instanceof Player && entityB instanceof Enemy) {
                    if (checkCollision(entityA, entityB)) {
                        ((Player) entityA).takeDamage(currentTime);
                    }
                }

                // Verifica colisões do jogador com efeito bonus
                else if (entityA instanceof Player && entityB instanceof BonusItem) {
                    if (checkCollision(entityA, entityB)) {
                        ((Player) entityA).setBonus(((BonusItem)entityB).getBonus());
                        entityB.setState(EntityState.INACTIVE);
                    };
                }
            }
        }
    }

    public void endGame(boolean playerWon) {
        ended = true;

        if (playerWon) {
            showVictory();
        } else {
            showGameOver();
        }
    }

    private void checkGameEndConditions() {
        Optional<Player> optionalPlayer = gameItems.stream()
                .filter(item -> item instanceof Player)
                .map(item -> (Player) item)
                .findFirst();

        optionalPlayer.ifPresent(player -> {
            EntityState playerState = player.getState();

            switch (playerState) {
                case EXPLODING:
                    shouldSpawnEntities = false;
                    explodeAll();
                    break;
                case DESTROYED:
                    endGame(false);
                    break;
                default:
                    break;
            }
        });
    }

    private void explodeAll() {
        List<GameItem> markedItemsToRemove = new ArrayList<>();

        for (GameItem item : gameItems) {
            if (item instanceof CollidableEntity && !(item instanceof Player)) {
                CollidableEntity entity = (CollidableEntity) item;
                entity.setState(EntityState.EXPLODING);
            } else if (!(item instanceof Player) && !(item instanceof Background)) {
                markedItemsToRemove.add(item);
            }
        }

        for (GameItem item : markedItemsToRemove) {
            gameItems.remove(item);
        }
    }

    private void spawnEntities() {
        if (shouldSpawnEntities) {
            spawnEnemies();
            spawnBonusItems();
        }
    }
    private void spawnEnemies() {
        List<Enemy> enemiesToSpawn = enemiesManager.getEnemiesToSpawn(currentTime);
        gameItems.addAll(enemiesToSpawn);
    }

    private void spawnBonusItems() {
        List<BonusItem> bonusItemsToSpawn = bonusItemsManager.getBonusItemsToSpawn(currentTime);
        gameItems.addAll(bonusItemsToSpawn);
    }

    private void removeInactiveItems() {
        gameItems.removeIf(item -> (item instanceof Entity) && ((Entity)item).getState() == EntityState.INACTIVE);
    }
    private void showGameOver() {
        GameLib.drawGameOver();
    }

    private void showVictory() {
        System.out.println("Parabéns! Você venceu!");
    }

    public static void busyWait(long time){
        while(System.currentTimeMillis() < time) Thread.yield();
    }

    @Override
    public void shoot(double x, double y, double vx, double vy, double radius, Color color, Class<? extends Entity> entityType) {
        Projectile projectile = new Projectile(x, y, vx, vy, radius, color, entityType);
        gameItems.add(projectile);
    }
}

