package mcr.gdx.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import mcr.gdx.dungeon.elements.CharacterTile;

import java.util.List;
import java.util.Set;

public class GdxDungeon extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture tilesetTexture;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private CharacterTile player;
    private OrthographicCamera camera;
    private float pixelScaleFactor = 1.0f;
    private final String playerTileSet = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png";
    private final String mapTileSet = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png";
    private final MapGenerator mapGenerator = new MapGenerator();
    private Vector2 playerOldPosition = new Vector2();
    private List<Rectangle> wallTiles;
    private SpatialHashMap spatialHashMap;

    private final InputAdapter inputProcessor = new InputAdapter() {
        @Override
        public boolean keyDown(int keycode) {
            Vector2 newPosition = new Vector2(player.position);

            switch (keycode) {
                case Keys.LEFT:
                    newPosition.x -= Constants.TILE_SIZE;
                    break;
                case Keys.RIGHT:
                    newPosition.x += Constants.TILE_SIZE;
                    break;
                case Keys.UP:
                    newPosition.y += Constants.TILE_SIZE;
                    break;
                case Keys.DOWN:
                    newPosition.y -= Constants.TILE_SIZE;
                    break;
            }

            // Check for collisions with the new position
            Rectangle newBoundingBox = new Rectangle(newPosition.x, newPosition.y, player.getBoundingBox().width, player.getBoundingBox().height);
            Set<Rectangle> potentialColliders = spatialHashMap.getPotentialColliders(newBoundingBox);
            boolean collisionDetected = false;
            for (Rectangle wallTile : potentialColliders) {
                if (newBoundingBox.overlaps(wallTile)) {
                    collisionDetected = true;
                    break;
                }
            }

            // Update the player's position only if there's no collision
            if (!collisionDetected) {
                player.position.set(newPosition);
            }

            return true;
        }
    };

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerTexture = new Texture(playerTileSet);
        playerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        tilesetTexture = new Texture(mapTileSet);
        tilesetTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Create a new TiledMap
        map = new TiledMap();

        // Camera related code
        camera = new OrthographicCamera();
        updateViewport();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / pixelScaleFactor, Gdx.graphics.getHeight() / pixelScaleFactor);
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 200 / camera.viewportWidth);


        TextureRegion playerRegion = new TextureRegion(playerTexture, 64, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);

        mapGenerator.generateProceduralMap(25, 25, 5, map);

        // Create a MapProperties object
        MapProperties mapProperties = new MapProperties();
        mapProperties.put("width", 25);
        mapProperties.put("height", 25);

        // Set the map properties
        map.getProperties().putAll(mapProperties);

        // Create the map renderer
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        player = new CharacterTile(mapGenerator.validPlayerPos, playerRegion);
        player.snapToTileCenter();

        // Get the collision tiles
        wallTiles = mapGenerator.getWallTiles((TiledMapTileLayer) map.getLayers().get("walls"));
        spatialHashMap = new SpatialHashMap(25 * Constants.TILE_SIZE, 25 * Constants.TILE_SIZE);
        for (Rectangle wallTile : wallTiles) {
            spatialHashMap.insert(wallTile);
        }

        // Set the input processor
        Gdx.input.setInputProcessor(inputProcessor);
    }

    private void updateViewport() {
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
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        // Update the viewport only when the window size changes
        if (Gdx.graphics.getWidth() != camera.viewportWidth * pixelScaleFactor ||
                Gdx.graphics.getHeight() != camera.viewportHeight * pixelScaleFactor) {
            updateViewport();
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
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        tilesetTexture.dispose();
        map.dispose();
        mapRenderer.dispose();
        mapGenerator.dispose();
    }
}