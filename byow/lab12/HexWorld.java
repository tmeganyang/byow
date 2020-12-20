package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;
    private static final int HCENTER = 30;

    public static void addHexagon(int row, int col, int s, TETile[][] world, TETile tile) {
        int colEnd = col + s + 2 * (s - 1);
        int rowStartUpper = row + s;
        int rowStartLower = row + s - 1;

        int offset = 0;
        for (int r = rowStartUpper; r < rowStartUpper + s; r++) {
            for (int c = col + offset; c < colEnd - offset; c++) {
                world[c][r] = tile;
            }
            offset++;
        }
        offset = 0;
        for (int r = rowStartLower; r > rowStartLower - s; r--) {
            for (int c = col + offset; c < colEnd - offset; c++) {
                world[c][r] = tile;
            }
            offset++;
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // add hexagons
        addHexagon(0, HCENTER, 3, world, Tileset.FLOWER);
        addHexagon(3, HCENTER - 5, 3, world, Tileset.WALL);
        addHexagon(3, HCENTER + 5, 3, world, Tileset.WALL);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
