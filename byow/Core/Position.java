package byow.Core;

public class Position {
    private int x, y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public static int compareX(Position a, Position b) {
        return a.getX() - b.getX();
    }
    public static int compareY(Position a, Position b) {
        return a.getY() - b.getY();
    }
}
