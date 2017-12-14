package cashflow.getmoney.amazeme;


<<<<<<< HEAD
=======
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

>>>>>>> 5666673d269294b2c16a021b56dfb65c8df25530
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import processing.core.PApplet;

public class Sketch extends PApplet {
    public static float SCALE;
    public static int BLOCKED_ALPHA = 100;
    public static Player player;
    public static Maze maze;
    public int xDim, yDim;
<<<<<<< HEAD

    public static float initialRotation = -3 * PI / 4, initialBearing, rotation;
=======
    public static float rotation = 0;

>>>>>>> 5666673d269294b2c16a021b56dfb65c8df25530
    public int updateCount = 0;
    public boolean initialized = false;
    double currentLat, currentLong, previousLat, previousLong;
    public static double startLat, startLong, degreesPerCell;

<<<<<<< HEAD
=======

>>>>>>> 5666673d269294b2c16a021b56dfb65c8df25530
    //score calculator
    public static long startTime;
    public static long endTime;
    public static long totalTime;
<<<<<<< HEAD

=======
>>>>>>> 5666673d269294b2c16a021b56dfb65c8df25530
    //cellsPerView Determines SCALE
    public Sketch() {
        super();

    }

//    FloatySquare[] fs;


    public void settings() {
        size(displayWidth, displayHeight);
    }


    public void setup() {
        background(255);
        xDim = 1;
        yDim = 1;
//        fs = new FloatySquare[30];
//        for (FloatySquare s : fs) {
//            s = new FloatySquare();
//        }

    }

    public void init(int difficulty, double startLatitude, double startLongitude, float initialBearing, double degreesPerCell, int cellsPerView) {
        this.startLat = startLatitude;
        this.startLong = startLongitude;
        this.degreesPerCell = degreesPerCell;
        this.initialBearing = initialBearing;
        SCALE = displayWidth / cellsPerView;
        int size = xDim * yDim;
        int minPathSize = (int) (difficulty * size / 10);
        IntCoord start = new IntCoord(xDim / 2, yDim / 2),
                finish = new IntCoord(xDim - 1, yDim - 1);
        maze = new Maze(xDim, yDim);
        maze.init(true);
        //maze.blockCells(new IntCoord (10,10), new IntCoord(12,14));
        maze.generate(start, finish, minPathSize);
        player = new Player(maze.getCell(start).getCenter(), maze);

        initialized = true;

        startTime = System.nanoTime();
    }

    public void updateLocation(float lat, float lon) {

        float X = (float) (((lat - startLat) / degreesPerCell) * SCALE);
        float Y = (float) (((lon - startLong) / degreesPerCell) * SCALE);
        updateCount++;
        previousLat = currentLat;
        previousLong = currentLong;
        currentLat = lat;
        currentLong = lon;
        if (player != null) {
            float X2 = cos(initialBearing) * X - sin(initialBearing) * Y;
            float Y2 = sin(initialBearing) * X + cos(initialBearing) * Y;
            player.updateCoords(maze.getStart().getCenter().add(X2, Y2));
        }
    }

    public void updateRotation(float bearing) {

        rotation = initialRotation - (bearing - initialBearing);
    }

    public void draw() {
        fill(255, 255, 255);
        rect(-1, -1, width + 2, height + 2);
        if (!initialized) {
            loading();
            return;
        }
        maze.translateTo(player.getFloatPosition());
        if (!maze.gameOver()) {
            run();
            return;
        }
        gameOverScreen();

    }

    private void loading() {
        textAlign(CENTER);
        fill(0);
        textSize(70);
        text("Connecting...\nFace an open area and stand still", width / 2, height / 2);


    }


    private void run() {

        pushMatrix();
        translate(width / 2, height / 2);
        rotate(rotation);
        maze.draw();
        popMatrix();
        fill(0);
        player.draw();
        textSize(40);
        text(player.moveListOutput(), width / 2, 300);
        text("updateCount: " + updateCount + "\nLAT: " + (currentLat - startLat) + "\nLONG: " + (currentLong - startLong) + "\nROTATION: " + rotation, width / 2, 50);
        handleIlegalMoves();
    }


