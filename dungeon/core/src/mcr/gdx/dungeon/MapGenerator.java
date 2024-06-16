package mcr.gdx.dungeon;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import mcr.gdx.dungeon.elements.WallCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The MapGenerator class is responsible for generating the game map.
 * It creates rooms and corridors, sets wall tiles, and generates valid positions within the dungeon.
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class MapGenerator{
    private static final String MAP_TILE_SET = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png";
    private static final WallCell[] WALL_CELLS = new WallCell[2];

    private final Random random;
    private final Array<Rectangle> rooms;
    private final Array<Rectangle> corridors;

    /**
     * Constructs a new MapGenerator.
     * Initializes the random number generator and the arrays for rooms and corridors.
     */
    public MapGenerator() {
        random = new Random();
        rooms = new Array<>();
        corridors = new Array<>();
    }

    /**
     * Clears the map by clearing the rooms and corridors arrays.
     */
    public void clearMap() {
        rooms.clear();
        corridors.clear();
    }

    /**
     * Initializes the textures for the map.
     */
    public void initializeTextures() {
        WALL_CELLS[0] = new WallCell(Assets.get(MAP_TILE_SET), 80, 16); // General Wall
        WALL_CELLS[1] = new WallCell(Assets.get(MAP_TILE_SET), 128, 112); // Inside wall
    }

    /**
     * Generates a procedural map with the specified width, height, and number of rooms.
     * Adds the generated map to the specified TiledMap object.
     *
     * @param width     the general width of the map
     * @param height    the general height of the map
     * @param numRooms  the number of rooms in the map
     * @param map       the TiledMap object to add the generated map to
     */
    public void generateProceduralMap(int width, int height, int numRooms, TiledMap map) {
        TiledMapTileLayer layer = createBaseLayer(width, height);
        generateRooms(layer, width, height, numRooms);
        connectRooms(layer);
        setWallTiles(layer);

        map.getLayers().add(generateBackground(width, height));
        map.getLayers().add(layer);
    }

    /**
     * Creates a base layer for the map with the specified width and height.
     * @param width     the width of the layer
     * @param height    the height of the layer
     * @return the base layer
     */
    private TiledMapTileLayer createBaseLayer(int width, int height) {
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, Constants.TILE_SIZE, Constants.TILE_SIZE);
        layer.setName("walls");
        return layer;
    }

    /**
     * Generates rooms for the map, carves them and then adds them to the rooms array.
     *
     * @param layer     the TiledMapTileLayer to add the rooms to
     * @param mapWidth  the width of the map
     * @param mapHeight the height of the map
     * @param numRooms  the number of rooms to generate
     */
    private void generateRooms(TiledMapTileLayer layer, int mapWidth, int mapHeight, int numRooms) {
        for (int i = 0; i < numRooms; i++) {
            Rectangle room = generateRandomRoom(mapWidth, mapHeight);
            carveRoom(layer, room);
            rooms.add(room);
        }
    }

    /**
     * Generates a random room within the map.
     *
     * @param mapWidth  the width of the map
     * @param mapHeight the height of the map
     * @return a Rectangle representing the room
     */
    private Rectangle generateRandomRoom(int mapWidth, int mapHeight) {
        int minRoomWidth = Constants.MIN_ROOM_WIDTH;
        int minRoomHeight = Constants.MIN_ROOM_HEIGHT;
        int maxRoomWidth = Constants.MAX_ROOM_WIDTH;
        int maxRoomHeight = Constants.MAX_ROOM_HEIGHT;

        int roomWidth = random.nextInt(maxRoomWidth - minRoomWidth + 1) + minRoomWidth;
        int roomHeight = random.nextInt(maxRoomHeight - minRoomHeight + 1) + minRoomHeight;
        int roomX = random.nextInt(mapWidth - roomWidth - 1) + 1;
        int roomY = random.nextInt(mapHeight - roomHeight - 1) + 1;

        return new Rectangle(roomX, roomY, roomWidth, roomHeight);
    }

    /**
     * Carves out the specified room in the map.
     *
     * @param layer the TiledMapTileLayer to carve the room in
     * @param room  the Rectangle representing the room to carve
     */
    private void carveRoom(TiledMapTileLayer layer, Rectangle room) {
        for (int x = (int) room.x; x < room.x + room.width; x++)
            for (int y = (int) room.y; y < room.y + room.height; y++)
                layer.setCell(x, y, null);
    }

    /**
     * Connects all the rooms in the map with corridors.
     *
     * @param layer the TiledMapTileLayer to add the corridors to
     */
    private void connectRooms(TiledMapTileLayer layer) {
        // Shuffle the rooms for randomness
        rooms.shuffle();

        for (int i = 0; i < rooms.size - 1; i++) {
            Rectangle room1 = rooms.get(i);
            Rectangle room2 = rooms.get(i + 1); // Connect each room to the next one
            carveCorridorBetweenRooms(layer, room1, room2);
        }
    }

    /**
     * Carves a corridor between two rooms.
     *
     * @param layer the TiledMapTileLayer to add the corridor to
     * @param room1 the first room
     * @param room2 the second room
     */
    private void carveCorridorBetweenRooms(TiledMapTileLayer layer, Rectangle room1, Rectangle room2) {
        int x1 = (int) room1.x;
        int y1 = (int) room1.y;
        int w1 = (int) room1.width;
        int h1 = (int) room1.height;

        int x2 = (int) room2.x;
        int y2 = (int) room2.y;
        int w2 = (int) room2.width;
        int h2 = (int) room2.height;

        // Get the center points of the rooms
        int cx1 = x1 + w1 / 2;
        int cy1 = y1 + h1 / 2;
        int cx2 = x2 + w2 / 2;
        int cy2 = y2 + h2 / 2;

        // Generate a random corridor path
        List<Vector2> path = generateCorridorPath(new Vector2(cx1, cy1), new Vector2(cx2, cy2));

        // Carve the corridor along the path
        for (int i = 0; i < path.size() - 1; i++) {
            Vector2 start = path.get(i);
            Vector2 end = path.get(i + 1);
            carveCorridor(layer, (int) start.x, (int) start.y, (int) end.x, (int) end.y);
        }
    }

    /**
     * Generates a path for a corridor from a start point to an end point.
     *
     * @param start the start point of the corridor
     * @param end   the end point of the corridor
     * @return a List of Vector2 points representing the path
     */
    private List<Vector2> generateCorridorPath(Vector2 start, Vector2 end) {
        List<Vector2> path = new ArrayList<>();
        path.add(start);

        Vector2 current = new Vector2(start);
        Vector2 previous = null;

        while (!current.equals(end)) {
            Vector2 direction = getRandomDirection(end, current, previous);
            previous = current;
            current.add(direction);
            path.add(new Vector2(current));
        }

        return path;
    }

    /**
     * Gets a random direction towards the end point from the current point.
     *
     * @param end      the end point
     * @param current  the current point
     * @param previous the previous point
     * @return a Vector2 representing the direction
     */
    private Vector2 getRandomDirection(Vector2 end, Vector2 current, Vector2 previous) {
        List<Vector2> directions = new ArrayList<>();
        Vector2 toEnd = new Vector2(end.x - current.x, end.y - current.y);

        if (toEnd.x > 0 && (previous == null || !previous.equals(new Vector2(current.x - 1, current.y)))) {
            directions.add(new Vector2(1, 0));
        }
        if (toEnd.x < 0 && (previous == null || !previous.equals(new Vector2(current.x + 1, current.y)))) {
            directions.add(new Vector2(-1, 0));
        }
        if (toEnd.y > 0 && (previous == null || !previous.equals(new Vector2(current.x, current.y - 1)))) {
            directions.add(new Vector2(0, 1));
        }
        if (toEnd.y < 0 && (previous == null || !previous.equals(new Vector2(current.x, current.y + 1)))) {
            directions.add(new Vector2(0, -1));
        }

        if (directions.isEmpty()) {
            return toEnd.nor(); // If we're stuck, move directly towards the end point :(
        }

        return directions.get(random.nextInt(directions.size()));
    }

    /**
     * Carves a corridor between two points on the map.
     *
     * @param layer the TiledMapTileLayer to add the corridor to
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     */
    private void carveCorridor(TiledMapTileLayer layer, int x1, int y1, int x2, int y2) {
        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);
        int startY = Math.min(y1, y2);
        int endY = Math.max(y1, y2);

        for (int x = startX; x <= endX; x++)
            for (int y = startY; y <= endY; y++)
                if (!isCellInsideAnyRoom(x, y))
                    layer.setCell(x, y, null);

        int corridorWidth = endX - startX + 1;
        int corridorHeight = endY - startY + 1;
        Rectangle corridor = new Rectangle(startX, startY, corridorWidth, corridorHeight);
        corridors.add(corridor);
    }

    /**
     * Sets the wall tiles on the map.
     *
     * @param layer the TiledMapTileLayer to add the wall tiles to
     */
    private void setWallTiles(TiledMapTileLayer layer) {
        for (int x = 0; x < layer.getWidth(); x++)
            for (int y = 0; y < layer.getHeight(); y++)
                if (!isCellInsideAnyRoom(x, y))
                    setWallTileBasedOnDirection(layer, x, y);
    }

    /**
     * Sets a wall tile at the specified position based on its direction. Right
     * now it's only one type of wall, but it could be expanded to include a left wall
     * , right wall, and so on.
     *
     * @param layer the TiledMapTileLayer to add the wall tile to
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
    private void setWallTileBasedOnDirection(TiledMapTileLayer layer, int x, int y) {
        if (y >= layer.getHeight() || y < 0 || x >= layer.getWidth() || x < 0) return;

        boolean northInRoom = isCellInsideAnyRoom(x, y + 1);
        boolean southInRoom = isCellInsideAnyRoom(x, y - 1);
        boolean eastInRoom = isCellInsideAnyRoom(x + 1, y);
        boolean westInRoom = isCellInsideAnyRoom(x - 1, y);

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

        if (!northInRoom && !southInRoom && !eastInRoom && !westInRoom) {
            cell.setTile(WALL_CELLS[1].getTile());
        } else {
            cell.setTile(WALL_CELLS[0].getTile());
        }

        layer.setCell(x, y, cell);
    }

    /**
     * Checks if a cell at the specified position is inside any room.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     * @return true if the cell is inside any room, false otherwise
     */
    boolean isCellInsideAnyRoom(int x, int y) {
        for (Rectangle room : rooms) {
            if (x >= room.x && x < room.x + room.width && y >= room.y && y < room.y + room.height) {
                return true;
            }
        }
        for (Rectangle corridor : corridors) {
            if (x >= corridor.x && x < corridor.x + corridor.width && y >= corridor.y && y < corridor.y + corridor.height) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a random position inside a room.
     *
     * @return a Vector2 representing the position
     * @throws IllegalStateException if no rooms have been generated yet
     */
    public Vector2 generateRandomPositionInRoom() {
        if (rooms.isEmpty())
            throw new IllegalStateException("No rooms have been generated yet.");

        Rectangle randomRoom = rooms.random(); // Choose a random room from the list
        int x = (int) (randomRoom.x + random.nextInt((int) randomRoom.width));
        int y = (int) (randomRoom.y + random.nextInt((int) randomRoom.height));

        // Ensure the position is within the room's bounds
        x = Math.max(x, (int) randomRoom.x);
        x = Math.min(x, (int) (randomRoom.x + randomRoom.width - 1));
        y = Math.max(y, (int) randomRoom.y);
        y = Math.min(y, (int) (randomRoom.y + randomRoom.height - 1));

        return new Vector2(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE); // Convert to world coordinates
    }

    /**
     * Generates a background layer for the map with the specified width and height.
     *
     * @param width the width of the layer
     * @param height the height of the layer
     * @return the background layer
     */
    private TiledMapTileLayer generateBackground(int width, int height) {
        TextureRegion backgroundRegion = new TextureRegion(Assets.get(MAP_TILE_SET), 144, 112, Constants.TILE_SIZE, Constants.TILE_SIZE);

        TiledMapTileLayer backgroundLayer = new TiledMapTileLayer(width, height, Constants.TILE_SIZE, Constants.TILE_SIZE);
        backgroundLayer.setName("background");

        TextureRegion backgroundTileRegion = new TextureRegion(backgroundRegion);
        TiledMapTileLayer.Cell backgroundCell = new TiledMapTileLayer.Cell();
        backgroundCell.setTile(new StaticTiledMapTile(backgroundRegion));

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                backgroundLayer.setCell(x, y, backgroundCell);
            }
        }

        return backgroundLayer;
    }

    /**
     * Gets all wall tiles in the specified layer.
     *
     * @param layer the TiledMapTileLayer to get the wall tiles from
     * @return a List of Rectangles representing the walls
     */
    public List<Rectangle> getWallTiles(TiledMapTileLayer layer) {
        List<Rectangle> wallTiles = new ArrayList<>();
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                if (layer.getCell(x, y) != null) {
                    wallTiles.add(new Rectangle(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE));
                }
            }
        }
        return wallTiles;
    }

}