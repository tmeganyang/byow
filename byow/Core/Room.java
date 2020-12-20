package byow.Core;
import java.util.List;
import java.util.ArrayList;

public class Room {
    private Position pos;
    private int width, height;
    private List<Position> span, walls;
    public Room(Position startPosition, int width, int height) {
        this.pos = startPosition;
        this.width = width;
        this.height = height;

        this.span = new ArrayList<>();
        this.walls = new ArrayList<>();

        int rowEnd = getY() + getHeight();
        int colEnd = getX() + getWidth();
        for (int row = getY() - 1; row < rowEnd + 1; row++) {
            for (int col = getX() - 1; col < colEnd + 1; col++) {
                if (row == getY() - 1 || row == rowEnd || col == getX() - 1 || col == colEnd) {
                    walls.add(new Position(col, row));
                } else {
                    span.add(new Position(col, row));
                }
            }
        }
    }

    public List<Position> getSpan() {
        return this.span;
    }
    public List<Position> getWalls() {
        return this.walls;
    }
    public int getX() {
        return this.pos.getX();
    }
    public int getY() {
        return this.pos.getY();
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
}
