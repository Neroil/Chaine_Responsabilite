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
import mcr.gdx.dungeon.elements.EnemyTile;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;
import mcr.gdx.dungeon.elements.items.Ladder;
import mcr.gdx.dungeon.elements.items.attacks.DamageRing;
import mcr.gdx.dungeon.elements.items.attacks.ManaRing;
import mcr.gdx.dungeon.elements.items.attacks.VigorRing;
import mcr.gdx.dungeon.elements.items.weapons.magical.MagicScepter;
import mcr.gdx.dungeon.elements.items.weapons.physical.Club;
import mcr.gdx.dungeon.elements.items.weapons.physical.Sword;

import java.util.LinkedList;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * The Game class represents the main game logic.
 * It handles the game state, player, enemies, items, and game map.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class Game {
    // Item creators for random item generation
    private static final ItemCreator[] itemCreators = new ItemCreator[]{
            Sword::new,
            Club::new,
            MagicScepter::new,
            DamageRing::new,
            ManaRing::new,
            VigorRing::new
    };
    private final LinkedList<EnemyTile> enemies = new LinkedList<>();
    //Everything that is able to block the entities' movements but that isn't the level itself
    private final LinkedList<CharacterTile> collidableEntities = new LinkedList<>();
    private final LinkedList<ItemTile> items = new LinkedList<>();
    private final LinkedList<DamageNumber> damageNumbers = new LinkedList<>();
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private PlayerTile player;
    private MapGenerator mapGenerator;
    private InputHandler inputHandler;
    private SpatialHashMap spatialHashMap;
    private int step = 0;

    private boolean isGameOver;
    private boolean isGameWon;
    private GameHUD gameHUD;

    /**
     * Initializes the game.
     */
    public void initializeGame() {
        //Generate the map
        mapGenerator = new MapGenerator();
        mapGenerator.initializeTextures();

        map = new TiledMap();
        mapGenerator.generateProceduralMap(Constants.MAP_SIZE, Constants.MAP_SIZE, Constants.NUM_ROOMS, map);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        //Create the player
        TextureRegion playerRegion = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 64, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
        player = new PlayerTile(mapGenerator.generateRandomPositionInRoom(), playerRegion, collidableEntities, this);
        player.snapToTileCenter();
        //Adding the player to the list of collidable entities cause the ennemies can collide with them.
        collidableEntities.add(player);

        //Creates the collisions for the generated map
        spatialHashMap = new SpatialHashMap(Constants.MAP_SIZE * Constants.TILE_SIZE, Constants.MAP_SIZE * Constants.TILE_SIZE);
        initializeCollisionDetection();

        //Inputs
        inputHandler = new InputHandler(this);
        Gdx.input.setInputProcessor(inputHandler);

        //Hud
        gameHUD = new GameHUD(player);

        //Generate the different elements of the game
        generateEnemies();
        generateItems();
        generateExit();
    }

    /**
     * Adds a damage number to the game.
     * This logic is held here to the numbers are still visible even if the entity is removed.
     *
     * @param damageNumber the damage number to add
     */
    public void addDamageNumber(DamageNumber damageNumber) {
        damageNumbers.add(damageNumber);
    }

    /**
     * Generates enemies for the game.
     * This method creates a number of enemies defined by Constants.NUM_ENEMIES.
     * Each enemy is given a random position in a room and a texture for its appearance.
     * The enemies are then added to the list of enemies and collidable entities.
     */
    private void generateEnemies() {
        for (int i = 0; i < Constants.NUM_ENEMIES; i++) {
            Vector2 position = mapGenerator.generateRandomPositionInRoom();
            TextureRegion enemyTexture = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Character_2.png"), 64, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
            EnemyTile enemyTile = new EnemyTile(position, enemyTexture, collidableEntities, this);
            enemyTile.snapToTileCenter();
            enemies.add(enemyTile);
            collidableEntities.add(enemyTile);
        }
    }

    /**
     * Generates the exit for the game.
     * This method creates an exit item at a random position in a random room of the dungeon.
     * The exit item is then added to the list of items since it's an item like the others,
     * the logic of the gameWin will be handled in the exit's class.
     */
    private void generateExit() {
        Vector2 position = mapGenerator.generateRandomPositionInRoom();
        TextureRegion exitTexture = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png"), 144, 48, Constants.TILE_SIZE, Constants.TILE_SIZE);
        ItemTile exit = new Ladder(position, exitTexture);
        exit.snapToTileCenter();
        items.add(exit);
    }

    /**
     * Generates items for the game.
     * This method creates a number of items defined by Constants.NUM_ITEMS.
     * Each item is given a random position in a room and a random type.
     * The items are then added to the list of items.
     */
    private void generateItems() {
        for (int i = 0; i < Constants.NUM_ITEMS; i++) {
            Vector2 position = mapGenerator.generateRandomPositionInRoom();

            // Randomly select an item in the item creator
            int rdm = random.nextInt(itemCreators.length);
            ItemTile item = itemCreators[rdm].create(position);

            item.snapToTileCenter();
            items.add(item);
        }
    }

    /**
     * Sets the game state to won.
     * This method can be called by the exit item when the player reaches it.
     */
    public void exitLevel() {
        isGameWon = true;
    }

    /**
     * Resets the game.
     * This method disposes of the current map and creates a new one.
     * It also resets the player and the lists of enemies, items, and collidable entities.
     * Finally, it generates new enemies, items, and an exit, and initializes collision detection.
     */
    public void resetGame() {
        map.dispose();
        mapRenderer.dispose();
        enemies.clear();
        items.clear();
        collidableEntities.clear();
        map = new TiledMap();
        mapGenerator.clearMap();
        mapGenerator.generateProceduralMap(Constants.MAP_SIZE, Constants.MAP_SIZE, 5, map);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        player.reset();
        player.position.set(mapGenerator.generateRandomPositionInRoom());
        player.snapToTileCenter();
        collidableEntities.add(player);
        gameHUD.setPlayer(player);

        //Reset game states
        isGameOver = false;
        isGameWon = false;

        //Generate the different elements of the game
        generateEnemies();
        generateItems();
        generateExit();

        initializeCollisionDetection();
    }

    /**
     * Initializes collision detection.
     * This method clears the spatial hash map and inserts all wall tiles into it.
     */
    private void initializeCollisionDetection() {
        spatialHashMap.clear();

        for (Rectangle wallTile : mapGenerator.getWallTiles((TiledMapTileLayer) map.getLayers().get("walls")))
            spatialHashMap.insert(wallTile);
    }

    /**
     * Updates the game state.
     * This method increments the step counter, checks if the player is dead,
     * and updates the player's resources every 10 steps.
     * It also moves the enemies, removes dead enemies, and checks if the player is on an item.
     */
    public void updateStep() {

        ++step;

        //Check if player is dead
        if (!player.isAlive())
            isGameOver = true;

        if (step % Constants.RESSOURCE_REGEN_TIMEOUT == 0)
            player.updateRessources();

        //List of dead ennemies to remove
        LinkedList<EnemyTile> enemyToRemove = new LinkedList<>();
        //Move enemies
        for (EnemyTile enemyTile : enemies) {
            //Check if enemy is dead
            if (!enemyTile.isAlive()) {
                enemyToRemove.add(enemyTile);
                continue;
            }
            enemyTile.move(spatialHashMap);
        }

        //Remove dead enemies
        for (EnemyTile enemy : enemyToRemove) {
            enemies.remove(enemy);
            collidableEntities.remove(enemy);
        }

        //Check if player is positioned over an item
        ItemTile itemToRemove = null;
        for (ItemTile item : getItems())
            if (player.position.equals(item.position)) {
                player.pickUpItem(item);
                itemToRemove = item;
                break;
            }
        items.remove(itemToRemove);
    }

    /**
     * Renders the game.
     * This method renders the background and wall layers of the map, draws the entities and damage numbers, and updates the damage numbers.
     */
    public void render(SpriteBatch batch) {
        // Render the background layer
        getMapRenderer().render(new int[]{0});


        batch.begin();

        //Draws the entities
        for (EnemyTile enemyTile : enemies)
            enemyTile.draw(batch);

        for (ItemTile item : items)
            item.draw(batch);

        player.draw(batch);

        batch.end();

        // Render the wall layer
        getMapRenderer().render(new int[]{1});

        batch.begin();
        // Update and draw damage numbers
        for (int i = damageNumbers.size() - 1; i >= 0; i--) {
            DamageNumber number = damageNumbers.get(i);
            number.update(Gdx.graphics.getDeltaTime());

            if (number.isExpired())
                damageNumbers.remove(i);
            else
                number.draw(batch);
        }
        batch.end();
    }

    /**
     * Disposes of the game resources.
     * This method disposes of the map, map renderer, map generator, and game HUD.
     */
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        gameHUD.dispose();
    }

    /**
     * Returns whether the game is won.
     *
     * @return true if the game is won, false otherwise
     */
    public boolean isGameWon() {
        return isGameWon;
    }

    /**
     * Returns whether the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**************************************************************************
     ********************************* Getters ********************************
     **************************************************************************/

    /**
     * Returns the map renderer.
     *
     * @return the map renderer
     */
    OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    /**
     * Returns the player.
     *
     * @return the player
     */
    public PlayerTile getPlayer() {
        return player;
    }

    /**
     * Returns the game HUD.
     *
     * @return the game HUD
     */
    public GameHUD getGameHUD() {
        return gameHUD;
    }

    /**
     * Returns the current step.
     *
     * @return the current step
     */
    public int getStep() {
        return step;
    }

    /**
     * Returns the list of enemies.
     *
     * @return the list of enemies
     */
    public LinkedList<ItemTile> getItems() {
        return items;
    }

    /**
     * Returns the spatial hash map.
     *
     * @return the spatial hash map
     */
    public SpatialHashMap getSpatialHashMap() {
        return spatialHashMap;
    }

    /**
     * Returns the input handler.
     *
     * @return the input handler
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Functional interface for creating items.
     */
    @FunctionalInterface
    private interface ItemCreator {
        ItemTile create(Vector2 position);
    }
}