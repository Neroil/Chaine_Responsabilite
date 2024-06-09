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

import mcr.gdx.dungeon.elements.EnemyTile;
import mcr.gdx.dungeon.elements.ItemTile;
import mcr.gdx.dungeon.elements.PlayerTile;
import mcr.gdx.dungeon.elements.items.Ladder;
import mcr.gdx.dungeon.elements.items.attacks.DamageRing;
import mcr.gdx.dungeon.elements.items.attacks.ManaRing;
import mcr.gdx.dungeon.elements.items.attacks.VigorRing;
import mcr.gdx.dungeon.elements.items.weapons.physical.Club;
import mcr.gdx.dungeon.elements.items.weapons.magical.MagicScepter;
import mcr.gdx.dungeon.elements.items.weapons.physical.Sword;

import static com.badlogic.gdx.math.MathUtils.random;


public class Game {
    private final LinkedList<EnemyTile> enemies = new LinkedList<>();
    private final LinkedList<CharacterTile> collidableEntities = new LinkedList<>();
    private final LinkedList<ItemTile> items = new LinkedList<>();
    private final LinkedList<DamageNumber> damageNumbers = new LinkedList<>();

    // Item creators for random item generation
    private static final ItemCreator[] itemCreators = new ItemCreator[]{
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

    private boolean isGameOver;
    private boolean isGameWon;
    private GameHUD gameHUD;

    OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public PlayerTile getPlayer() {
        return player;
    }

    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        mapGenerator.dispose();
        gameHUD.dispose();
    }

    public void initializeGame() {
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

        //Generate the different elements of the game
        generateEnemies();
        generateItems();
        generateExit();
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public void addDamageNumber(DamageNumber damageNumber) {
        damageNumbers.add(damageNumber);
    }

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

    private void generateExit() {
        Vector2 position = mapGenerator.generateRandomPositionInRoom();
        TextureRegion exitTexture = new TextureRegion(Assets.get("2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png"), 144, 48, Constants.TILE_SIZE, Constants.TILE_SIZE);
        ItemTile exit = new Ladder(position, exitTexture);
        exit.snapToTileCenter();
        items.add(exit);
    }

    // Functional interface for creating items
    @FunctionalInterface
    private interface ItemCreator {
        ItemTile create(Vector2 position);
    }

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

    public void exitLevel() {
        isGameWon = true;
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
        player.reset();
        player.position.set(mapGenerator.validPlayerPos);
        player.snapToTileCenter();
        collidableEntities.add(player);
        gameHUD.setPlayer(player);

        isGameOver = false;
        isGameWon = false;

        generateEnemies();
        generateItems();
        generateExit();

        initializeCollisionDetection();
    }

    private void initializeCollisionDetection() {
        spatialHashMap.clear();

        for (Rectangle wallTile : mapGenerator.getWallTiles((TiledMapTileLayer) map.getLayers().get("walls")))
            spatialHashMap.insert(wallTile);
    }

    public int getStep() {
        return step;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void updateStep() {
        // Update the game state
        ++step;

        //Check if player is dead
        if (!player.isAlive())
            isGameOver = true;

        if (step % 10 == 0)
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

        //Check if player is on an item
        ItemTile itemToRemove = null;
        for (ItemTile item : getItems())
            if (player.position.equals(item.position)) {
                player.pickUpItem(item);
                itemToRemove = item;
                break;
            }
        items.remove(itemToRemove);
    }


    public void render(SpriteBatch batch) {
        // Render the background layer
        getMapRenderer().render(new int[]{0});

        // Draw the player
        batch.begin();

        for (EnemyTile enemyTile : enemies)
            enemyTile.draw(batch);

        for (ItemTile item : items)
            item.draw(batch);

        player.draw(batch);
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
        // Render the wall layer
        getMapRenderer().render(new int[]{1});
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public GameHUD getGameHUD() {
        return gameHUD;
    }
}