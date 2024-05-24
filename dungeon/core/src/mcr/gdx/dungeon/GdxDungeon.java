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
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import mcr.gdx.dungeon.elements.CharacterTile;

import java.util.List;

public class GdxDungeon extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture tilesetTexture;
    private Texture backgroundTexture;
    private TextureRegion backgroundRegion;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private CharacterTile player;
    private OrthographicCamera camera;
    private float pixelScaleFactor = 1.0f; // You can adjust this value to change the scale
    String playerTileSet = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png";
	String mapTileSet = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png";
    private final MapGenerator mapGenerator = new MapGenerator();
	private Vector2 playerOldPosition = new Vector2();
	private List<Rectangle> wallTiles;

    private final InputAdapter inputProcessor = new InputAdapter() {
        @Override
        public boolean keyDown(int keycode) {
			Vector2 newPosition = new Vector2(player.position);
			if((newPosition.x % Constants.TILE_SIZE) != 0){
				newPosition.x = newPosition.x / Constants.TILE_SIZE * Constants.TILE_SIZE;
			}

			if((newPosition.y % Constants.TILE_SIZE) != 0){
				newPosition.y = newPosition.y / Constants.TILE_SIZE * Constants.TILE_SIZE;
			}

            switch (keycode) {
                case Keys.LEFT:
                    newPosition.x -= 16;
                    break;
                case Keys.RIGHT:
					newPosition.x += 16;
                    break;
                case Keys.UP:
					newPosition.y += 16;
                    break;
                case Keys.DOWN:
					newPosition.y -= 16;
                    break;
            }

			// Check for collisions with the new position
			Rectangle newBoundingBox = new Rectangle(newPosition.x, newPosition.y, player.getBoundingBox().width, player.getBoundingBox().height);
			boolean collisionDetected = false;
			for (Rectangle wallTile : wallTiles) {
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
        playerTexture = new Texture(playerTileSet); // replace with your player texture
        playerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        tilesetTexture = new Texture(mapTileSet); // replace with your tileset texture
        tilesetTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        backgroundTexture = new Texture(mapTileSet);
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Create a new TiledMap
        map = new TiledMap();

        // Camera related code
        camera = new OrthographicCamera();
        updateViewport();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / pixelScaleFactor, Gdx.graphics.getHeight() / pixelScaleFactor);
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 200 / camera.viewportWidth);

        // Set the input processor
        Gdx.input.setInputProcessor(inputProcessor);

        // Create a TiledMapTileLayer
        int width = 10; // width of the map
        int height = 10; // height of the map
        int tileWidth = Constants.TILE_SIZE; // width of a tile
        int tileHeight = Constants.TILE_SIZE; // height of a tile
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, tileWidth, tileHeight);

        TextureRegion tileRegion = new TextureRegion(tilesetTexture, 32, 32, tileWidth, tileHeight);
        TextureRegion playerRegion = new TextureRegion(playerTexture, 64, 0, tileWidth, tileHeight);
        backgroundRegion = new TextureRegion(backgroundTexture, 0, 0, tileWidth, tileHeight);

        mapGenerator.generateProceduralMap(25, 25, 5, map, tileRegion);






//		// Create a TiledMapTileLayer.Cell and set its tile to the TextureRegion
//		TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
//		cell.setTile(new StaticTiledMapTile(tileRegion));
//
//		// Add the cell to the layer
//		for (int x = 0; x < width; x++) {
//			for (int y = 0; y < height; y++) {
//				layer.setCell(x, y, cell);
//			}
//		}

        // Create a MapProperties object
        MapProperties mapProperties = new MapProperties();
        mapProperties.put("width", width);
        mapProperties.put("height", height);

		// Add the layer to the map
		//map.getLayers().add(layer);
        // Add the background layer to the map
        //map.getLayers().add(backgroundLayer);


        // Set the map properties
        map.getProperties().putAll(mapProperties);

        // Create the map renderer
        mapRenderer = new OrthogonalTiledMapRenderer(map);

		System.out.println(mapGenerator.validPlayerPos);
        player = new CharacterTile(mapGenerator.validPlayerPos, playerRegion);
		wallTiles = mapGenerator.getWallTiles((TiledMapTileLayer) map.getLayers().get("walls"));
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

        updateViewport();

        // Set the batch's projection matrix to the camera's combined matrix
        batch.setProjectionMatrix(camera.combined);
		// Render the first layer (index 0)
		mapRenderer.setView(camera);
		mapRenderer.render(new int[]{0}); //First render the background

		//Vector2 oldPosition = new Vector2(player.position);


		// Draw the player
		batch.begin();
		player.draw(batch);
		batch.end();



		// Render the second layer (index 1)
		mapRenderer.render(new int[]{1}); //Then render the walls

        Camera.updateCameraPosition(player, camera);



        //mapRenderer.render();

    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        playerTexture.dispose();
        tilesetTexture.dispose();
        map.dispose();
        mapRenderer.dispose();

    }
}