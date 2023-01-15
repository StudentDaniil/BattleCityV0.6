package comMain.GvozdevDanii.game;

import comMain.GvozdevDanii.IO.Input;
import comMain.GvozdevDanii.graphics.Sprite;
import comMain.GvozdevDanii.graphics.SpriteSheet;
import comMain.GvozdevDanii.graphics.TextureAtlas;
import comMain.GvozdevDanii.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Enemy extends Entity {
    public static final int SPRITE_SCALE = 16;
    public static final int SPRITES_PER_HEADING = 1;

    private boolean firing;
    private long firingTimer;
    private long firingDelay;


    private enum Heading {
        UP(8 * SPRITE_SCALE, 0, SPRITE_SCALE, SPRITE_SCALE),
        RIGHT(14 * SPRITE_SCALE, 0, SPRITE_SCALE, SPRITE_SCALE),
        DOWN(12 * SPRITE_SCALE, 0, SPRITE_SCALE, SPRITE_SCALE),
        LEFT(10 * SPRITE_SCALE, 0, SPRITE_SCALE, SPRITE_SCALE),

        EMPTY(22 * SPRITE_SCALE, 0, SPRITE_SCALE, SPRITE_SCALE);

        private final int x;
        private final int y;
        private final int h;
        private final int w;

        Heading(int x, int y, int h, int w) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        private BufferedImage texture(TextureAtlas atlas) {
            return atlas.cut(x, y, w, h);
        }
    }

    private Enemy.Heading heading;
    private final Map<Enemy.Heading, Sprite> spriteMap;
    private final float scale;
    private final float speed;

    public Enemy(float x, float y, float scale, float speed, TextureAtlas atlas) {
        super(EntityType.Enemy, x, y);
        firing = false;
        firingTimer = System.nanoTime();
        firingDelay = 200;

        heading = Enemy.Heading.UP;
        spriteMap = new HashMap<Enemy.Heading, Sprite>();
        this.scale = scale;
        this.speed = speed;

        for (Enemy.Heading h : Enemy.Heading.values()) {
            SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
            Sprite sprite = new Sprite(sheet, scale);
            spriteMap.put(h, sprite);
        }

    }

    public void setFiring(boolean b) {
        firing = b;
    }

    @Override
    public void update(Input input) {

        float newX = x;
        float newY = y;

        if (Utils.getRandomBoolean()) {
            Random random = new Random();
            newY -= speed;
            heading = Enemy.Heading.UP;
        } else if (Utils.getRandomBoolean()) {
            Random random = new Random();
            newX += speed;
            heading = Enemy.Heading.RIGHT;
        } if (Utils.getRandomBoolean()) {
            Random random = new Random();
            newY += speed;
            heading = Enemy.Heading.DOWN;
        } else if (Utils.getRandomBoolean()) {
            Random random = new Random();
            newX -= speed;
            heading = Enemy.Heading.LEFT;
        }


        if (Utils.getRandomBoolean()) {
            setFiring(true);
        }
        if (firing) {
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if (elapsed > firingDelay) {
                if (heading == Enemy.Heading.UP) {
                    Game.bulletsEnemy.add(new Bullet(270, (int) (newX + 15), (int) newY));
                    firingTimer = System.nanoTime();
                } else if (heading == Enemy.Heading.DOWN) {
                    Game.bulletsEnemy.add(new Bullet(90, (int) (newX + 15), (int) (newY + 30)));
                    firingTimer = System.nanoTime();
                } else if (heading == Enemy.Heading.LEFT) {
                    Game.bulletsEnemy.add(new Bullet(180, (int) (newX), (int) (newY + 15)));
                    firingTimer = System.nanoTime();
                } else if (heading == Enemy.Heading.RIGHT) {
                    Game.bulletsEnemy.add(new Bullet(0, (int) (newX + 30), (int) (newY + 15)));
                    firingTimer = System.nanoTime();

                }
            }
        }
        setFiring(false);


        if (newX < 0) {
            newX = 0;
        } else if (newX >= Game.WIDTH - SPRITE_SCALE * scale) {
            newX = Game.WIDTH - SPRITE_SCALE * scale;
        }

        if (newY < 0) {
            newY = 0;
        } else if (newY >= Game.HEIGHT - SPRITE_SCALE * scale) {
            newY = Game.HEIGHT - SPRITE_SCALE * scale;
        }

        x = newX;
        y = newY;

    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    @Override
    public void render(Graphics2D g) {
        spriteMap.get(heading).render(g, x, y);
    }
    public void unRender(Graphics2D g){
        spriteMap.get(Heading.EMPTY).render(g, x, y);
    }

}
