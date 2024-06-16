package mcr.gdx.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;

import static mcr.gdx.dungeon.Constants.font;

/**
 * The GameHUD class is responsible for rendering the game's heads-up display (HUD).
 * It displays the player's health, stamina, and mana bars, the player's current weapon, and the player's items.
 * It also displays the end game screen when the game is won or lost.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class GameHUD {
    private final ShapeRenderer shapeRenderer;
    private final TextureRegion defaultTexture;
    private PlayerTile player;
    SpriteBatch HUDbatch = new SpriteBatch();

    /**
     * Constructs a new GameHUD for the specified player.
     *
     * @param player the player to display the HUD for
     */
    public GameHUD(PlayerTile player) {
        shapeRenderer = new ShapeRenderer();
        this.player = player;
        defaultTexture = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 128, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    /**
     * Sets the player to display the HUD for.
     *
     * @param player the player to display the HUD for
     */
    public void setPlayer(PlayerTile player) {
        this.player = player;
    }

    /**
     * Renders the HUD.
     */
    public void render() {
        renderBars();
        renderWeapon();
        renderItems();
    }

    /**
     * Renders the player's health, stamina, and mana bars.
     */
    private void renderBars() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw health bar
        drawBar(Color.RED, 20, 20, player.getHP(), 20);

        // Draw stamina bar
        drawBar(Color.YELLOW, 20, 50, player.getVigor(), player.getVigorMax());

        // Draw mana bar
        drawBar(Color.BLUE, 20, 80, player.getMana(), player.getManaMax());

        shapeRenderer.end();
    }

    /**
     * Draws a bar at the specified position with the specified color, value, and maximum value.
     *
     * @param color the color of the bar
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     * @param value the current value of the bar
     * @param max the maximum value of the bar
     */
    private void drawBar(Color color, float x, float y, float value, float max) {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x - 1, y - 1, max + 2, 2);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, value, 20);
    }

    /**
     * Renders the player's equipped weapon.
     */
    private void renderWeapon() {
        HUDbatch.begin();
        TextureRegion textureRegion = player.getWeapon() != null ? player.getWeapon().getTexture() : defaultTexture;
        HUDbatch.draw(textureRegion, 720, 30, 0, 0, 32, 32, 1.2f, 1.2f, 0);
        HUDbatch.end();
    }

    /**
     * Renders the player's items.
     */
    private void renderItems() {
        HUDbatch.begin();
        float itemX = Constants.BASE_X_RESOLUTION - 50;
        float itemY = Constants.BASE_Y_RESOLUTION - 40;

        for (ItemTile item : player.getAttackItems()) {
            itemX = drawItemInHorizontalLine(item, itemX, itemY);
        }

        for (ItemTile item : player.getDefenseItems()) {
            itemX = drawItemInHorizontalLine(item, itemX, itemY);
        }

        HUDbatch.end();
    }

    /**
     * Draws an item at the specified position and returns the new x-coordinate for the next item.
     *
     * @param item the item to draw
     * @param itemX the x-coordinate of the position
     * @param itemY the y-coordinate of the position
     * @return the new x-coordinate for the next item
     */
    private float drawItemInHorizontalLine(ItemTile item, float itemX, float itemY) {
        HUDbatch.draw(item.getTexture(), itemX, itemY, 0, 0, 16, 16, 1.5f, 1.5f, 0);
        return itemX - 32;
    }

    /**
     * Renders the end game screen with the specified text. The end game screen is a black
     * screen with the text displayed in white in the center. It also prompts the user to restart the game
     * by pressing "R".
     *
     * @param text the text to display
     */
    private void renderEndTextScreen(String text) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        HUDbatch.begin();
        font.setColor(Color.WHITE);

        String text2 = "Press R to restart";
        font.getData().setScale(5);
        GlyphLayout layout = new GlyphLayout(font, text);
        font.getData().setScale(2);
        GlyphLayout layout2 = new GlyphLayout(font, text2);

        font.getData().setScale(5);
        font.draw(HUDbatch, text, (Constants.BASE_X_RESOLUTION - layout.width) / 2, (Constants.BASE_Y_RESOLUTION + layout.height) / 2);

        font.getData().setScale(2);
        font.draw(HUDbatch, text2, (Constants.BASE_X_RESOLUTION - layout2.width) / 2, (Constants.BASE_Y_RESOLUTION - layout.height - layout2.height) / 2);

        HUDbatch.end();
    }

    /**
     * Renders the win screen.
     */
    public void renderWinScreen() {
        renderEndTextScreen("You win!");
    }

    /**
     * Renders the loss screen.
     */
    public void renderLoseScreen() {
        renderEndTextScreen("You lose!");
    }

    /**
     * Disposes of the shape renderer.
     */
    public void dispose() {
        shapeRenderer.dispose();
    }
}