    private void gameOverScreen() {
        endTime = System.nanoTime();
<<<<<<< HEAD

=======
>>>>>>> 5666673d269294b2c16a021b56dfb65c8df25530
        textAlign(CENTER);
        fill(0);
        text("WINNER", width / 2, height / 2);

        getActivity().finish();
//        for (FloatySquare s:fs){
//            s.draw();
//        }

    }

    private class FloatySquare {
        float x, y, r, xVel, yVel, rVel;
        float size;

        public FloatySquare() {
            size = random(width / 10, width / 5);
            x = random(width - 4 * size);
            y = random(height - 4 * size);
            xVel = random(-20, 20);
            yVel = random(-20, 20);
            r = random(PI);
            rVel = random(-10, 10);
        }

        public void draw() {
            pushMatrix();
            translate(x, y);
            rotate(r);
            rect(x - size / 2, y - size / 2, size, size);
            x += xVel;
            y += yVel;
            r += rVel;
            popMatrix();
            if (x >= width || x <= 0) xVel *= -1;
            if (y >= height || y <= 0) yVel *= -1;
        }


    }

    //returns total seconds it took for user to complete maze
<<<<<<< HEAD
    private long returnTotalTime() {
=======
    private int returnTotalTime(){
>>>>>>> 5666673d269294b2c16a021b56dfb65c8df25530
        totalTime = endTime - startTime;
        int totalSeconds = (int) TimeUnit.NANOSECONDS.toSeconds(totalTime);
        return totalSeconds;
    }

    private void handleIlegalMoves() {
        Cell mark;
        if (player.lastLegal() != null)
            mark = maze.getCell(player.lastLegal().getB());
        else mark = null;

        maze.markLastLegal(mark);
    }


    public class Cell {
        private boolean explored = false, blocked = false;
        private IntCoord gridLocation;
        private FloatCoord screenLocation;
        private Wall[] walls;
        private boolean finishCell = false, startCell = false;

        public Cell(int x, int y) {
            gridLocation = new IntCoord(x, y);
            screenLocation = gridLocation.getFloatCoord();
            walls = new Wall[4];
        }

        public void setWall(Direction d, Wall w) {
            switch (d) {
                case UP:
                    walls[0] = w;
                    break;
                case DOWN:
                    walls[1] = w;
                    break;
                case LEFT:
                    walls[2] = w;
                    break;
                case RIGHT:
                    walls[3] = w;
                    break;
                default:
                    break;
            }
        }

        public void setAsFinish() {
            finishCell = true;
        }

        public void setAsStart() {
            startCell = true;
        }

        public Wall getWall(Direction d) {
            switch (d) {
                case UP:
                    return walls[0];
                case DOWN:
                    return walls[1];
                case LEFT:
                    return walls[2];
                case RIGHT:
                    return walls[3];

                default:
                    return null;
            }
        }

        public IntCoord getPosition() {
            return gridLocation;
        }

        public FloatCoord getCenter() {
            return new FloatCoord(screenLocation.getX() + SCALE / 2, screenLocation.getY() + SCALE / 2);
        }

        public void setBlocked() {
            blocked = true;
        }

        public void setExplored() {
            explored = true;
        }

        public void setUnexplored() {
            explored = false;
        }

        public boolean isBlocked() {
            return blocked;
        }

        public boolean isExplored() {
            return explored;
        }

        public void highlight() {
            float x = screenLocation.getX(), y = screenLocation.getY();
            fill(255, 50, 100);
            noStroke();
            rect(x, y, SCALE, SCALE);
        }

        public void draw() {
            float x = screenLocation.getX(), y = screenLocation.getY();
            noStroke();
            if (blocked) {
                fill(0, 0, 0, BLOCKED_ALPHA);
                rect(x, y, SCALE, SCALE);
                return;
            }

            if (finishCell) {
                fill(255, 100, 0);
                rect(x, y, SCALE, SCALE);
            }

            if (startCell) {
                fill(50, 100, 255);
                rect(x, y, SCALE, SCALE);
            }

            for (Wall w : walls) {
                w.draw();
            }
        }
    }


    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public class FloatCoord {
        private float x, y;

        public FloatCoord(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public FloatCoord add(float xPos, float yPos) {
            return new FloatCoord(this.x + xPos, this.y + yPos);
        }

        public IntCoord getIntCoord() {
            return new IntCoord((int) (x / SCALE), (int) (y / SCALE));
        }

        public FloatCoord changeX(float rate) {
            x += rate;
            return copy();
        }

        public FloatCoord copy() {
            return new FloatCoord(x, y);
        }


        public FloatCoord changeY(float rate) {
            y += rate;
            return copy();
        }

        public FloatCoord getInversion() {
            return new FloatCoord(-x + SCALE, -y + SCALE);
        }

        public FloatCoord getDelta(FloatCoord other) {
            return new FloatCoord(x - other.getX(), y - other.getY());
        }

        public float magnitude() {
            return sqrt((x * x) + (y * y));
        }

        public FloatCoord relocate(float translation) {
            return new FloatCoord(translation * x / magnitude(), translation * y / magnitude());
        }

    }

    public class IntCoord {
        private int x, y;

        public IntCoord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public FloatCoord getFloatCoord() {
            return new FloatCoord(x * SCALE, y * SCALE);
        }

        public IntCoord copy() {
            return new IntCoord(x, y);
        }


        public String coordString() {
            return x + ", " + y;
        }

        public boolean equals(IntCoord other) {
            return (x == other.getX() && y == other.getY());
        }
    }


    public class Maze {
        private Cell[][] grid;
        private int widthDim, heightDim;
        private FloatCoord translation;
        private Cell finish;
        private Cell lastLegal;
        private Cell start;

        public Maze(int widthDim, int heightDim) {
            grid = new Cell[widthDim][heightDim];
            this.widthDim = widthDim;
            this.heightDim = heightDim;
        }

        private void init(boolean firstInit) {

            for (int j = 0; j < heightDim; j++)
                for (int i = 0; i < widthDim; i++) {

                    if (firstInit) grid[i][j] = new Cell(i, j);
                    grid[i][j].setUnexplored();
                    Wall h = new Wall(i, j, Orientation.HORIZ);
                    Wall v = new Wall(i, j, Orientation.VERT);
                    grid[i][j].setWall(Direction.UP, h);
                    grid[i][j].setWall(Direction.LEFT, v);

                    if (i != 0) grid[i - 1][j].setWall(Direction.RIGHT, v);
                    if (j != 0) grid[i][j - 1].setWall(Direction.DOWN, h);


                    if (i == widthDim - 1) {
                        v = new Wall(i + 1, j, Orientation.VERT);
                        grid[i][j].setWall(Direction.RIGHT, v);
                    }

                    if (j == heightDim - 1) {
                        h = new Wall(i, j + 1, Orientation.HORIZ);
                        grid[i][j].setWall(Direction.DOWN, h);
                    }
                }
        }

        public void stringToMaze(String userId) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mazeRef = database.getReference("maps/" + userId);

            mazeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int widthDim = (int) dataSnapshot.child("width").getValue();
                    int heightDim = (int) dataSnapshot.child("height").getValue();

                    // Instantiate maze grid with correct dimensions
                    grid = new Cell[widthDim][heightDim];

                    String mazeStr = (String) dataSnapshot.child("mazeString").getValue();
                    char[] mazeChars = mazeStr.toCharArray();
                    int charIndex = 0;

                    for(int j = 0; j < heightDim; j++) {
                        for(int i = 0; i < widthDim; i++) {
                            Wall h = new Wall(i, j, Orientation.HORIZ);
                            Wall v = new Wall(i, j, Orientation.VERT);

                            switch(mazeChars[charIndex]) {
                                case '0':
                                    charIndex++;
                                case '1':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.LEFT, v);
                                case '2':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.DOWN, h);
                                case '3':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.RIGHT, v);
                                case '4':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.UP, h);
                                case '5':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.LEFT, v);
                                    grid[i][j].setWall(Direction.DOWN, h);
                                case '6':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.RIGHT, v);
                                    grid[i][j].setWall(Direction.DOWN, h);
                                case '7':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.RIGHT, v);
                                    grid[i][j].setWall(Direction.UP, h);
                                case '8':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.LEFT, v);
                                    grid[i][j].setWall(Direction.UP, h);
                                case '9':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.LEFT, v);
                                    grid[i][j].setWall(Direction.RIGHT, v);
                                    grid[i][j].setWall(Direction.DOWN, h);
                                case 'A':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.RIGHT, v);
                                    grid[i][j].setWall(Direction.UP, h);
                                    grid[i][j].setWall(Direction.DOWN, h);
                                case 'B':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.LEFT, v);
                                    grid[i][j].setWall(Direction.RIGHT, v);
                                    grid[i][j].setWall(Direction.UP, h);
                                case 'C':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.LEFT, v);
                                    grid[i][j].setWall(Direction.UP, h);
                                    grid[i][j].setWall(Direction.DOWN, h);
                                case 'D':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.LEFT, v);
                                    grid[i][j].setWall(Direction.RIGHT, v);
                                case 'E':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.UP, h);
                                    grid[i][j].setWall(Direction.DOWN, h);
                                case 'F':
                                    charIndex++;
                                    grid[i][j].setWall(Direction.LEFT, v);
                                    grid[i][j].setWall(Direction.RIGHT, v);
                                    grid[i][j].setWall(Direction.UP, h);
                                    grid[i][j].setWall(Direction.DOWN, h);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void mazeToString(String userId) {
            Cell[][] mazeCells = maze.grid;
            int widthDim = maze.widthDim, heightDim = maze.heightDim;
            String mazeStr = "";

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mazeRef = database.getReference("maps/" + userId);

            for(int j = 0; j < heightDim; j++) {
                for(int i = 0; i < widthDim; i++) {
                    boolean isUp = false, isDown = false, isLeft = false, isRight = false;

                    Wall up =  mazeCells[i][j].getWall(Direction.UP);
                    if(up != null) { isUp = true; }
                    Wall down = mazeCells[i][j].getWall(Direction.DOWN);
                    if(down != null) { isDown = true; }
                    Wall left = mazeCells[i][j].getWall(Direction.LEFT);
                    if(left != null) { isLeft = true; }
                    Wall right = mazeCells[i][j].getWall(Direction.RIGHT);
                    if(right != null) { isRight = true; }

                    if(!isUp && !isDown && !isLeft && !isRight) {
                        mazeStr += "0";
                    } else if (!isUp && !isDown && isLeft && !isRight) {
                        mazeStr += "1";
                    } else if (!isUp && isDown && !isLeft && !isRight) {
                        mazeStr += "2";
                    } else if (!isUp && !isDown && !isLeft && isRight) {
                        mazeStr += "3";
                    } else if (isUp && !isDown && !isLeft && !isRight) {
                        mazeStr += "4";
                    } else if (!isUp && isDown && isLeft && !isRight) {
                        mazeStr += "5";
                    } else if (!isUp && isDown && !isLeft && isRight) {
                        mazeStr += "6";
                    } else if (isUp && !isDown && !isLeft && isRight) {
                        mazeStr += "7";
                    } else if (isUp && !isDown && isLeft && !isRight) {
                        mazeStr += "8";
                    } else if (!isUp && isDown && isLeft && isRight) {
                        mazeStr += "9";
                    } else if (isUp && isDown && !isLeft && isRight) {
                        mazeStr += "A";
                    } else if (isUp && !isDown && isLeft && isRight) {
                        mazeStr += "B";
                    } else if (isUp && isDown && isLeft && !isRight) {
                        mazeStr += "C";
                    } else if (!isUp && !isDown && isLeft && isRight) {
                        mazeStr += "D";
                    } else if (isUp && isDown && !isLeft && !isRight) {
                        mazeStr += "E";
                    } else if (isUp && isDown && isLeft && isRight) {
                        mazeStr += "F";
                    }
                }
            }

            mazeRef.child("mazeString").setValue(mazeStr);
            mazeRef.child("width").setValue(widthDim);
            mazeRef.child("height").setValue(heightDim);

        }

        public Cell getStart() {

            return start;
        }

        public void generate(IntCoord start, IntCoord finish, int minLength) {

            int pathLength = 0;
            int count = 0;
            Stack<Cell> stack = new Stack<Cell>();
            Cell current = getCell(start);
            current.setAsStart();
            Cell goal = getCell(finish);
            this.finish = goal;
            this.start = getCell(start);
            //mark current as start?
            goal.setAsFinish();
            stack.push(current);
            while (!stack.isEmpty()) {
                Cell rando = getRandomAdjacent(current);
                if (rando == goal) {
                    rando.setExplored();
                    removeWall(current, rando);
                    pathLength = count;
                }
                if (rando != null && rando != goal) {
                    count++;
                    stack.push(rando);
                    rando.setExplored();
                    removeWall(current, rando);
                    current = rando;
                } else {
                    current = stack.pop();
                    count--;
                }
            }
            int size = widthDim * heightDim;
            float errorMargin = .9f;
            if (pathLength < minLength || pathLength > minLength + errorMargin * size) {
                init(false);
                generate(start, finish, minLength);
            }
        }


        public Cell getCell(IntCoord i) {
            if (!withinBounds(i.getX(), i.getY())) {
                return null;
            }
            return grid[i.getX()][i.getY()];
        }

        private Cell getRandomAdjacent(Cell c) {
            Cell up = getAdjacent(c, Direction.UP);
            Cell down = getAdjacent(c, Direction.DOWN);
            Cell left = getAdjacent(c, Direction.LEFT);
            Cell right = getAdjacent(c, Direction.RIGHT);

            Cell[] adj = new Cell[]{up, down, left, right};

            boolean cont = true;

            while (cont) {
                int rando = horizBiasRandom();
                if (adj[rando] != null) {
                    if (!adj[rando].isExplored() && !adj[rando].isBlocked()) return adj[rando];
                    adj[rando] = null;
                }
                int count = 0;
                for (Cell cell : adj) {
                    if (cell != null)
                        count++;
                }
                cont = count != 0;
            }
            return null;
        }

        private int horizBiasRandom() {
            double rando = Math.random();
            if (rando < 0.1) return 0;
            if (rando < 0.2) return 1;
            if (rando < 0.6) return 2;
            return 3;
        }

        private Cell getAdjacent(Cell c, Direction d) {
            int x1 = c.getPosition().getX();
            int y1 = c.getPosition().getY();
            int x2 = x1, y2 = y1;

            switch (d) {
                case UP:
                    y2--;
                    break;
                case DOWN:
                    y2++;
                    break;
                case LEFT:
                    x2--;
                    break;
                case RIGHT:
                    x2++;
                    break;
            }

            if (!withinBounds(x2, y2)) return null;
            return grid[x2][y2];
        }

        private boolean withinBounds(int x, int y) {
            if (x < 0 || y < 0) return false;
            if (x >= widthDim || y >= heightDim) return false;
            return true;
        }

        private void removeWall(Cell a, Cell b) {
            stroke(255, 0, 0);


            Direction d = A_TO_B(a, b);
            a.getWall(d).remove();
        }

        public Direction A_TO_B(Cell a, Cell b) {
            int x1 = a.getPosition().getX(), x2 = b.getPosition().getX();
            int y1 = a.getPosition().getY(), y2 = b.getPosition().getY();

            if (x1 < x2) return Direction.RIGHT;
            if (x1 > x2) return Direction.LEFT;
            if (y1 < y2) return Direction.DOWN;
            return Direction.UP;
        }

        public void draw() {
            pushMatrix();
            translate(translation.getInversion().getX() - SCALE, translation.getInversion().getY() - SCALE);
            if (lastLegal != null) lastLegal.highlight();
            for (int i = 0; i < widthDim; i++)
                for (Cell c : grid[i])
                    c.draw();


            popMatrix();
            resetMatrix();
            pushMatrix();
            translate(width / 2, height / 2);
            rotate(rotation);

            ArrowBoy guide = new ArrowBoy(translation, finish.getCenter(), SCALE / 1.7f, SCALE / 3);
            stroke(255, 150, 70);
            guide.draw();

            if (lastLegal != null) {
                ArrowBoy markGuide = new ArrowBoy(translation, lastLegal.getCenter(), SCALE / 1.7f, SCALE / 3);
                stroke(255, 0, 0);
                markGuide.draw();
            }

            popMatrix();
        }

        public void markLastLegal(Cell c) {
            lastLegal = c;
        }

        public void translateTo(FloatCoord t) {
            translation = t;
        }

        public void blockCells(IntCoord topLeft, IntCoord botRight) {
            int x1 = topLeft.getX(), y1 = topLeft.getY(), x2 = botRight.getX(), y2 = botRight.getY();

            for (int y = y1; y <= y2; y++)
                for (int x = x1; x <= x2; x++) {
                    grid[x][y].setBlocked();
                }
        }

        public boolean gameOver() {
            return (finish.getCenter().getDelta(translation).magnitude() < SCALE / 2 && lastLegal == null);
        }
    }

    public class ArrowBoy {

        private FloatCoord tail, tip;

        public ArrowBoy(FloatCoord from, FloatCoord to, float size, float offset) {

            FloatCoord delta = to.getDelta(from);

            tip = delta.relocate(size);
            tail = delta.relocate(offset);
        }

        public void draw() {

            strokeWeight(2);

            line(tail.getX(), tail.getY(), tip.getX(), tip.getY());
            stroke(0);
        }
    }

    public class Player {
        public FloatCoord floatPosition;
        private Maze maze;
        public IntCoord intPosition;
        private boolean cheating = false;
        public static final float moveRate = 15;
        ArrayList<Move> moveList;

        public Player(FloatCoord floatPosition, Maze maze) {
            this.maze = maze;
            this.floatPosition = floatPosition;
            this.intPosition = floatPosition.getIntCoord();
            moveList = new ArrayList<Move>();
        }

        public void updateCoords(FloatCoord f) {
            floatPosition = f;
            IntCoord B = f.getIntCoord();
            if (!B.equals(intPosition)) {
                Move m = new Move(intPosition, B);
                if (!cheating) moveList.add(m);
                if (!m.legal(maze)) cheating = true;

                if (lastLegal() != null)
                    if (B.equals(lastLegal().getB())) {
                        cheating = false;
                        moveList.remove(moveList.size() - 1);
                    }
                intPosition = B;
            }
        }

        public String moveListOutput() {
            String out = "";
            for (Move m : moveList) {
                out += m.getA().coordString() + " -> " + m.getB().coordString() + " " + m.legal(maze) + " \n";
            }
            return out;
        }

        public Move lastLegal() {
            for (int i = 0; i < moveList.size(); i++) {
                if (!moveList.get(i).legal(maze) && i != 0) {
                    return moveList.get(i - 1);
                }
            }
            return null;
        }

        public FloatCoord getFloatPosition() {
            return floatPosition;
        }

        public IntCoord getIntPosition() {
            return floatPosition.getIntCoord();
        }

        public void move(Direction d) {
            switch (d) {
                case UP:
                    updateCoords(floatPosition.changeY(-moveRate));
                    break;
                case DOWN:
                    updateCoords(floatPosition.changeY(moveRate));
                    break;
                case LEFT:
                    updateCoords(floatPosition.changeX(-moveRate));
                    break;
                case RIGHT:
                    updateCoords(floatPosition.changeX(moveRate));
                    break;
            }
        }

        public void draw() {
            ellipse(width / 2, height / 2, 10, 10);
        }
    }

    public class Move {

        private IntCoord A, B;

        public Move(IntCoord A, IntCoord B) {
            this.A = A;
            this.B = B;
        }

        public boolean legal(Maze m) {
            Cell a = m.getCell(A);
            Cell b = m.getCell(B);
            if (a == null || b == null) return false;
            Direction d = m.A_TO_B(a, b);
            return a.getWall(d).isOpen();
        }

        public IntCoord getA() {
            return A;
        }

        public IntCoord getB() {
            return B;
        }
    }

    public class Wall {
        private IntCoord pointA, pointB;
        public Orientation orientation;

        public Wall(int x, int y, Orientation o) {
            pointA = new IntCoord(x, y);
            switch (o) {
                case HORIZ:
                    pointB = new IntCoord(x + 1, y);
                    break;
                case VERT:
                    pointB = new IntCoord(x, y + 1);
                    break;
                default:
                    pointB = pointA.copy();
                    break;
            }
            orientation = o;
        }

        public void remove() {
            orientation = Orientation.OPEN;
        }

        public boolean isOpen() {
            return orientation == Orientation.OPEN;
        }


        public IntCoord getPointA() {
            return pointA;
        }

        public IntCoord getPointB() {
            return pointB;
        }

        public void draw() {
            if (orientation == Orientation.OPEN) return;
            float x1 = pointA.getFloatCoord().getX(), x2 = pointB.getFloatCoord().getX(),
                    y1 = pointA.getFloatCoord().getY(), y2 = pointB.getFloatCoord().getY();

            stroke(0);
            strokeWeight(2);
            line(x1, y1, x2, y2);
        }

    }

    public enum Orientation {
        OPEN, HORIZ, VERT
    }


}