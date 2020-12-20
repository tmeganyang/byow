package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width * 16);
        StdDraw.setYscale(0, this.height * 16);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        String str = "";
        for (int i = 0; i < n; i++) {
            int index = rand.nextInt(CHARACTERS.length);
            str += CHARACTERS[index];
        }
        return str;
    }

    public void drawFrame(String s) {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(this.width * 8, this.height * 8, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            StdDraw.clear();
            StdDraw.show();
            StdDraw.pause(500);
        }


    }

    public String solicitNCharsInput(int n) {
        String str = "";
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                str += StdDraw.nextKeyTyped();
                n--;
            }
        }
        return str;
    }

    public void startGame() {
        int round = 1;
        String randString = "";
        String inputString = "";
        while (randString.equals(inputString)) {
            drawFrame("Round: " + round);
            StdDraw.pause(500);
            randString = generateRandomString(round);
            flashSequence(randString);
            inputString = solicitNCharsInput(round);
            round++;
        }
        round--;
        drawFrame("Game Over! You made it to Round: " + round);
    }

}
