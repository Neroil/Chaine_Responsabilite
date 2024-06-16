package mcr.gdx.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Main class for the GdxDungeon game.
 * This class extends ApplicationAdapter and implements Disposable to manage the game lifecycle and resources.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class GdxDungeon extends ApplicationAdapter implements Disposable {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float pixelScaleFactor = 1.0f;
    private Game game;


    /**
     * This method is called when the application is created.
     * It initializes the SpriteBatch, OrthographicCamera, Game objects and the game itself.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        game = new Game();
        game.initializeGame();
    }

    /**
     * This method is called every frame to render the game.
     * It updates the camera, handles player input, and renders the game and HUD.
     * It's also where the game over and win screens are rendered.
     */
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

    /**
     * This method updates the camera's viewport based on the window's aspect ratio.
     */
    private void updateCamera() {
        float targetWidth = Gdx.graphics.getWidth();
        float targetHeight = Gdx.graphics.getHeight();

        //Render resolution of the game, it's low since it's a pixelated game
        float baseWidth = Constants.BASE_X_RENDER_RES;
        float baseHeight = Constants.BASE_Y_RENDER_RES;
        float baseAspectRatio = baseWidth / baseHeight;

        float targetAspectRatio = targetWidth / targetHeight;

        // Adjust the viewport based on the aspect ratio
        if (targetAspectRatio > baseAspectRatio) {
            camera.viewportHeight = baseHeight;
            camera.viewportWidth = baseHeight * targetAspectRatio;
        } else {
            camera.viewportWidth = baseWidth;
            camera.viewportHeight = baseWidth / targetAspectRatio;
        }

        // Calculate the scaling factor based on the adjusted viewport
        pixelScaleFactor = Math.max(targetWidth / camera.viewportWidth, targetHeight / camera.viewportHeight);

        camera.update();
    }

    /**
     * This method is called when the application is disposed.
     * It disposes the SpriteBatch, Assets, and Game objects to free up resources.
     */
    @Override
    public void dispose() {
        batch.dispose();
        Assets.dispose();
        game.dispose();
    }
}
