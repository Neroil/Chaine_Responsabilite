package mcr.gdx.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.elements.CharacterTile;

import java.util.LinkedList;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.elements.Enemy;

import static com.badlogic.gdx.math.MathUtils.random;


public class Game {
    private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private CharacterTile player;
    private MapGenerator mapGenerator;
    private InputHandler inputHandler;
    private SpatialHashMap spatialHashMap;
    //private int score;
    private boolean isGameOver;

    private GenericHandler firstHandler; // The start of the chain

    public Game() {
        //firstHandler = new MovementHandler(this); // Movement is the first step
        //firstHandler.setNext(new EnemyTurnHandler(this)); // Then enemies take their turn

    }

    OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    CharacterTile getPlayer() {
        return player;
    }

    public void dispose(){
        map.dispose();
        mapRenderer.dispose();
        mapGenerator.dispose();
    }

    public void initializeGame(){
        mapGenerator = new MapGenerator();
        mapGenerator.initializeTextures();

        map = new TiledMap();
        mapGenerator.generateProceduralMap(Constants.MAP_SIZE, Constants.MAP_SIZE, Constants.NUM_ROOMS, map);
        mapRenderer = new OrthogonalTiledMapRenderer(map);


        TextureRegion playerRegion = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 64, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
        player = new CharacterTile(mapGenerator.validPlayerPos, playerRegion);
        player.snapToTileCenter();
        spatialHashMap = new SpatialHashMap(Constants.MAP_SIZE * Constants.TILE_SIZE, Constants.MAP_SIZE * Constants.TILE_SIZE);
        initializeCollisionDetection();

        inputHandler = new InputHandler(this);
        Gdx.input.setInputProcessor(inputHandler);

        //TODO: Générer ennemis
        // Generate enemies
        generateEnemies();
        //TODO: Générer armes
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    private void generateEnemies() {
        for (int i = 0; i < Constants.NUM_ENEMIES; i++) {
            Vector2 position;
            do {
                position = generateRandomPosition();
            } while (!mapGenerator.isCellInsideAnyRoom((int) position.x, (int) position.y));

            TextureRegion enemyTexture = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 64, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
            Enemy enemy = new Enemy(position, enemyTexture);
            enemies.add(enemy);
        }
    }

    private Vector2 generateRandomPosition() {
        int x = random.nextInt(Constants.MAP_SIZE);
        int y = random.nextInt(Constants.MAP_SIZE);
        return new Vector2(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
    }

    public SpatialHashMap getSpatialHashMap() {
        return spatialHashMap;
    }

    public void resetGame() {
        map.dispose();
        mapRenderer.dispose();
        enemies.clear();
        map = new TiledMap();
        mapGenerator.resetMap();
        mapGenerator.generateProceduralMap(Constants.MAP_SIZE, Constants.MAP_SIZE, 5, map);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        player.position.set(mapGenerator.validPlayerPos);
        player.snapToTileCenter();

        isGameOver = false;
        generateEnemies();
        initializeCollisionDetection();
    }

    private void initializeCollisionDetection() {
        spatialHashMap.clear();
        for (Rectangle wallTile : mapGenerator.getWallTiles((TiledMapTileLayer)map.getLayers().get("walls"))) {
            spatialHashMap.insert(wallTile);
        }
    }

    public void setGameOver(boolean state) {
        isGameOver = state;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void update(float deltaTime) {
        // Update game state based on elapsed time
        //handleChain();
        // ...
    }

//    public void handleChain() {
//        firstHandler.handleRequest();
//    }


    public void loadResources() {
        // Load game resources
    }

    public void unloadResources() {
        // Unload game resources
    }

    public void render(SpriteBatch batch) {
        // Render the background layer
        getMapRenderer().render(new int[]{0});

        // Draw the player
        batch.begin();
        player.draw(batch);
        for (Enemy enemy : enemies) {
            enemy.draw(batch);
        }
        batch.end();

        // Render the wall layer
        getMapRenderer().render(new int[]{1});
    }
}