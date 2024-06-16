package mcr.gdx.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.CharacterTile;

/**
 * The InputHandler class is responsible for handling user input.
 * It extends InputAdapter to handle keyboard input and uses a Direction enum to track the direction of movement.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class InputHandler extends InputAdapter {

    /**
     * Enum representing the possible directions of movement, used to reset the timer
     * in case we're trying to strafe in a different direction
     */
    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Direction direction = null;
    private Direction lastDirection = null;
    private final Game game;
    private float moveDelay = 0.2f;             // Delay in seconds between each movement
    private float changeDirectionDelay = 0.1f;  // Delay in seconds between each direction change
    private float changeDirectionTimer = 0f;
    private float moveTimer = 0f;
    private boolean isKeyPressed = false;       // Timer to track the elapsed time

    /**
     * Constructs a new InputHandler for the specified game.
     *
     * @param game the game to handle input for
     */
    public InputHandler(Game game) {
        this.game = game;
    }

    /**
     * Handles input for the game.
     * Arrow keys -> move the player character in the four cardinal directions
     * Shift + arrow keys -> move the player character faster
     * Z -> attack (Y on a QWERTZ keyboard)
     * R -> reset the game
     *
     * @param player            the player character to handle input for
     * @param spatialHashMap    the spatial hash map for collision detection
     * @param delta             the time elapsed since the last frame
     */
    public void handleInput(CharacterTile player, SpatialHashMap spatialHashMap, float delta) {
        Vector2 newPosition = new Vector2(player.position);
        boolean movePlayer = false;
        int horizontalInput = 0;
        int verticalInput = 0;

        // Can only move or attack if the game isn't over or won
        if (!game.isGameWon() && !game.isGameOver()) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                horizontalInput = -1;
                movePlayer = true;
                direction = Direction.LEFT;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                horizontalInput = 1;
                movePlayer = true;
                direction = Direction.RIGHT;
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                verticalInput = 1;
                movePlayer = true;
                direction = Direction.UP;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                verticalInput = -1;
                movePlayer = true;
                direction = Direction.DOWN;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                moveDelay = 0.05f;
            } else {
                moveDelay = 0.2f;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                game.getPlayer().attack();
                return;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            this.game.resetGame();
            return;
        }

        newPosition.x += horizontalInput * Constants.TILE_SIZE;
        newPosition.y += verticalInput * Constants.TILE_SIZE;

        if (movePlayer) {
            //Logic used to be able to move the player as soon as we click on the direction, allows strafing directions too
            if (!isKeyPressed || ((lastDirection != direction) && (changeDirectionTimer >= changeDirectionDelay))) {
                isKeyPressed = true;
                changeDirectionTimer = 0f;
                lastDirection = direction;
                player.move(new Vector2(horizontalInput, verticalInput), spatialHashMap);
            } else {
                moveTimer += delta; // Accumulate the elapsed time
                changeDirectionTimer += delta;
                if (moveTimer >= moveDelay) { // Check if enough time has elapsed
                    moveTimer = 0f; // Reset the timer
                    player.move(new Vector2(horizontalInput, verticalInput), spatialHashMap);
                }
            }
        } else {
            isKeyPressed = false;
            moveTimer = 0f; // Reset the timer when no keys are pressed
            changeDirectionTimer = 0f;
        }
    }
}
