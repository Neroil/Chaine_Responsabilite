package mcr.gdx.dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import static mcr.gdx.dungeon.Constants.font;

public class DamageNumber {
    private final String damageText;
    private final Vector2 position;
    private float timer;
    private static final float DISPLAY_TIME = 2f; // Seconds to display

    public DamageNumber(int damageAmount, Vector2 characterPosition) {
        this.damageText = String.valueOf(damageAmount);
        this.position = new Vector2(characterPosition.x, characterPosition.y + 10); // Offset above character
        this.timer = DISPLAY_TIME;
    }

    public void update(float delta) {
        timer -= delta;
        position.y += 5 * delta; // Make it float upwards
    }

    public boolean isExpired() {
        return timer <= 0;
    }

    public void draw(SpriteBatch batch) {
        font.getData().setScale(1f);
        GlyphLayout layout = new GlyphLayout(font, damageText);
        font.setColor(Color.RED);
        font.draw(batch, damageText, position.x - layout.width / 2, position.y);
    }
}