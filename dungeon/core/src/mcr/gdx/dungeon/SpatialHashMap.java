package mcr.gdx.dungeon;

import com.badlogic.gdx.math.Rectangle;

import java.util.HashSet;
import java.util.Set;

/**
 * The SpatialHashMap class is used for efficient collision detection in the game.
 * It divides the game world into a grid of cells and keeps track of the entities in each cell.
 * This allows for quickly finding potential colliders for a given entity by only checking the entities in the same cell.
 * This class was made with the aid of an Artificial Intelligence.
 *
 * @version 1.1
 */
public class SpatialHashMap {
    private static final int CELL_SIZE = Constants.TILE_SIZE;
    private final Set<Rectangle>[][] grid;
    private final int numCellsX;
    private final int numCellsY;

    /**
     * Constructs a new SpatialHashMap with the specified width and height.
     * The width and height are divided by the cell size to determine the number of cells in the x and y directions.
     *
     * @param width  the width of the game world
     * @param height the height of the game world
     */
    public SpatialHashMap(int width, int height) {
        numCellsX = (width + CELL_SIZE - 1) / CELL_SIZE;
        numCellsY = (height + CELL_SIZE - 1) / CELL_SIZE;
        grid = new HashSet[numCellsX][numCellsY];
    }

    /**
     * Inserts a rectangle into the spatial hash map.
     * The rectangle is added to all cells that it overlaps.
     *
     * @param rect the rectangle to insert
     */
    public void insert(Rectangle rect) {
        int minX = getMinCellX(rect.x);
        int maxX = getMaxCellX(rect.x + rect.width);
        int minY = getMinCellY(rect.y);
        int maxY = getMaxCellY(rect.y + rect.height);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                addRectToCell(rect, x, y);
            }
        }
    }

    /**
     * Returns a set of potential colliders for the specified rectangle.
     * The potential colliders are all rectangles in the same cells as the specified rectangle.
     *
     * @param rect the rectangle to find potential colliders for
     * @return a set of potential colliders
     */
    public Set<Rectangle> getPotentialColliders(Rectangle rect) {
        Set<Rectangle> potentialColliders = new HashSet<>();
        int minX = getMinCellX(rect.x);
        int maxX = getMaxCellX(rect.x + rect.width);
        int minY = getMinCellY(rect.y);
        int maxY = getMaxCellY(rect.y + rect.height);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (grid[x][y] != null) {
                    potentialColliders.addAll(grid[x][y]);
                }
            }
        }
        return potentialColliders;
    }

    /**
     * Clears all rectangles from the spatial hash map.
     */
    public void clear() {
        for (int x = 0; x < numCellsX; x++) {
            for (int y = 0; y < numCellsY; y++) {
                if (grid[x][y] != null) {
                    grid[x][y].clear();
                }
            }
        }
    }

    /**
     * Returns the minimum cell index for the specified x coordinate.
     * @param x the x coordinate
     * @return the minimum cell index
     */
    private int getMinCellX(float x) {
        return (int) (x / CELL_SIZE);
    }

    /**
     * Returns the maximum cell index for the specified x coordinate.
     * @param x the x coordinate
     * @return the maximum cell index
     */
    private int getMaxCellX(float x) {
        return (int) ((x - 1) / CELL_SIZE);
    }

    /**
     * Returns the minimum cell index for the specified y coordinate.
     * @param y the y coordinate
     * @return the minimum cell index
     */
    private int getMinCellY(float y) {
        return (int) (y / CELL_SIZE);
    }

    /**
     * Returns the maximum cell index for the specified y coordinate.
     * @param y the y coordinate
     * @return the maximum cell index
     */
    private int getMaxCellY(float y) {
        return (int) ((y - 1) / CELL_SIZE);
    }

    /**
     * Adds a rectangle to the specified cell.
     * @param rect the rectangle to add
     * @param x the x index of the cell
     * @param y the y index of the cell
     */
    private void addRectToCell(Rectangle rect, int x, int y) {
        if (grid[x][y] == null) {
            grid[x][y] = new HashSet<>();
        }
        grid[x][y].add(rect);
    }
}