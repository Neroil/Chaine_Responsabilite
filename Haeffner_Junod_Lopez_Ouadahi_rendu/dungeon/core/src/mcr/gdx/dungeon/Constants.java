package mcr.gdx.dungeon;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * The Constants class contains all the constants used in the game.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class Constants {
    static final BitmapFont font = new BitmapFont();
    public static final int TILE_SIZE = 16;
    public static final int BASE_Y_RESOLUTION = 480;
    public static final int BASE_X_RESOLUTION = 800;
    public static final int BASE_Y_RENDER_RES = 240;
    public static final int BASE_X_RENDER_RES = 320;
    public static final int TARGET_FPS = 60;
    public static final int NUM_ROOMS = 10;
    public static final int MIN_ROOM_WIDTH = 5;
    public static final int MIN_ROOM_HEIGHT = 5;
    public static final int MAX_ROOM_WIDTH = 15;
    public static final int MAX_ROOM_HEIGHT = 15;
    public static final int MAP_SIZE = 50;
    public static final int NUM_ENEMIES = 5;
    public static final int NUM_ITEMS = 10;
    public static final double VIGOR_RING_MODIFIER = 0.5;
    public static final double MANA_RING_MODIFIER = 0.5;
    public static final int RESSOURCE_REGEN_TIMEOUT = 20;
}
