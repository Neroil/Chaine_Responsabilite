package mcr.gdx.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;

public class GdxDungeon extends ApplicationAdapter implements Disposable {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float pixelScaleFactor = 1.0f;
    private Game game;


    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        game = new Game();
        game.initializeGame();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        // Update the viewport only when the window size changes
        if (Gdx.graphics.getWidth() != camera.viewportWidth * pixelScaleFactor ||
                Gdx.graphics.getHeight() != camera.viewportHeight * pixelScaleFactor) {
            updateCamera();
        }

        // Set the batch's projection matrix to the camera's combined matrix
        batch.setProjectionMatrix(camera.combined);

        // Render the game
        game.getMapRenderer().setView(camera);
        game.render(batch);

        Camera.updateCameraPosition(game.getPlayer(), camera);

        // Handle player input
        game.getInputHandler().handleInput(game.getPlayer(), game.getSpatialHashMap(), Gdx.graphics.getDeltaTime());

        game.getGameHUD().render();

        if (game.isGameOver()) {
            game.getGameHUD().renderLoseScreen();
        } else if (game.isGameWon()) {
            game.getGameHUD().renderWinScreen();
        }

    }

    private void updateCamera() {
        float targetWidth = Gdx.graphics.getWidth();
        float targetHeight = Gdx.graphics.getHeight();

        float baseWidth = Constants.BASE_X_RENDER_RES;
        float baseHeight = Constants.BASE_Y_RENDER_RES;
        float baseAspectRatio = baseWidth / baseHeight;

        float targetAspectRatio = targetWidth / targetHeight;

        // Adjust the viewport based on the aspect ratio
        if (targetAspectRatio > baseAspectRatio) {
            // Window is wider than the base aspect ratio
            camera.viewportHeight = baseHeight;
            camera.viewportWidth = baseHeight * targetAspectRatio;
        } else {
            // Window is taller than the base aspect ratio
            camera.viewportWidth = baseWidth;
            camera.viewportHeight = baseWidth / targetAspectRatio;
        }

        // Calculate the scaling factor based on the adjusted viewport
        pixelScaleFactor = Math.max(targetWidth / camera.viewportWidth, targetHeight / camera.viewportHeight);

        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        Assets.dispose();
        game.dispose();
    }
}
