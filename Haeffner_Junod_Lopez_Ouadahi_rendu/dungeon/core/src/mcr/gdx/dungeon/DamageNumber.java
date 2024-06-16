package mcr.gdx.dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import static mcr.gdx.dungeon.Constants.font;

/**
 * The DamageNumber class represents a damage number that appears when a character takes damage.
 * The damage number floats upwards and disappears after a certain amount of time.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class DamageNumber {
    private final String damageText;
    private final Vector2 position;
    private float timer;
    private static final float DISPLAY_TIME = 2f; // Seconds to display
    private static final float OFFSET = 10;

    /**
     * Constructs a new DamageNumber.
     *
     * @param damageAmount the amount of damage to display
     * @param characterPosition the position of the character that took damage
     */
    public DamageNumber(int damageAmount, Vector2 characterPosition) {
        this.damageText = String.valueOf(damageAmount);
        this.position = new Vector2(characterPosition.x, characterPosition.y + OFFSET); // Offset above character
        this.timer = DISPLAY_TIME;
    }

    /**
     * Updates the state of the DamageNumber. To use in the render loop as it's called
     * every frame.
     *
     * @param delta the time since the last frame
     */
    public void update(float delta) {
        timer -= delta;
        position.y += 5 * delta; // Make it float upwards
    }

    /**
     * Returns whether the DamageNumber has expired.
     *
     * @return true if the DamageNumber has expired, false otherwise
     */
    public boolean isExpired() {
        return timer <= 0;
    }

    /**
     * Draws the DamageNumber.
     *
     * @param batch the SpriteBatch to draw on
     */
    public void draw(SpriteBatch batch) {
        font.getData().setScale(1f);
        GlyphLayout layout = new GlyphLayout(font, damageText);
        font.setColor(Color.RED);
        font.draw(batch, damageText, position.x - layout.width / 2, position.y);
    }
}