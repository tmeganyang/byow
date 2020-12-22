# Explorable World Game Engine

This is the project page for a game engine that generates explorable 2D tile-based worlds. 

## World Generation
Worlds are initialized as 90 x 40 grids of black tiles. A random positive integer is chosen denoting the number of rooms we would like to populate our world with. These rooms are then generated with pseudo-random integer positions, widths, and heights. To ensure that worlds are not too cluttered, our world generation algorithm ensures that rooms do not overlap and maintain a distance of at least three spaces away from each other. 

Following room generation, we connect rooms at random using hallways. First, we randomly select a point inside a fixed first room and another point inside a second room. Then, we draw a path comprised of a horizontal and vertical component connecting the two points together. Finally, we update a weighted union-find data structure to reflect that these two rooms are now connected.

![alt text](https://i.imgur.com/OqH2RJB.png)

## Persistence
Game persistence is accomplished by keeping track of the world seed and user keypresses following world generation. The state of a play is saved in the `data.txt` file when a user quits the program with `:q` in the following format: `n[SEED][PLAYER MOVEMENT]`. The saved game can be loaded when the program is first started by pressing `l`. 

![alt text](https://i.imgur.com/91cs8Cj.png)

## Interactivity
The user is able to control an avatar represented by an `@` tile that can moved around using the W, A, S, and D keys. The system is deterministic in that the same sequence of keypresses from the same seed will result in exactly the same behavior every time.
