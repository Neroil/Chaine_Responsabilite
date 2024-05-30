package mcr.gdx.dungeon;

import com.badlogic.gdx.math.Rectangle;
import java.util.HashSet;
import java.util.Set;

public class SpatialHashMap {
    private static final int CELL_SIZE = Constants.TILE_SIZE;
    private final Set<Rectangle>[][] grid;

    public SpatialHashMap(int width, int height) {
        int numCellsX = (width + CELL_SIZE - 1) / CELL_SIZE;
        int numCellsY = (height + CELL_SIZE - 1) / CELL_SIZE;
        grid = new HashSet[numCellsX][numCellsY];
    }

    public void insert(Rectangle rect) {
        int minX = (int) (rect.x / CELL_SIZE);
        int maxX = (int) ((rect.x + rect.width - 1) / CELL_SIZE);
        int minY = (int) (rect.y / CELL_SIZE);
        int maxY = (int) ((rect.y + rect.height - 1) / CELL_SIZE);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (grid[x][y] == null) {
                    grid[x][y] = new HashSet<Rectangle>();
                }
                grid[x][y].add(rect);
            }
        }
    }

    public Set<Rectangle> getPotentialColliders(Rectangle rect) {
        Set<Rectangle> potentialColliders = new HashSet<Rectangle>();
        int minX = (int) (rect.x / CELL_SIZE);
        int maxX = (int) ((rect.x + rect.width - 1) / CELL_SIZE);
        int minY = (int) (rect.y / CELL_SIZE);
        int maxY = (int) ((rect.y + rect.height - 1) / CELL_SIZE);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (grid[x][y] != null) {
                    potentialColliders.addAll(grid[x][y]);
                }
            }
        }
        return potentialColliders;
    }
}