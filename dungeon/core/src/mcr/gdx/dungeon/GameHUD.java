package mcr.gdx.dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import mcr.gdx.dungeon.elements.PlayerTile;

public class GameHUD {
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final TextureRegion defaultTexture;
    private final PlayerTile player;

    public GameHUD(PlayerTile player) {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        this.player = player;
        defaultTexture = new TextureRegion(Assets.get("wpns_16x16_black_outline.png"), 128, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public void render(SpriteBatch batch) {



        // Draw equipped item
        // spriteBatch.draw(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 20, 110, 32, 32);
        TextureRegion textureRegion;
        if (player.getWeapon() != null) {
            textureRegion = player.getWeapon().getTexture();
        } else {
            textureRegion = defaultTexture;
        }

        batch.draw(textureRegion, 20, 110, 0, 0, 32, 32, 0.5f, 0.5f, 0); // Scaling by 0.5

    }

    public void render() {
        // Draw life bar
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
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}