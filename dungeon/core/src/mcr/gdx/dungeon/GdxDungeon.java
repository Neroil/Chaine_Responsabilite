package mcr.gdx.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import mcr.gdx.dungeon.elements.CharacterTile;

public class GdxDungeon extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture playerTexture;
	private Texture tilesetTexture;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private CharacterTile player;

	@Override
	public void create() {
		batch = new SpriteBatch();
		playerTexture = new Texture("badlogic.jpg"); // replace with your player texture
		tilesetTexture = new Texture("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png"); // replace with your tileset texture

		// Create a new TiledMap
		map = new TiledMap();

		// Create a TiledMapTileLayer
		int width = 10; // width of the map
		int height = 10; // height of the map
		int tileWidth = 16; // width of a tile
		int tileHeight = 16; // height of a tile
		TiledMapTileLayer layer = new TiledMapTileLayer(width, height, tileWidth, tileHeight);

		// Create a TextureRegion for the specific tile from the tileset
		int tilesetWidth = tilesetTexture.getWidth();
		int tilesetHeight = tilesetTexture.getHeight();
		int tileX = 0; // x-coordinate of the tile in the tileset
		int tileY = 0; // y-coordinate of the tile in the tileset
		TextureRegion tileRegion = new TextureRegion(tilesetTexture, tileX * tileWidth, tileY * tileHeight, tileWidth, tileHeight);

		// Create a TiledMapTileLayer.Cell and set its tile to the TextureRegion
		TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
		cell.setTile(new StaticTiledMapTile(tileRegion));

		// Add the cell to the layer
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				layer.setCell(x, y, cell);
			}
		}

		// Add the layer to the map
		map.getLayers().add(layer);

		// Create the map renderer
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		// Create the player at the middle of the map
		int mapWidthInPixels = width * tileWidth;
		int mapHeightInPixels = height * tileHeight;
		player = new CharacterTile(new Vector2((float) mapWidthInPixels / 2, (float) mapHeightInPixels / 2), playerTexture);
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);
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