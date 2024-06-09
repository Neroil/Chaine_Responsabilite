package mcr.gdx.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.CharacterTile;

import java.util.Set;

public class InputHandler extends InputAdapter {
    //TODO: Gérer gestion de STEP
    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Direction direction = null;
    private Direction lastDirection = null;
    private final Game game;
    private float moveDelay = 0.2f;// Delay in seconds between each movement
    private float changeDirectionDelay = 0.1f;// Delay in seconds between each direction change
    private float changeDirectionTimer = 0f;
    private float moveTimer = 0f;
    private boolean isKeyPressed = false;// Timer to track the elapsed time

    public InputHandler(Game game) {
        this.game = game;
    }

    public void handleInput(CharacterTile player, SpatialHashMap spatialHashMap, float delta) {
        Vector2 newPosition = new Vector2(player.position);
        boolean movePlayer = false;
        int horizontalInput = 0;
        int verticalInput = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalInput = -1;
            movePlayer = true;
            direction = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalInput = 1;
            movePlayer = true;
            direction = Direction.RIGHT;
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            verticalInput = 1;
            movePlayer = true;
            direction = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            verticalInput = -1;
            movePlayer = true;
            direction = Direction.DOWN;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            moveDelay = 0.05f;
        } else {
            moveDelay = 0.2f;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            this.game.resetGame();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            game.getPlayer().attack();
            return;
        }

        newPosition.x += horizontalInput * Constants.TILE_SIZE;
        newPosition.y += verticalInput * Constants.TILE_SIZE;

        if (movePlayer) {
            if (!isKeyPressed || ((lastDirection != direction) &&  (changeDirectionTimer >= changeDirectionDelay))){ // If a key is newly pressed, move immediately
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

//    private void updatePlayerPosition(CharacterTile player, Vector2 newPosition, SpatialHashMap spatialHashMap) {
//        Rectangle newBoundingBox = new Rectangle(newPosition.x, newPosition.y, player.getBoundingBox().width, player.getBoundingBox().height);
//        Set<Rectangle> potentialColliders = spatialHashMap.getPotentialColliders(newBoundingBox);
//        boolean collisionDetected = false;
//        for (Rectangle wallTile : potentialColliders) {
//            if (newBoundingBox.overlaps(wallTile)) {
//                collisionDetected = true;
//                break;
//            }
//        }
//        if (!collisionDetected) {
//            player.position.set(newPosition);
//        }
//
//
//    }
}
