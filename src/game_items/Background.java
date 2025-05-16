package game_items;

import config.Constants;
import game_items.entities.BackgroundStar;
import interfaces.GameItem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class Background implements GameItem {
    private List<BackgroundStar> stars;
    private Color starsColor;
    private double starsSpeed;
    private int starsWidth;
    private int starsHeight;
    private int starsCount;

    public Background(int starsCount, Color starsColor, int starsWidth, int starsHeight, double starsSpeed) {
        this.stars = new ArrayList<>();
        this.starsColor = starsColor;
        this.starsWidth = starsWidth;
        this.starsHeight = starsHeight;
        this.starsSpeed = starsSpeed;
        this.starsCount = starsCount;
        this.initializeStars();
    }

    private void initializeStars() {
            for (int i = 0; i < this.starsCount; i++) {
                double x = Math.random() * Constants.GAME_WIDTH;
                double y = Math.random() * Constants.GAME_HEIGHT;
                double radius = Math.sqrt(this.starsWidth * this.starsWidth + this.starsHeight * this.starsHeight) / 2;
                this.stars.add(new BackgroundStar(x, y, radius, this.starsColor, this.starsSpeed, this.starsWidth, this.starsHeight));
            }
    }

    @Override
    public void update(long delta) {
        for (BackgroundStar star : stars) {
            star.update(delta);
        }
    }

    @Override
    public void render() {
        for (BackgroundStar star : stars) {
            star.render();
        }
    }
}
