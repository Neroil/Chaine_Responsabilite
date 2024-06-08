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
    private LinkedList<CharacterTile> collidableEntities = new LinkedList<CharacterTile>();
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private CharacterTile player;
    private MapGenerator mapGenerator;
    private InputHandler inputHandler;
    private SpatialHashMap spatialHashMap;
    private int step = 0;
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
        player = new CharacterTile(mapGenerator.validPlayerPos, playerRegion, collidableEntities);
        player.snapToTileCenter();
        collidableEntities.add(player);
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
            Vector2 position = mapGenerator.generateRandomPositionInRoom();
            TextureRegion enemyTexture = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 64, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
            Enemy enemy = new Enemy(position, enemyTexture, collidableEntities, player);
            enemy.snapToTileCenter();
            enemies.add(enemy);
            collidableEntities.add(enemy);
        }
    }


    public SpatialHashMap getSpatialHashMap() {
        return spatialHashMap;
    }

    public void resetGame() {
        map.dispose();
        mapRenderer.dispose();
        enemies.clear();
        collidableEntities.clear();
        map = new TiledMap();
        mapGenerator.resetMap();
        mapGenerator.generateProceduralMap(Constants.MAP_SIZE, Constants.MAP_SIZE, 5, map);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        player.position.set(mapGenerator.validPlayerPos);
        player.snapToTileCenter();
        collidableEntities.add(player);

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

    public void updateStep() {
        // Update game state based on elapsed time
        //handleChain();
        // ...
        ++step;
        for(Enemy enemy : enemies){
            enemy.move(spatialHashMap);
        }
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