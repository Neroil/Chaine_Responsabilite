package mcr.gdx.dungeon;

import mcr.gdx.dungeon.elements.CharacterTile;

public class Camera {

    static void updateCameraPosition(CharacterTile player, com.badlogic.gdx.graphics.OrthographicCamera camera) {
        // Get the player's position
        float playerX = player.position.x;
        float playerY = player.position.y;

//        // Update the camera's position to match the player's position
//        camera.position.set(playerX, playerY, 0);
//        camera.update();
        // Define a lerp factor
        float lerpFactor = 0.1f;

        // Update the camera's position to match the player's position using lerp
        camera.position.x += (playerX - camera.position.x) * lerpFactor;
        camera.position.y += (playerY - camera.position.y) * lerpFactor;

        camera.update();
    }
}
