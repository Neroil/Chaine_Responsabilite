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
    private static final WallCell[] WALL_CELLS = new WallCell[4];

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

    public void initializeTextures() {
        backgroundTexture = new Texture(MAP_TILE_SET);
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        tilesetTexture = new Texture(MAP_TILE_SET);
        tilesetTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        WALL_CELLS[0] = new WallCell(tilesetTexture, 16, 0); // North wall
        WALL_CELLS[1] = new WallCell(tilesetTexture, 32, 64); // South wall
        WALL_CELLS[2] = new WallCell(tilesetTexture, 0, 16); // East wall
        WALL_CELLS[3] = new WallCell(tilesetTexture, 80, 16); // West wall
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

        TextureRegion tileRegion = new TextureRegion(tilesetTexture, 128, 112, Constants.TILE_SIZE, Constants.TILE_SIZE);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(new StaticTiledMapTile(tileRegion));

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                layer.setCell(x, y, cell);
            }
        }

        return layer;
    }

    private void generateRooms(TiledMapTileLayer layer, int mapWidth, int mapHeight, int numRooms) {
        for (int i = 0; i < numRooms; i++) {
            Rectangle room = generateRandomRoom(mapWidth, mapHeight);
            if (room != null) {
                carveRoom(layer, room);
                rooms.add(room);
            }
        }
    }

    private Rectangle generateRandomRoom(int mapWidth, int mapHeight) {
        int minRoomWidth = Constants.MIN_ROOM_WIDTH;
        int minRoomHeight = Constants.MIN_ROOM_HEIGHT;
        int maxRoomWidth = Constants.MAX_ROOM_WIDTH;
        int maxRoomHeight = Constants.MAX_ROOM_HEIGHT;

        Rectangle room = null;
        boolean isValid = false;
        int maxIterations = 1000; // Set a reasonable maximum number of iterations
        int iteration = 0;

        while (!isValid && iteration < maxIterations) {
            int roomWidth = random.nextInt(maxRoomWidth - minRoomWidth + 1) + minRoomWidth;
            int roomHeight = random.nextInt(maxRoomHeight - minRoomHeight + 1) + minRoomHeight;
            int roomX = random.nextInt(mapWidth - roomWidth - 1) + 1;
            int roomY = random.nextInt(mapHeight - roomHeight - 1) + 1;

            room = new Rectangle(roomX, roomY, roomWidth, roomHeight);

            if (room.x + room.width < mapWidth && room.y + room.height < mapHeight) {
                isValid = true;
            }

            iteration++;
        }

        if (!isValid) {
            // Handle the case where a valid room could not be generated
            // You can return null or throw an exception, depending on your requirements
            throw new RuntimeException("Could not generate a valid room after " + maxIterations + " iterations");
        }

        return room;
    }


    private void carveRoom(TiledMapTileLayer layer, Rectangle room) {
        for (int x = (int) room.x; x < room.x + room.width; x++) {
            for (int y = (int) room.y; y < room.y + room.height; y++) {
                layer.setCell(x, y, null);
            }
        }
    }

    private void connectRooms(TiledMapTileLayer layer) {
        for (int i = 0; i < rooms.size - 1; i++) {
            Rectangle room1 = rooms.get(i);
            Rectangle room2 = getClosestRoom(rooms, room1);
            if (room2 != null) {
                carveCorridorBetweenRooms(layer, room1, room2);
            }
        }
    }

    private Rectangle getClosestRoom(Array<Rectangle> rooms, Rectangle room) {
        Rectangle closestRoom = null;
        float minDistance = Float.MAX_VALUE;

        for (Rectangle otherRoom : rooms) {
            if (otherRoom != room) {
                float distance = room.getCenter(new Vector2()).dst(otherRoom.getCenter(new Vector2()));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestRoom = otherRoom;
                }
            }
        }

        return closestRoom;
    }

    private void carveCorridorBetweenRooms(TiledMapTileLayer layer, Rectangle room1, Rectangle room2) {
        Vector2 room1Center = room1.getCenter(new Vector2());
        Vector2 room2Center = room2.getCenter(new Vector2());

        int dx = (int) Math.signum(room2Center.x - room1Center.x);
        int dy = (int) Math.signum(room2Center.y - room1Center.y);

        int x = (int) room1Center.x;
        int y = (int) room1Center.y;

        int corridorStartX = x;
        int corridorStartY = y;

        while (x != (int) room2Center.x || y != (int) room2Center.y) {
            layer.setCell(x, y, null);

            if (Math.abs(x - room2Center.x) > Math.abs(y - room2Center.y)) {
                x += dx;
            } else {
                y += dy;
            }
        }

        int corridorWidth = Math.abs(x - corridorStartX);
        int corridorHeight = Math.abs(y - corridorStartY);
        Rectangle corridor = new Rectangle(corridorStartX, corridorStartY, corridorWidth, corridorHeight);
        corridors.add(corridor);
    }

    private void setWallTiles(TiledMapTileLayer layer) {
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                if (!isCellInsideAnyRoom(x, y)) {
                    setWallTileBasedOnDirection(layer, x, y);
                }
            }
        }
    }

    private void setWallTileBasedOnDirection(TiledMapTileLayer layer, int x, int y) {
        if (y >= layer.getHeight() || y < 0 || x >= layer.getWidth() || x < 0) return;

        boolean northInRoom = isCellInsideAnyRoom(x, y + 1);
        boolean southInRoom = isCellInsideAnyRoom(x, y - 1);
        boolean eastInRoom = isCellInsideAnyRoom(x + 1, y);
        boolean westInRoom = isCellInsideAnyRoom(x - 1, y);

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

        if (!northInRoom && !southInRoom && !eastInRoom && !westInRoom) {
            cell.setTile(WALL_CELLS[0].getTile());
        } else if (northInRoom && southInRoom && eastInRoom && westInRoom) {
            return;
        } else if (!northInRoom && !southInRoom && !eastInRoom) {
            cell.setTile(WALL_CELLS[3].getTile());
        } else if (!northInRoom && !southInRoom && !westInRoom) {
            cell.setTile(WALL_CELLS[2].getTile());
        } else if (!northInRoom && !eastInRoom && !westInRoom) {
            cell.setTile(WALL_CELLS[1].getTile());
        } else if (!southInRoom && !eastInRoom && !westInRoom) {
            cell.setTile(WALL_CELLS[0].getTile());
        } else if (!northInRoom && !southInRoom) {
            cell.setTile(WALL_CELLS[2].getTile());
        } else if (!eastInRoom && !westInRoom) {
            cell.setTile(WALL_CELLS[0].getTile());
        } else if (!northInRoom) {
            cell.setTile(WALL_CELLS[0].getTile());
        } else if (!southInRoom) {
            cell.setTile(WALL_CELLS[1].getTile());
        } else if (!eastInRoom) {
            cell.setTile(WALL_CELLS[2].getTile());
        } else if (!westInRoom) {
            cell.setTile(WALL_CELLS[3].getTile());
        }

        layer.setCell(x, y, cell);
    }

    private boolean isCellInsideAnyRoom(int x, int y) {
        for (Rectangle room : rooms) {
            if (room.contains(x, y)) {
                return true;
            }
        }
        for (Rectangle corridor : corridors) {
            if (corridor.contains(x, y)) {
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
