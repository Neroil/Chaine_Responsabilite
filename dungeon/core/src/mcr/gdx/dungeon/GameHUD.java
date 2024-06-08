package mcr.gdx.dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameHUD {
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private float life = 100;
    private float stamina = 100;
    private float mana = 100;

    public GameHUD() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
    }

    public void render() {
        spriteBatch.begin();

        // Draw life bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(20, 20, life, 20);
        shapeRenderer.end();

        // Draw stamina bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(20, 50, stamina, 20);
        shapeRenderer.end();

        // Draw mana bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(20, 80, mana, 20);
        shapeRenderer.end();

        // Draw equipped item
       // spriteBatch.draw(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 20, 110, 32, 32);
        spriteBatch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
    }
}