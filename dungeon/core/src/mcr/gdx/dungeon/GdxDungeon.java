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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import mcr.gdx.dungeon.elements.CharacterTile;

public class GdxDungeon extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture playerTexture;
	private Texture tilesetTexture;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private CharacterTile player;
	private OrthographicCamera camera;
	private float pixelScaleFactor = 1.0f; // You can adjust this value to change the scale
	String mapTileSet = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png";
	String playerTileSet = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png";

	private InputAdapter inputProcessor = new InputAdapter() {
		@Override
		public boolean keyDown(int keycode) {
			switch (keycode) {
				case Keys.LEFT:
					player.position.x -= 16;
					break;
				case Keys.RIGHT:
					player.position.x += 16;
					break;
				case Keys.UP:
					player.position.y += 16;
					break;
				case Keys.DOWN:
					player.position.y -= 16;
					break;
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

		// Create a new TiledMap
		map = new TiledMap();

		// Camera related code
		camera = new OrthographicCamera();
		updateViewport();
		camera.setToOrtho(false, Gdx.graphics.getWidth() / pixelScaleFactor, Gdx.graphics.getHeight() / pixelScaleFactor);

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

		// Create a TiledMapTileLayer.Cell and set its tile to the TextureRegion
		TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
		cell.setTile(new StaticTiledMapTile(tileRegion));

		// Add the cell to the layer
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				layer.setCell(x, y, cell);
			}
		}

		// Create a MapProperties object
		MapProperties mapProperties = new MapProperties();
		mapProperties.put("width", width);
		mapProperties.put("height", height);

		// Add the layer to the map
		map.getLayers().add(layer);

		// Set the map properties
		map.getProperties().putAll(mapProperties);

		// Create the map renderer
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		// Create the player at the middle of the map
		int mapWidthInPixels = width * tileWidth;
		int mapHeightInPixels = height * tileHeight;
		player = new CharacterTile(new Vector2((float) mapWidthInPixels / 2, (float) mapHeightInPixels / 2), playerRegion);
	}

	private void updateViewport() {
		float targetWidth = Gdx.graphics.getWidth();
		float targetHeight = Gdx.graphics.getHeight();

		float baseWidth = 320;
		float baseHeight = 240;
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

	private void updateCameraPosition() {
		float playerX = player.position.x;
		float playerY = player.position.y;

		// Get the map dimensions
		int mapWidth = map.getProperties().get("width", Integer.class) * Constants.TILE_SIZE;
		int mapHeight = map.getProperties().get("height", Integer.class) * Constants.TILE_SIZE;

		// Calculate the camera position based on the player's position and map boundaries
		float cameraX = Math.max(camera.viewportWidth / 2, Math.min(playerX, mapWidth - camera.viewportWidth / 2));
		float cameraY = Math.max(camera.viewportHeight / 2, Math.min(playerY, mapHeight - camera.viewportHeight / 2));

		camera.position.set(cameraX, cameraY, 0);
		camera.update();
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);

		updateViewport();

		updateCameraPosition();

		// Set the batch's projection matrix to the camera's combined matrix
		batch.setProjectionMatrix(camera.combined);

		mapRenderer.setView(camera);
		mapRenderer.render();

		batch.begin();
		player.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		playerTexture.dispose();
		tilesetTexture.dispose();
		map.dispose();
		mapRenderer.dispose();
	}
}