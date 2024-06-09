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


public class GameHUD {
    private final ShapeRenderer shapeRenderer;
    private final TextureRegion defaultTexture;
    private PlayerTile player;
    SpriteBatch HUDbatch = new SpriteBatch();

    public GameHUD(PlayerTile player) {
        shapeRenderer = new ShapeRenderer();
        this.player = player;
        defaultTexture = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 128, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public void setPlayer(PlayerTile player) {
        this.player = player;
    }

    public void render() {

        // Draw health bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(19, 19, 10 + 2, 2); // Draw larger black rectangle
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(20, 20, player.getHP(), 20);
        shapeRenderer.end();

        // Draw stamina bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(19, 49, player.getVigorMax() + 2, 2); // Draw larger black rectangle
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(20, 50, player.getVigor(), 20);
        shapeRenderer.end();

        // Draw mana bar with black border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(19, 79, player.getManaMax() + 2, 2); // Draw larger black rectangle
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(20, 80, player.getMana(), 20);
        shapeRenderer.end();

        HUDbatch.begin();
        TextureRegion textureRegion;
        if (player.getWeapon() != null) {
            textureRegion = player.getWeapon().getTexture();
        } else {
            textureRegion = defaultTexture;
        }
        HUDbatch.draw(textureRegion, 720, 30, 0, 0, 32, 32, 1.2f, 1.2f, 0); // Scaling by 0.5

        float itemX = Constants.BASE_X_RESOLUTION - 50; // Adjust this value as needed
        float itemY = Constants.BASE_Y_RESOLUTION - 40;


        for (ItemTile item : player.getAttackItems()) {
            HUDbatch.draw(item.getTexture(), itemX, itemY, 0, 0, 16, 16, 1.5f, 1.5f, 0);
            itemX -= 32;
        }

        for (ItemTile item : player.getDefenseItems()) {
            HUDbatch.draw(item.getTexture(), itemX, itemY, 0, 0, 16, 16, 1.5f, 1.5f, 0); // Scaling by 0.5
            itemX -= 32;
        }

        HUDbatch.end();
    }

    private void renderEndTextScreen(String text) {
        // Draw a black screen (if needed)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        HUDbatch.begin();
        font.setColor(Color.WHITE);

        // Center the text horizontally and vertically
        String text2 = "Press R to restart";
        font.getData().setScale(5);
        GlyphLayout layout = new GlyphLayout(font, text);
        font.getData().setScale(2);
        GlyphLayout layout2 = new GlyphLayout(font, text2);

        // Adjust font size for better visibility
        font.getData().setScale(5); // Or find a suitable scale
        font.draw(HUDbatch, text, (Constants.BASE_X_RESOLUTION - layout.width) / 2, (Constants.BASE_Y_RESOLUTION + layout.height) / 2);
        // Draw the second line of text
        font.getData().setScale(2);
        font.draw(HUDbatch, text2, (Constants.BASE_X_RESOLUTION - layout2.width) / 2, (Constants.BASE_Y_RESOLUTION - layout.height - layout2.height) / 2);

        HUDbatch.end();
    }

    public void renderWinScreen() {
        renderEndTextScreen("You win!");
    }

    public void renderLoseScreen() {
        renderEndTextScreen("You lose!");
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}