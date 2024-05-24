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

    private final ArrayList<Rectangle> rooms = new ArrayList<>();
    private final Random random = new Random();
    String mapTileSet = "2D Pixel Dungeon Asset Pack/character and tileset/Dungeon_Tileset.png";
    public Vector2 validPlayerPos;

    private TiledMapTileLayer generateBackground(){
        // Generate the background
        Texture backgroundTexture = new Texture(mapTileSet);
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion backgroundRegion = new TextureRegion(backgroundTexture, 0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);

        int backgroundWidth = 25;
        int backgroundHeight = 25;

        //Background layer
        TiledMapTileLayer backgroundLayer = new TiledMapTileLayer(backgroundWidth, backgroundHeight, Constants.TILE_SIZE, Constants.TILE_SIZE);
        backgroundLayer.setName("background");
        // Create a TextureRegion for the background tiles
        TextureRegion backgroundTileRegion = new TextureRegion(backgroundRegion);
        // Create a TiledMapTileLayer.Cell for the background tiles
        TiledMapTileLayer.Cell backgroundCell = new TiledMapTileLayer.Cell();
        backgroundCell.setTile(new StaticTiledMapTile(backgroundRegion));

        // Fill the background layer with the background tiles
        for (int x = 0; x < 25; x++) {
            for (int y = 0; y < 25; y++) {
                backgroundLayer.setCell(x, y, backgroundCell);
            }
        }

        return backgroundLayer;
    }

    public void generateProceduralMap(int width, int height, int numRooms, TiledMap map, TextureRegion tilesetTexture) {

        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, Constants.TILE_SIZE, Constants.TILE_SIZE);
        layer.setName("walls");
        TextureRegion tileRegion = new TextureRegion(tilesetTexture, 32, 32, Constants.TILE_SIZE, Constants.TILE_SIZE);
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
            carveRoom(layer, room);
            rooms.add(room);
        }

        // Generate corridors between rooms
        for (int i = 0; i < rooms.size() - 1; i++) {
            Rectangle room1 = rooms.get(i);
            Rectangle room2 = getClosestRoom(rooms, room1);
            carveCorridorBetweenRooms(layer, room1, room2);
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


        return new Rectangle(roomX, roomY, roomWidth, roomHeight);
    }

    private void carveRoom(TiledMapTileLayer layer, Rectangle room) {
        for (int x = (int) room.x; x < room.x + room.width; x++) {
            for (int y = (int) room.y; y < room.y + room.height; y++) {
                layer.setCell(x, y, null); // Set cell to floor tile
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

            if (Math.abs(x - room2Center.x) > Math.abs(y - room2Center.y)) {
                x += dx;
            } else {
                y += dy;
            }
        }
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

    public void generateValidPlayerPosition() {
        Rectangle room = rooms.get(random.nextInt(rooms.size()));
        float roomCenterX = room.x + (room.width / 2f);
        float roomCenterY = room.y + (room.height / 2f);
        float randomOffsetX = random.nextInt((int) room.width / 2) - (room.width / 4f);
        float randomOffsetY = random.nextInt((int) room.height / 2) - (room.height / 4f);
        float x = roomCenterX + randomOffsetX;
        float y = roomCenterY + randomOffsetY;
        validPlayerPos = new Vector2(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);
    }
}
