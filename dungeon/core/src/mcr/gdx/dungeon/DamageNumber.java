package mcr.gdx.dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;

public class DamageNumber {
    private final String damageText;
    private final Vector2 position;
    private float timer;
    private static final float DISPLAY_TIME = 2f; // Seconds to display
    private static final BitmapFont font = new BitmapFont(); // Use your preferred font

    public DamageNumber(int damageAmount, Vector2 characterPosition) {
        this.damageText = String.valueOf(damageAmount);
        this.position = new Vector2(characterPosition.x, characterPosition.y + 10); // Adjust offset as needed
        this.timer = DISPLAY_TIME;
    }

    public void update(float delta) {
        timer -= delta;
        position.y += 5 * delta; // Make it float upwards
    }

    public boolean isExpired() {
        return timer <= 0;
    }

    public void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        GlyphLayout layout = new GlyphLayout(font, damageText);
        font.setColor(Color.RED);
        font.draw(batch, damageText, position.x - layout.width / 2, position.y);
    }
}