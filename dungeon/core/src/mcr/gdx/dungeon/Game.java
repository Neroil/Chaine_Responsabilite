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
import java.util.ArrayList;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.elements.EnemyTile;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;
import mcr.gdx.dungeon.elements.items.*;

import static com.badlogic.gdx.math.MathUtils.random;


public class Game {
    private LinkedList<EnemyTile> enemies = new LinkedList<EnemyTile>();
    private LinkedList<CharacterTile> collidableEntities = new LinkedList<CharacterTile>();
    private LinkedList<ItemTile> items = new LinkedList<ItemTile>();

    // Item creators for random item generation
    private static final ItemCreator[] itemCreators = new ItemCreator[] {
            Sword::new,
            Club::new,
            MagicScepter::new,
            DamageRing::new,
            ManaRing::new,
            VigorRing::new
    };

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private PlayerTile player;
    private MapGenerator mapGenerator;
    private InputHandler inputHandler;
    private SpatialHashMap spatialHashMap;
    private int step = 0;
    //private int score;
    private boolean isGameOver;
    private GameHUD gameHUD;

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
        gameHUD.dispose();
    }

    public void initializeGame(){
        mapGenerator = new MapGenerator();
        mapGenerator.initializeTextures();

        map = new TiledMap();
        mapGenerator.generateProceduralMap(Constants.MAP_SIZE, Constants.MAP_SIZE, Constants.NUM_ROOMS, map);
        mapRenderer = new OrthogonalTiledMapRenderer(map);


        TextureRegion playerRegion = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 64, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
        player = new PlayerTile(mapGenerator.validPlayerPos, playerRegion, collidableEntities, this);
        player.snapToTileCenter();
        collidableEntities.add(player);
        spatialHashMap = new SpatialHashMap(Constants.MAP_SIZE * Constants.TILE_SIZE, Constants.MAP_SIZE * Constants.TILE_SIZE);
        initializeCollisionDetection();

        inputHandler = new InputHandler(this);
        Gdx.input.setInputProcessor(inputHandler);

        gameHUD = new GameHUD(player);

        //TODO: Générer ennemis
        // Generate enemies
        generateEnemies();
        //TODO: Générer armes
        generateItems();
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    private void generateEnemies() {
        for (int i = 0; i < Constants.NUM_ENEMIES; i++) {
            Vector2 position = mapGenerator.generateRandomPositionInRoom();
            TextureRegion enemyTexture = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 64, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
            EnemyTile enemyTile = new EnemyTile(position, enemyTexture, collidableEntities, player);
            enemyTile.snapToTileCenter();
            enemies.add(enemyTile);
            collidableEntities.add(enemyTile);
        }
    }

    // Functional interface for creating items
    @FunctionalInterface
    private interface ItemCreator { ItemTile create(Vector2 position); }

    private void generateItems() {
        for (int i = 0; i < Constants.NUM_ITEMS; i++) {
            Vector2 position = mapGenerator.generateRandomPositionInRoom();

            // Randomly select an item creator
            int rdm = random.nextInt(itemCreators.length);
            ItemTile item = itemCreators[rdm].create(position);

            item.snapToTileCenter();
            items.add(item);
        }
    }

    public LinkedList<ItemTile> getItems() {
        return items;
    }

    public SpatialHashMap getSpatialHashMap() {
        return spatialHashMap;
    }

    public void resetGame() {
        map.dispose();
        mapRenderer.dispose();
        enemies.clear();
        items.clear();
        collidableEntities.clear();
        map = new TiledMap();
        mapGenerator.resetMap();
        mapGenerator.generateProceduralMap(Constants.MAP_SIZE, Constants.MAP_SIZE, 5, map);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        player.position.set(mapGenerator.validPlayerPos);
        player.snapToTileCenter();
        collidableEntities.add(player);

        gameHUD = new GameHUD(player);

        isGameOver = false;
        generateEnemies();
        generateItems();

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
        //Move enemies
        for(EnemyTile enemyTile : enemies){
            enemyTile.move(spatialHashMap);
        }

        ItemTile itemToRemove = null;
        for (ItemTile item : getItems()){
            if (player.position.equals(item.position)) {
                player.pickUpItem(item);
                itemToRemove = item;
                break;
            }
        }
        items.remove(itemToRemove);
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

        // Update damage indicators
        for(CharacterTile c : collidableEntities){
            c.update(Gdx.graphics.getDeltaTime());
        }

        // Render the background layer
        getMapRenderer().render(new int[]{0});

        // Draw the player
        batch.begin();

        for (EnemyTile enemyTile : enemies) {
            enemyTile.draw(batch);
        }

        for (ItemTile item : items) {
            item.draw(batch);
        }

        player.draw(batch);
        gameHUD.render(batch);

        batch.end();

        gameHUD.render();

        // Render the wall layer
        getMapRenderer().render(new int[]{1});


    }
}