package comMain.GvozdevDanii.game;

import comMain.GvozdevDanii.IO.Input;
import comMain.GvozdevDanii.display.Display;
import comMain.GvozdevDanii.game.level.Level;
import comMain.GvozdevDanii.graphics.TextureAtlas;
import comMain.GvozdevDanii.utils.Time;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game implements Runnable {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Tanks";
    public static final int CLEAR_COLOR = 0xff000000;
    public static final int NUM_BUFFERS = 3;

    public static final float UPDATE_RATE = 60.0f;
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long IDLE_TIME = 1;

    public static final String ATLAS_FILE_NAME = "texture_atlas.png";

    private boolean running;
    private Thread gameThread;
    private final Graphics2D graphics;
    private final Input input;
    private final TextureAtlas atlas;
    private final Player player;
    private final Enemy enemy;

    public static ArrayList<Bullet> bullets;
    public static ArrayList<Bullet> bulletsEnemy;
    private final Level level;

    private static boolean flagEnemyDead = false;
    private static boolean flagPlayerDead = false;

    public Game() {
        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics = Display.getGraphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        player = new Player(0, 0, 2, 3, atlas);
        enemy = new Enemy(300, 300, 2, 3, atlas);

        level = new Level(atlas);

        bullets = new ArrayList<Bullet>();

        bulletsEnemy = new ArrayList<Bullet>();


    }

    public synchronized void start() {

        if (running)
            return;

        running = true;
        gameThread = new Thread(this);
        gameThread.start();

    }

    public synchronized void stop() {

        if (!running)
            return;

        running = false;

        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cleanUp();

    }

    private void update() {
        player.update(input);
        for (int i = 0; i < bullets.size(); i++) {
            boolean remove = bullets.get(i).update();
            if (remove) {
                bullets.remove(i);
                i--;
            }
        }

        if (input.getKey(KeyEvent.VK_ENTER)) {
            cleanUp();
        }

    }

    private void updateForEnemy() {
        enemy.update(input);
        for (int i = 0; i < bulletsEnemy.size(); i++) {
            boolean remove = bulletsEnemy.get(i).update();
            if (remove) {
                bulletsEnemy.remove(i);
                i--;
            }
        }

        if (input.getKey(KeyEvent.VK_ENTER)) {
            cleanUp();
        }

    }


    private void render() {
        Display.clear();
        level.render(graphics);
        if (!flagEnemyDead) {
            enemy.render(graphics);
            for (Bullet bullet : bulletsEnemy) {
                if (bullet.getX() - player.x <= 31 && bullet.getX() - player.x >= 0 && bullet.getY() - player.y <= 31 && bullet.getY() - player.y >= 0) {
                    flagPlayerDead = true;
                }
                bullet.draw(graphics);
            }

        } else enemy.unRender(graphics);
        if (!flagPlayerDead) {
            player.render(graphics);
            for (Bullet bullet : bullets) {
                if (bullet.getX() - enemy.x <= 31 && bullet.getX() - enemy.x >= 0 && bullet.getY() - enemy.y <= 31 && bullet.getY() - enemy.y >= 0) {
                    flagEnemyDead = true;
                }
                bullet.draw(graphics);
            }
        } else player.unRender(graphics);
        level.renderGrass(graphics);
        if (flagEnemyDead) {
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Century Gothic", Font.PLAIN, 18));
            String s = "You win!";
            int length = (int) graphics.getFontMetrics().getStringBounds(s, graphics).getWidth();
            graphics.drawString(s, enemy.x, enemy.y);
        } else if (flagPlayerDead) {
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Century Gothic", Font.PLAIN, 18));
            String s = "You lose!";
            int length = (int) graphics.getFontMetrics().getStringBounds(s, graphics).getWidth();
            graphics.drawString(s, player.x, player.y);

        }


        Display.swapBuffers();


    }


    public void run() {

        int fps = 0;
        int upd = 0;
        int updl = 0;

        long count = 0;

        float delta = 0;

        long lastTime = Time.get();
        while (running) {
            long now = Time.get();
            long elapsedTime = now - lastTime;
            lastTime = now;
            count += elapsedTime;

            boolean render = false;
            delta += (elapsedTime / UPDATE_INTERVAL);
            while (delta > 1) {
                if (!flagEnemyDead) {
                    updateForEnemy();
                }
                if (!flagPlayerDead){
                    update();
                }

                upd++;
                delta--;
                if (render) {
                    updl++;
                } else {
                    render = true;
                }
            }

            if (render) {
                render();
                fps++;
            } else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (count >= Time.SECOND) {
                Display.setTitle(TITLE + " || Fps: " + fps + " | Upd: " + upd + " | Updl: " + updl);
                upd = 0;
                fps = 0;
                updl = 0;
                count = 0;
            }

        }

    }

    private void cleanUp() {
        Display.destroy();
    }

}
