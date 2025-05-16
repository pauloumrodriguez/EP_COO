package config;

import core.GameLib;

import java.awt.Color;

public class Constants {
    // Constantes relacionadas ao jogo
    public static final int GAME_WIDTH = GameLib.WIDTH;
    public static final int GAME_HEIGHT = GameLib.HEIGHT;

    // Constantes relacionadas ao jogador
    public static final double PLAYER_START_X = GAME_WIDTH / 2;
    public static final double PLAYER_START_Y = GAME_WIDTH * 0.9;
    public static final Color PLAYER_COLOR = Color.BLUE;
    public static final double PLAYER_RADIUS = 12.0;
    public static final double PLAYER_VELOCITY_X = 0.25;
    public static final double PLAYER_VELOCITY_Y = 0.25;
    public static final double PLAYER_EXPLOSION_DURATION = 1500;
    public static final Color PLAYER_PROJECTILE_COLOR = Color.GREEN;
    public static final double PLAYER_PROJECTILE_SPEED = -1;
    public static final int PLAYER_TOTAL_HEALTH = 5;
    public static final long PLAYER_INVULNERABILITY_DURATION = 1000;


    // Constantes relacionadas ao fundo mais próximo
    public static final int NUM_FOREGROUND_STARS = 20;
    public static final Color COLOR_FOREGROUND_STARS = Color.GRAY;
    public static final double SPEED_FOREGROUND_STARS = 0.070;

    // Constantes relacionadas ao fundo mais distante
    public static final int NUM_BACKGROUND_STARS = 50;
    public static final Color COLOR_BACKGROUND_STARS = Color.DARK_GRAY;
    public static final double SPEED_BACKGROUND_STARS = 0.045;

    // Constantes relacionadas às estrelas
    public static final int STAR_WIDTH = 3;
    public static final int STAR_HEIGHT = 3;

    // Constantes relacionadas ao inimigo padrão
    public static final double DEFAULT_ENEMY_EXPLOSION_DURATION = 500;
    public static final int DEFAULT_ENEMY_TOTAL_HEALTH = 1;
    public static final double DEFAULT_ENEMY_ANGLE = (3 * Math.PI) / 2;
    public static final double DEFAULT_ENEMY_RADIUS = 9.0;
    public static final int DEFAULT_ENEMY_SPAWN_INTERVAL = 2000;
    public static final int DEFAULT_ENEMY_PROJECTILE_RADIUS = 5;
    public static final Color DEFAULT_ENEMY_COLOR = Color.CYAN;
    public static final Color DEFAULT_ENEMY_PROJECTILE_COLOR = Color.RED;
    public static final long DEFAULT_ENEMY_INVULNERABILITY_DURATION = 0;


    // Constantes relacionadas ao inimigo forte
    public static final double STRONGER_ENEMY_EXPLOSION_DURATION = 500;
    public static final int STRONGER_ENEMY_TOTAL_HEALTH = 1;
    public static final double STRONGER_ENEMY_ANGLE = (3 * Math.PI) / 2;
    public static final double STRONGER_ENEMY_RADIUS = 9.0;
    public static final int STRONGER_ENEMY_SPAWN_INTERVAL = 7000;
    public static final int STRONGER_ENEMY_PROJECTILE_RADIUS = 5;
    public static final Color STRONGER_ENEMY_COLOR = Color.MAGENTA;
    public static final Color STRONGER_ENEMY_PROJECTILE_COLOR = Color.RED;
    public static final long STRONGER_ENEMY_INVULNERABILITY_DURATION = 0;


    // Constantes relacionadas ao inimigo mais poderoso
    public static final double BOSS_ENEMY_EXPLOSION_DURATION = 2000;
    public static final int BOSS_ENEMY_TOTAL_HEALTH = 30;
    public static final double BOSS_ENEMY_ANGLE = (3 * Math.PI) / 2;
    public static final double BOSS_ENEMY_RADIUS = 100.0;
    public static final int BOSS_ENEMY_SPAWN_INTERVAL = 17500;
    public static final int BOSS_ENEMY_PROJECTILE_RADIUS = 10;
    public static final Color BOSS_ENEMY_COLOR = Color.WHITE;
    public static final Color BOSS_ENEMY_PROJECTILE_COLOR = Color.RED;
    public static final long BOSS_ENEMY_INVULNERABILITY_DURATION = 50;

    // Constantes relacionadas ao escudo bonus
    public static final int BONUS_ITEM_SHIELD_DURATION = 5000;
    public static final int BONUS_ITEM_SHIELD_SPAWN_INTERVAL = 10000;
    public static final Color BONUS_ITEM_SHIELD_COLOR = Color.WHITE;

}
