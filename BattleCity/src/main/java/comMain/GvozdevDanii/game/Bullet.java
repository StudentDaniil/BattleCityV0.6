package comMain.GvozdevDanii.game;

import java.awt.*;

public class Bullet {
    private int x;
    private int y;
    private final int r;

    private final double dx;
    private final double dy;
    private final double rad;

    private final double speed;

    private final Color color1;

    public Bullet(double angle, int x, int y) {
        this.x = x;
        this.y = y;
        r = 2;



        rad = Math.toRadians(angle);

        speed = 8;

        dx = Math.cos(rad)*speed;
        dy = Math.sin(rad)*speed;


        color1 = Color.yellow;

    }

    public boolean update() {
        x += dx;
        y += dy;

        if (x < -r || x > Game.WIDTH + r ||
                y < -r || y > Game.HEIGHT + r) {
            return true;
        }

        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics2D g) {
        g.setColor(color1);

        g.fillOval((int) (x-r), (int) (y-r), 2 * r, 2 * r);

    }


}
