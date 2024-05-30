package mcr.gdx.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mcr.gdx.dungeon.elements.CharacterTile;
import mcr.gdx.dungeon.InputHandler;

public class GdxDungeon extends ApplicationAdapter implements Disposable {
    private SpriteBatch batch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private CharacterTile player;
    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private MapGenerator mapGenerator;
    private final InputHandler inputHandler;
    private final SpatialHashMap spatialHashMap;
    private float pixelScaleFactor = 1.0f;

    public GdxDungeon() {
        inputHandler = new InputHandler(this);
        spatialHashMap = new SpatialHashMap(25 * Constants.TILE_SIZE, 25 * Constants.TILE_SIZE);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        mapGenerator = new MapGenerator();
        mapGenerator.initializeTextures();

        map = new TiledMap();
        mapGenerator.generateProceduralMap(25, 25, 5, map);

        mapRenderer = new OrthogonalTiledMapRenderer(map);

        TextureRegion playerRegion = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 64, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
        player = new CharacterTile(mapGenerator.validPlayerPos, playerRegion);
        player.snapToTileCenter();

        initializeCollisionDetection();

        Gdx.input.setInputProcessor(inputHandler);
    }

    private void initializeCollisionDetection() {
        spatialHashMap.clear();
        for (Rectangle wallTile : mapGenerator.getWallTiles((TiledMapTileLayer)map.getLayers().get("walls"))) {
            spatialHashMap.insert(wallTile);
        }
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

        // Render the background layer
        mapRenderer.setView(camera);
        mapRenderer.render(new int[]{0});

        // Draw the player
        batch.begin();
        player.draw(batch);
        batch.end();

        // Render the wall layer
        mapRenderer.render(new int[]{1});
        Camera.updateCameraPosition(player, camera);

        // Handle player input
        inputHandler.handleInput(player, spatialHashMap,Gdx.graphics.getDeltaTime());
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
            camera.viewportHeight = baseHeight * 0.7f;
            camera.viewportWidth = baseHeight * targetAspectRatio * 0.7f;
        } else {
            // Window is taller than the base aspect ratio
            camera.viewportWidth = baseWidth;
            camera.viewportHeight = baseWidth / targetAspectRatio;
        }

        // Calculate the scaling factor based on the adjusted viewport
        pixelScaleFactor = Math.max(targetWidth / camera.viewportWidth, targetHeight / camera.viewportHeight);

        camera.update();
    }

    public void resetGame() {
        map.dispose();
        mapRenderer.dispose();

        map = new TiledMap();
        mapGenerator.generateProceduralMap(25, 25, 5, map);

        mapRenderer = new OrthogonalTiledMapRenderer(map);

        player.position.set(mapGenerator.validPlayerPos);
        player.snapToTileCenter();

        initializeCollisionDetection();
    }

    @Override
    public void dispose() {
        batch.dispose();
        Assets.dispose();
        map.dispose();
        mapRenderer.dispose();
        mapGenerator.dispose();
    }
}
