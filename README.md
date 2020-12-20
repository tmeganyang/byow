# Build Your Own World Design Document

**Partner 1:**
Megan Yang

**Partner 2:**
Blanchard Kenfack

## Classes and Data Structure
### UnionFind

Used to connect rooms 
### Position
Represent a coordinate on a world grid

##### Instance Variables
1. x - the x-axis (or equivalent) coordinate of a point.
2. y - the y-axis (or equivalent) coordinate of a point.

### compareX(Position a, Position b) and compareY(Position a, Position b) 

static methods


### Room
A two-dimensional rectangle with random integer width and height fixed at lower left Position

##### Instance Variables
1. Position pos - the Position of the lower left corner of the room
2. int width - width of the room
3. int height - height of the room
4. List<Position> span - the space occupied by the room
5. List<Position> walls - the walls surrounding the span of the room

## Algorithms
### generateRoom

The generateRoom method returns a Room of random integer width and height positioned at some randomly generated Position. We randomly generate four integers, each denoting one of the width, height, lower left corner x-coordinate, and lower left corner y-coordinate. Then, we instantiate and return a new Room object by passing these fields into the constructor to generate a new Room.

### connect
The connect method connects two rooms together. First, we randomly select a point inside the first room and another point inside the second room. Then, we draw a path comprised of a horizontal and vertical component connecting the two points together. Finally, we update our disjoint set data structure to reflect that these two rooms are now connected.


## Persistence

