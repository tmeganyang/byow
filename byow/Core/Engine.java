package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Date;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;

import java.text.SimpleDateFormat;

public class Engine {
    private Random rand;
    private Avatar avatar;
    private List<Room> rooms;
    private String playerMovement, seedString;
    private TETile[][] finalWorldFrame;

    public static final int WIDTH = 90;
    public static final int HEIGHT = 40;

    public Engine() {
        avatar = null;
        rand = null;
        rooms = new ArrayList<>();
        playerMovement = "";
        seedString = "";
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        drawIntroFrame();
        String opt = solicitOption();
        if (opt.equals("n")) {
            String inputString = opt + solicitSeed() + "s";
            startGame(inputString);
        } else if (opt.equals("l")) {
            startGame(opt);
        } else if (opt.equals("r")) {
            replayGame();
        } else if (opt.equals("q")) {
            System.exit(0);
        }
    }

    public void startGame(String input) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + 3, 0, 0);
        interactWithInputString(input);

        boolean active = true;
        while (active) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                active = updateWorldFrame(key);
            }
            drawHUDFrame("PLAY", 6);
            ter.renderFrame(this.finalWorldFrame);
        }
        System.exit(0);
    }
    public void replayGame() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + 3, 0, 0);
        String input = "";
        try {
            FileReader reader = new FileReader("data.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            input = bufferedReader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int divide = input.indexOf("s") + 1;
        String savedMoves = input.substring(divide);
        interactWithInputString(input.substring(0, divide));

        int index = 0;
        String keyLog = "";
        boolean active = true;
        while (active && index < savedMoves.length()) {
            drawHUDFrame("REPLAY", 7);
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                keyLog += key;
                if (key == ' ') {
                    updateWorldFrame(savedMoves.charAt(index));
                    index++;
                }
            }
            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":q")) {
                active = false;
            }
            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":r")) {
                seedString = "";
                playerMovement = "";
                rooms = new ArrayList<>();
                replayGame();
            }
            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":h")) {
                resetWorld();
                interactWithKeyboard();
            }
            ter.renderFrame(this.finalWorldFrame);
        }

        StdDraw.clear();
        while (active) {
            drawHUDFrame("REPLAY OVER", 8);
            ter.renderFrame(this.finalWorldFrame);
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                keyLog += key;
            }
            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":q")) {
                active = false;
            }
            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":r")) {
                seedString = "";
                playerMovement = "";
                rooms = new ArrayList<>();
                replayGame();
            }
            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":h")) {
                resetWorld();
                interactWithKeyboard();
            }
        }
        System.exit(0);
    }

    public void resetWorld() {
        this.rand = null;
        this.avatar = null;
        this.seedString = "";
        this.playerMovement = "";
        this.rooms = new ArrayList<>();
    }

    public void drawIntroFrame() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(WIDTH * 8, HEIGHT * 12, "2D TILE EXPLORATION");
        StdDraw.text(WIDTH * 8, HEIGHT * 5, "NEW GAME (N)");
        StdDraw.text(WIDTH * 8, HEIGHT * 4, "LOAD GAME (L)");
        StdDraw.text(WIDTH * 8, HEIGHT * 3, "REPLAY GAME (R)");
        StdDraw.text(WIDTH * 8, HEIGHT * 2, "QUIT GAME (Q)");
        StdDraw.show();
    }
    public void drawSeedFrame(String seed) {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(WIDTH * 8, HEIGHT * 12, "PLEASE ENTER A SEED");
        StdDraw.text(WIDTH * 8, HEIGHT * 10, "SEED: " + seed);
        StdDraw.show();
    }

    /**
     * @source https://stackabuse.com/how-to-get-current-date-and-time-in-java/
     */
    public void drawHUDFrame(String status, int statusX) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.rectangle(5, HEIGHT + 2, WIDTH, 2);
        StdDraw.text(3, HEIGHT + 2, "STATUS: ");
        StdDraw.text(statusX, HEIGHT + 2, status);

        StdDraw.text(19, HEIGHT + 2, "PLAYER COORDS: ");
        StdDraw.text(25, HEIGHT + 2, "(" + avatar.getX() + ", " + avatar.getY() + ")");

        int mouseX = Math.min((int) StdDraw.mouseX(), WIDTH - 1);
        int mouseY = Math.min((int) StdDraw.mouseY(), HEIGHT - 1);
        TETile mouseTile = this.finalWorldFrame[mouseX][mouseY];
        StdDraw.text(4, HEIGHT + 1, "MOUSE OVER: ");
        StdDraw.text(9, HEIGHT + 1, mouseTile.description());

        StdDraw.text(35.5, HEIGHT + 2, "INSTRUCTIONS: ");
        if (status.equals("REPLAY")) {
            StdDraw.text(48, HEIGHT + 2, "Press [SPACE] to advance one frame");
        } else if (status.equals("REPLAY OVER")) {
            StdDraw.text(46.5, HEIGHT + 2, "Type :r to replay from start");
        } else if (status.equals("PLAY")) {
            StdDraw.text(47, HEIGHT + 2, "Use [W][A][S][D] to move around");
        }
        StdDraw.line(13, HEIGHT, 13, HEIGHT + 3);
        StdDraw.line(29.5, HEIGHT, 29.5, HEIGHT + 3);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        StdDraw.text(WIDTH - 8, HEIGHT + 2, formatter.format(date));
        StdDraw.show();
        StdDraw.pause(50);
    }

    public String solicitOption() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'n' || key == 'l' || key == 'q' || key == 'r') {
                    return Character.toString(key);
                }
            }
        }
    }
    public String solicitSeed() {
        String seed = "";
        char lastChar = 0;
        while (lastChar != 's') {
            drawSeedFrame(seed);
            if (StdDraw.hasNextKeyTyped()) {
                lastChar = StdDraw.nextKeyTyped();
                seed += lastChar;
            }
        }
        return seed.substring(0, seed.length() - 1);
    }

    public boolean updateWorldFrame(char c) {
        if (c != 'a' && c != 'd' && c != 's' && c != 'w' && c != ':' && c != 'q') {
            return true;
        }
        if (playerMovement.length() > 0) {
            char lastChar = playerMovement.charAt(playerMovement.length() - 1);
            if (lastChar != ':' && c == 'q') {
                return true;
            }
            if (lastChar == ':' && c == 'q') {
                try {
                    FileWriter writer = new FileWriter("data.txt");
                    int last = this.playerMovement.length() - 1;
                    writer.write(this.seedString + this.playerMovement.substring(0, last));
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
            if (lastChar == ':' && c != 'q') {
                playerMovement = playerMovement.substring(0, playerMovement.length() - 1);
            }
        }
        int x = this.avatar.getX();
        int y = this.avatar.getY();
        int changeX = 0;
        int changeY = 0;
        if (c == 'a') {
            changeX = -1;
        } else if (c == 'd') {
            changeX = 1;
        } else if (c == 's') {
            changeY = -1;
        } else if (c == 'w') {
            changeY = 1;
        }
        if (!finalWorldFrame[x + changeX][y + changeY].equals(Tileset.WALL)) {
            finalWorldFrame[x][y] = Tileset.FLOOR;
            finalWorldFrame[x + changeX][y + changeY] = this.avatar.getTile();
            this.avatar.setX(x + changeX);
            this.avatar.setY(y + changeY);
            this.playerMovement += c;
        } else if (c == ':' || c == 'q') {
            this.playerMovement += c;
        }
        return true;
    }
    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // parse input string
        if (input.charAt(0) == 'l') {
            try {
                FileReader reader = new FileReader("data.txt");
                BufferedReader bufferedReader = new BufferedReader(reader);
                input = bufferedReader.readLine() + input.substring(1);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int index = -1;
        do {
            index++;
            this.seedString += input.charAt(index);
        } while (input.charAt(index) != 's');
        index++;

        // seed the random generator
        Long seed = Long.parseLong(this.seedString.substring(1, this.seedString.length() - 1));
        rand = new Random(seed);

        // initialize tiles
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }

        // generate rooms
        int numRooms = 6 + rand.nextInt(6);
        for (int i = 0; i < numRooms; i++) {
            Room r = generateRoom();
            rooms.add(r);
            drawRoom(r);
        }

        // generate hallways
        UnionFind uf = new UnionFind(rooms.size());
        for (int i = 0; i < 50; i++) {
            int a = rand.nextInt(rooms.size());
            int b = rand.nextInt(rooms.size());
            Room roomA = rooms.get(a);
            Room roomB = rooms.get(b);
            if (!uf.isConnected(a, b)) {
                uf.connect(a, b);
                connect(roomA, roomB);
            }
        }

        for (int i = 0; i < numRooms; i++) {
            for (int j = 0; j < numRooms; j++) {
                if (!uf.isConnected(i, j)) {
                    uf.connect(i, j);
                    connect(rooms.get(i), rooms.get(j));
                }
            }
        }

        // random player start position in world
        Room startRoom = rooms.get(rand.nextInt(rooms.size()));
        List<Position> roomSpan = startRoom.getSpan();
        Position startPosition = roomSpan.get(rand.nextInt(roomSpan.size()));
        avatar = new Avatar(startPosition, Tileset.AVATAR);
        finalWorldFrame[avatar.getX()][avatar.getY()] = avatar.getTile();

        // execute saved player moves
        while (index < input.length()) {
            updateWorldFrame(input.charAt(index));
            index++;
        }
        return finalWorldFrame;
    }

    public void connect(Room roomA, Room roomB) {
        List<Position> spanA = roomA.getSpan();
        List<Position> spanB = roomB.getSpan();

        Position pointA = spanA.get(rand.nextInt(spanA.size()));
        Position pointB = spanB.get(rand.nextInt(spanB.size()));

        Position start = Position.compareX(pointA, pointB) < 0 ? pointA : pointB;
        Position end = start == pointA ? pointB : pointA;

        for (int col = start.getX() - 1; col < end.getX() + 2; col++) {
            for (int row = start.getY() - 1; row < start.getY() + 2; row++) {
                if (row == start.getY() && col >= start.getX() && col <= end.getX()) {
                    finalWorldFrame[col][row] = Tileset.FLOOR;
                } else if (finalWorldFrame[col][row] != Tileset.FLOOR) {
                    finalWorldFrame[col][row] = Tileset.WALL;
                }
            }
        }

        Position corner = new Position(end.getX(), start.getY());
        start = Position.compareY(corner, end) < 0 ? corner : end;
        end = start == corner ? end : corner;

        for (int row = start.getY() - 1; row < end.getY() + 2; row++) {
            for (int col = start.getX() - 1; col < end.getX() + 2; col++) {
                if (col == start.getX() && row >= start.getY() && row <= end.getY()) {
                    finalWorldFrame[col][row] = Tileset.FLOOR;
                } else if (finalWorldFrame[col][row] != Tileset.FLOOR) {
                    finalWorldFrame[col][row] = Tileset.WALL;
                }
            }
        }

    }

    public Room generateRoom() {
        Position startPosition = randomPosition();
        int width = 6 + rand.nextInt(6);
        int height = 6 + rand.nextInt(6);
        Room newRoom = new Room(startPosition, width, height);
        for (Room r : rooms) {
            if (overlaps(r, newRoom)) {
                return generateRoom();
            }
        }
        return newRoom;
    }

    public void drawRoom(Room r) {
        for (Position p : r.getSpan()) {
            finalWorldFrame[p.getX()][p.getY()] = Tileset.FLOOR;
        }
        for (Position p : r.getWalls()) {
            finalWorldFrame[p.getX()][p.getY()] = Tileset.WALL;
        }
    }

    public boolean overlaps(Room a, Room b) {
        for (Position p1 : b.getSpan()) {
            for (Position p2 : a.getSpan()) {
                if (Math.abs(p1.getX() - p2.getX()) <= 3 && Math.abs(p1.getY() - p2.getY()) <= 3) {
                    return true;
                }
            }
        }
        return false;
    }

    public Position randomPosition() {
        int x = 1 + rand.nextInt(WIDTH - 13);
        int y = 1 + rand.nextInt(HEIGHT - 13);
        return new Position(x, y);
    }
}
