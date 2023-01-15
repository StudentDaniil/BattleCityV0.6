package comMain.GvozdevDanii.game;

import comMain.GvozdevDanii.IO.Input;
import comMain.GvozdevDanii.graphics.Sprite;
import comMain.GvozdevDanii.graphics.SpriteSheet;
import comMain.GvozdevDanii.graphics.TextureAtlas;
import comMain.GvozdevDanii.utils.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {

    public static final int SPRITE_SCALE = 16;
    public static final int SPRITES_PER_HEADING = 1;

    private boolean firing;
    private long firingTimer;
    private final long firingDelay;

    private final Integer[][] level = Utils.levelParser("res/lvl.txt");


    private enum Heading {
        UP(0, 0, SPRITE_SCALE, SPRITE_SCALE),
        RIGHT(6 * SPRITE_SCALE, 0, SPRITE_SCALE, SPRITE_SCALE),
        DOWN(4 * SPRITE_SCALE, 0, SPRITE_SCALE, SPRITE_SCALE),
        LEFT(2 * SPRITE_SCALE, 0, SPRITE_SCALE, SPRITE_SCALE),
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

    private Heading heading;
    private final Map<Heading, Sprite> spriteMap;
    private final float scale;
    private final float speed;


    public Player(float x, float y, float scale, float speed, TextureAtlas atlas) {
        super(EntityType.Player, x, y);
        firing = false;
        firingTimer = System.nanoTime();
        firingDelay = 200;

        heading = Heading.UP;
        spriteMap = new HashMap<Heading, Sprite>();
        this.scale = scale;
        this.speed = speed;

        for (Heading h : Heading.values()) {
            SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
            Sprite sprite = new Sprite(sheet, scale);
            spriteMap.put(h, sprite);
        }

    }

    public void setFiring(boolean b) {
        firing = b;
    }

    public boolean boolBlockPhysicsUp() {

        int xTile = 0;
        int yTile = 0;


        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                if (level[i][j] != 0 && level[i][j] != 4) {
                    xTile = j * 16;
                    yTile = i * 16;
                    if ((y - speed - yTile) <= 15 && (y - speed - yTile) >= 0 && (xTile - x) >= -15 && (xTile - x) <= 31) {
                        return false;
                    }
                }


            }
        }
        return true;


    }
    public boolean boolBlockPhysicsDown() {

        int xTile = 0;
        int yTile = 0;


        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                if (level[i][j] != 0 && level[i][j] != 4) {
                    xTile = j * 16;
                    yTile = i * 16;
                    if ((y + speed - yTile) >= -31 && (y + speed - yTile) <= 0 && (xTile - x) >= -15 && (xTile - x) <= 31) {
                        return false;
                    }
                }


            }
        }
        return true;


    }
    public boolean boolBlockPhysicsRight() {

        int xTile = 0;
        int yTile = 0;


        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                if (level[i][j] != 0 && level[i][j] != 4) {
                    xTile = j * 16;
                    yTile = i * 16;
                    if ((x + speed - xTile) >= -31 && (x + speed - xTile) <= 0 && (yTile - y) >= -15 && (yTile - y) <= 31) {
                        return false;
                    }
                }


            }
        }
        return true;


    }
    public boolean boolBlockPhysicsLeft() {

        int xTile = 0;
        int yTile = 0;


        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                if (level[i][j] != 0 && level[i][j] != 4) {
                    xTile = j * 16;
                    yTile = i * 16;
                    if ((x - speed - xTile) <= 15 && (x - speed - xTile) >= 0 && (yTile - y) >= -15 && (yTile - y) <= 31) {
                        return false;
                    }
                }


            }
        }
        return true;


    }

    @Override
    public void update(Input input) {

        float newX = x;
        float newY = y;


        if (input.getKey(KeyEvent.VK_UP)) {
            heading = Heading.UP;
            if (boolBlockPhysicsUp()){
                newY -= speed;
            }
        } else if (input.getKey(KeyEvent.VK_RIGHT)) {
            heading = Heading.RIGHT;
            if (boolBlockPhysicsRight()){
                newX += speed;
            }
        } else if (input.getKey(KeyEvent.VK_DOWN)) {
            heading = Heading.DOWN;
            if (boolBlockPhysicsDown()){
                newY += speed;
            }
        } else if (input.getKey(KeyEvent.VK_LEFT)) {
            heading = Heading.LEFT;
            if (boolBlockPhysicsLeft()){
                newX -= speed;
            }
        } else if (input.getKey(KeyEvent.VK_D)) {
            newX += speed;
            heading = Heading.RIGHT;
        } else if (input.getKey(KeyEvent.VK_S)) {
            newY += speed;
            heading = Heading.DOWN;
        } else if (input.getKey(KeyEvent.VK_A)) {
            newX -= speed;
            heading = Heading.LEFT;
        } else if (input.getKey(KeyEvent.VK_W)) {
            newY -= speed;
            heading = Heading.UP;
        }


        if (input.getKey(KeyEvent.VK_SPACE)) {
            setFiring(true);
        }
        if (firing) {
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if (elapsed > firingDelay) {
                if (heading == Heading.UP) {
                    Game.bullets.add(new Bullet(270, (int) (newX + 15), (int) newY));
                    firingTimer = System.nanoTime();
                } else if (heading == Heading.DOWN) {
                    Game.bullets.add(new Bullet(90, (int) (newX + 15), (int) (newY + 30)));
                    firingTimer = System.nanoTime();
                } else if (heading == Heading.LEFT) {
                    Game.bullets.add(new Bullet(180, (int) (newX), (int) (newY + 15)));
                    firingTimer = System.nanoTime();
                } else if (heading == Heading.RIGHT) {
                    Game.bullets.add(new Bullet(0, (int) (newX + 30), (int) (newY + 15)));
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

    public float getX() {

        return x;
    }

    public float getY() {

        return y;
    }

    @Override
    public void render(Graphics2D g) {
        spriteMap.get(heading).render(g, x, y);
    }

    public void unRender(Graphics2D g) {
        spriteMap.get(Player.Heading.EMPTY).render(g, x, y);
    }


}
