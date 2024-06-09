package mcr.gdx.dungeon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import mcr.gdx.dungeon.elements.WallCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator implements Disposable {
    private static final String MAP_TILE_SET = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png";
    private static final WallCell[] WALL_CELLS = new WallCell[2];

    private final Random random;
    private final Array<Rectangle> rooms;
    private final Array<Rectangle> corridors;
    private Texture backgroundTexture;
    private Texture tilesetTexture;

    public Vector2 validPlayerPos;

    public MapGenerator() {
        random = new Random();
        rooms = new Array<>();
        corridors = new Array<>();
    }

    public void resetMap() {
        rooms.clear();
        corridors.clear();
    }

    public void initializeTextures() {
        backgroundTexture = new Texture(MAP_TILE_SET);
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        tilesetTexture = new Texture(MAP_TILE_SET);
        tilesetTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        WALL_CELLS[0] = new WallCell(tilesetTexture, 80, 16); // General Wall
        WALL_CELLS[1] = new WallCell(tilesetTexture, 128, 112); // Inside wall
    }

    public void generateProceduralMap(int width, int height, int numRooms, TiledMap map) {
        TiledMapTileLayer layer = createBaseLayer(width, height);
        generateRooms(layer, width, height, numRooms);
        connectRooms(layer);
        setWallTiles(layer);
        generateValidPlayerPosition();

        map.getLayers().add(generateBackground(width, height));
        map.getLayers().add(layer);
    }

    private TiledMapTileLayer createBaseLayer(int width, int height) {
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, Constants.TILE_SIZE, Constants.TILE_SIZE);
        layer.setName("walls");
        return layer;
    }

    private void generateRooms(TiledMapTileLayer layer, int mapWidth, int mapHeight, int numRooms) {
        for (int i = 0; i < numRooms; i++) {
            Rectangle room = generateRandomRoom(mapWidth, mapHeight);

            carveRoom(layer, room);
            rooms.add(room);
        }
    }

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


    private void carveRoom(TiledMapTileLayer layer, Rectangle room) {
        for (int x = (int) room.x; x < room.x + room.width; x++)
            for (int y = (int) room.y; y < room.y + room.height; y++)
                layer.setCell(x, y, null);
    }

    private void connectRooms(TiledMapTileLayer layer) {
        // Shuffle the rooms for randomness
        rooms.shuffle();

        for (int i = 0; i < rooms.size - 1; i++) {
            Rectangle room1 = rooms.get(i);
            Rectangle room2 = rooms.get(i + 1); // Connect each room to the next one

            carveCorridorBetweenRooms(layer, room1, room2);
        }
    }


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

    private void carveCorridorBetweenRooms(TiledMapTileLayer layer, Rectangle room1, Rectangle room2) {
        int x1 = (int) room1.x;
        int y1 = (int) room1.y;
        int w1 = (int) room1.width;
        int h1 = (int) room1.height;

        int x2 = (int) room2.x;
        int y2 = (int) room2.y;
        int w2 = (int) room2.width;
        int h2 = (int) room2.height;

        // Determine the closest points between the two rooms
        int cx1 = x1 + w1 / 2;
        int cy1 = y1 + h1 / 2;
        int cx2 = x2 + w2 / 2;
        int cy2 = y2 + h2 / 2;

        // Carve the first segment
        boolean isHorizontalFirst;
        if (cx1 < cx2) {
            carveHorizontalCorridor(layer, cx1, cy1, x2, cy1);
            isHorizontalFirst = true;
        } else {
            carveVerticalCorridor(layer, cx1, cy1, cx1, cy2);
            isHorizontalFirst = false;
        }

        // Carve the second segment (L-shape)
        if (isHorizontalFirst)
            carveVerticalCorridor(layer, x2, cy1, x2, cy2);
        else
            carveHorizontalCorridor(layer, x1, cy2, cx2, cy2);

    }

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

    private void carveHorizontalCorridor(TiledMapTileLayer layer, int x1, int y1, int x2, int y2) {
        carveCorridor(layer, x1, y1, x2, y1);
    }

    private void carveVerticalCorridor(TiledMapTileLayer layer, int x1, int y1, int x2, int y2) {
        carveCorridor(layer, x1, y1, x1, y2);
    }

    private void setWallTiles(TiledMapTileLayer layer) {
        for (int x = 0; x < layer.getWidth(); x++)
            for (int y = 0; y < layer.getHeight(); y++)
                if (!isCellInsideAnyRoom(x, y))
                    setWallTileBasedOnDirection(layer, x, y);
    }

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

    private void generateValidPlayerPosition() {
        if (rooms.isEmpty()) {
            return;
        }

        Rectangle room = rooms.random();
        float roomCenterX = room.x + (room.width / 2f);
        float roomCenterY = room.y + (room.height / 2f);

        if (room.width > 0 && room.height > 0) {
            float randomOffsetX = random.nextInt((int) room.width / 2) - (room.width / 4f);
            float randomOffsetY = random.nextInt((int) room.height / 2) - (room.height / 4f);
            float x = roomCenterX + randomOffsetX;
            float y = roomCenterY + randomOffsetY;
            validPlayerPos = new Vector2(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
        } else {
            validPlayerPos = new Vector2(roomCenterX * Constants.TILE_SIZE, roomCenterY * Constants.TILE_SIZE);
        }
    }

    private TiledMapTileLayer generateBackground(int width, int height) {
        TextureRegion backgroundRegion = new TextureRegion(backgroundTexture, 144, 112, Constants.TILE_SIZE, Constants.TILE_SIZE);

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

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        tilesetTexture.dispose();
    }
}