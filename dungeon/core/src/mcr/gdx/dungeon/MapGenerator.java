package mcr.gdx.dungeon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MapGenerator {

    private final ArrayList<Rectangle> rooms = new ArrayList<Rectangle>();
    private final Random random = new Random();
    private final String mapTileSet = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png";
    private Texture backgroundTexture; // Declare Texture object as a field
    private Texture tilesetTexture;
    public Vector2 validPlayerPos;

    private TiledMapTileLayer generateBackground() {
        // Generate the background
        backgroundTexture = new Texture(mapTileSet); // Load the texture
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion backgroundRegion = new TextureRegion(backgroundTexture, 144, 112, Constants.TILE_SIZE, Constants.TILE_SIZE);

        int backgroundWidth = 25;
        int backgroundHeight = 25;

        // Background layer
        TiledMapTileLayer backgroundLayer = new TiledMapTileLayer(backgroundWidth, backgroundHeight, Constants.TILE_SIZE, Constants.TILE_SIZE);
        backgroundLayer.setName("background");
        // Create a TextureRegion for the background tiles
        TextureRegion backgroundTileRegion = new TextureRegion(backgroundRegion);
        // Create a TiledMapTileLayer.Cell for the background tiles
        TiledMapTileLayer.Cell backgroundCell = new TiledMapTileLayer.Cell();
        backgroundCell.setTile(new StaticTiledMapTile(backgroundRegion));

        // Fill the background layer with the background tiles
        for (int x = 0; x < backgroundWidth; x++) {
            for (int y = 0; y < backgroundHeight; y++) {
                backgroundLayer.setCell(x, y, backgroundCell);
            }
        }


        return backgroundLayer;
    }

    public void generateProceduralMap(int width, int height, int numRooms, TiledMap map) {

        tilesetTexture = new Texture(mapTileSet); // Load the texture
        tilesetTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, Constants.TILE_SIZE, Constants.TILE_SIZE);
        layer.setName("walls");
        TextureRegion tileRegion = new TextureRegion(tilesetTexture, 128, 112, Constants.TILE_SIZE, Constants.TILE_SIZE);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(new StaticTiledMapTile(tileRegion));

        // Fill the layer with wall tiles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                layer.setCell(x, y, cell);
            }
        }
        // Generate rooms
        for (int i = 0; i < numRooms; i++) {
            Rectangle room = generateRandomRoom(width, height);
            if (room != null) {
                carveRoom(layer, room);
                rooms.add(room);
            }
        }
        // Generate corridors between rooms
        for (int i = 0; i < rooms.size() - 1; i++) {
            Rectangle room1 = rooms.get(i);
            Rectangle room2 = getClosestRoom(rooms, room1);
            if (room2 != null) {
                carveCorridorBetweenRooms(layer, room1, room2);
            }
        }

        // Set wall tiles based on direction after all rooms and corridors have been carved
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!isCellInsideAnyRoom(x, y)) { // Only check floor tiles
                    setWallTilesBasedOnDirection(layer, x, y);
                }
            }
        }

        generateValidPlayerPosition();


        map.getLayers().add(generateBackground());

        // Add the layer to the map
        map.getLayers().add(layer);
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

        // Check if the room extends beyond the map boundaries
        if (roomX + roomWidth >= mapWidth || roomY + roomHeight >= mapHeight) {
            return null; // Return null to skip this room
        }

        return new Rectangle(roomX, roomY, roomWidth, roomHeight);
    }

    private void carveRoom(TiledMapTileLayer layer, Rectangle room) {
        for (int x = (int) room.x; x < room.x + room.width; x++) {
            for (int y = (int) room.y; y < room.y + room.height; y++) {
                layer.setCell(x, y, null); // Set cell to floor tile
                //setWallTilesBasedOnDirection(layer, x, y);
            }
        }
    }

    private Rectangle getClosestRoom(ArrayList<Rectangle> rooms, Rectangle room) {
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

        while (x != (int) room2Center.x || y != (int) room2Center.y) {
            layer.setCell(x, y, null); // Set cell to floor tile
            //setWallTilesBasedOnDirection(layer, x, y);

            if (Math.abs(x - room2Center.x) > Math.abs(y - room2Center.y)) {
                x += dx;
            } else {
                y += dy;
            }
        }
    }

    private void setWallTilesBasedOnDirection(TiledMapTileLayer layer, int x, int y) {
        TextureRegion northWallTileRegion = new TextureRegion(tilesetTexture, 16, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
        TextureRegion southWallTileRegion = new TextureRegion(tilesetTexture, 32, 64, Constants.TILE_SIZE, Constants.TILE_SIZE);
        TextureRegion eastWallTileRegion = new TextureRegion(tilesetTexture, 0, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);
        TextureRegion westWallTileRegion = new TextureRegion(tilesetTexture, 80, 16, Constants.TILE_SIZE, Constants.TILE_SIZE);

        TiledMapTileLayer.Cell cell = layer.getCell(x, y);
        if (cell == null) {
            return;
        }

        if (y >= layer.getHeight() || y < 0 || x >= layer.getWidth() || x < 0) return;

        boolean northInRoom = isCellInsideAnyRoom(x, y + 1);
        boolean southInRoom = isCellInsideAnyRoom(x, y - 1);
        boolean eastInRoom = isCellInsideAnyRoom(x + 1, y);
        boolean westInRoom = isCellInsideAnyRoom(x - 1, y);
        StaticTiledMapTile northWallTile = new StaticTiledMapTile(northWallTileRegion);
        StaticTiledMapTile southWallTile = new StaticTiledMapTile(southWallTileRegion);
        StaticTiledMapTile eastWallTile = new StaticTiledMapTile(eastWallTileRegion);
        StaticTiledMapTile westWallTile = new StaticTiledMapTile(westWallTileRegion);

        if (!northInRoom && !southInRoom && !eastInRoom && !westInRoom) {
            // All directions are in rooms, set a single wall tile (choose any direction)
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { northWallTile });
        } else if (northInRoom && southInRoom && eastInRoom && westInRoom) {
            // All directions are rooms, don't set a wall tile
            return;
        } else if (!northInRoom && !southInRoom && !eastInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { westWallTile });
        } else if (!northInRoom && !southInRoom && !westInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { eastWallTile });
        } else if (!northInRoom && !eastInRoom && !westInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { southWallTile });
        } else if (!southInRoom && !eastInRoom && !westInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { northWallTile });
        } else if (!northInRoom && !southInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { eastWallTile });
        } else if (!eastInRoom && !westInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { northWallTile });
        } else if (!northInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { northWallTile });
        } else if (!southInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { southWallTile });
        } else if (!eastInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { eastWallTile });
        } else if (!westInRoom) {
            cell.getTile().setAnimatedTiles(new StaticTiledMapTile[] { westWallTile });
        }
    }

    private boolean isCellInsideAnyRoom(int x, int y) {
        for (Rectangle room : rooms) {
            if (room.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public List<Rectangle> getWallTiles(TiledMapTileLayer layer) {
        List<Rectangle> wallTiles = new ArrayList<Rectangle>();
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                if (layer.getCell(x, y) != null) {
                    wallTiles.add(new Rectangle(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE));
                }
            }
        }
        return wallTiles;
    }

    public void generateValidPlayerPosition() {
        if (rooms.isEmpty()) {
            return; // Return if there are no rooms
        }

        Rectangle room = rooms.get(random.nextInt(rooms.size()));
        float roomCenterX = room.x + (room.width / 2f);
        float roomCenterY = room.y + (room.height / 2f);

// Check if the room's width and height are non-zero before division
        if (room.width > 0 && room.height > 0) {
            float randomOffsetX = random.nextInt((int) room.width / 2) - (room.width / 4f);
            float randomOffsetY = random.nextInt((int) room.height / 2) - (room.height / 4f);
            float x = roomCenterX + randomOffsetX;
            float y = roomCenterY + randomOffsetY;
            validPlayerPos = new Vector2(x * Constants.TILE_SIZE , y * Constants.TILE_SIZE);
        } else {
            // Handle the case where the room's width or height is zero
            validPlayerPos = new Vector2(roomCenterX * Constants.TILE_SIZE, roomCenterY * Constants.TILE_SIZE);
        }
    }

    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose(); // Dispose the background texture
        }
    }
}