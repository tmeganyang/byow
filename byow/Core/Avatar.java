package byow.Core;
import byow.TileEngine.TETile;

public class Avatar {
    private Position pos;
    private TETile tile;
    public Avatar(Position pos, TETile tile) {
        this.pos = pos;
        this.tile = tile;
    }

    public int getX() {
        return this.pos.getX();
    }
    public int getY() {
        return this.pos.getY();
    }
    public void setX(int x) {
        this.pos = new Position(x, getY());
    }
    public void setY(int y) {
        this.pos = new Position(getX(), y);
    }
    public Position getPosition() {
        return this.pos;
    }
    public TETile getTile() {
        return this.tile;
    }
}
