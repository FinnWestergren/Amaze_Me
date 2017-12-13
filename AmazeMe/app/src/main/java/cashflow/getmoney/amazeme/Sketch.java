package cashflow.getmoney.amazeme;


import java.util.ArrayList;
import java.util.Stack;

import processing.core.PApplet;

public class Sketch extends PApplet {
    public static  float SCALE;
    public static int BLOCKED_ALPHA = 100;
    public static Player player;
    public static Maze maze;
    public int xDim, yDim;
    public static float rotation = 0;
    public static double startLat, startLong, feetPerCell;
    //cellsPerView Determines SCALE
    public Sketch(){
        super();

    }


    public void settings(){
        size(displayWidth,displayHeight);
    }


    public void setup() {
        background(255);
        xDim = 20;
        yDim = 20;

    }

    public void init(int difficulty,double startLatitude, double startLongitude, double feetPerCell, int cellsPerView) {
        this.startLat = startLatitude;
        this.startLong = startLongitude;
        this.feetPerCell = feetPerCell;
        SCALE = displayWidth/cellsPerView;
        int size = xDim * yDim;
        int minPathSize = (int)(difficulty * size / 10) ;
        IntCoord start = new IntCoord(0, 0),
                finish = new IntCoord(xDim-1, yDim-1);
        maze = new Maze(xDim, yDim);
        maze.init(true);
        //maze.blockCells(new IntCoord (10,10), new IntCoord(12,14));
        maze.generate(start, finish, minPathSize);

        player = new Player(maze.getCell(start).getCenter(), maze);
    }

    public void updateLocation(double lat, double lon){

        float X = (float) (((lat - startLat)/feetPerCell) * SCALE);
        float Y = (float) (((lon - startLong)/feetPerCell) * SCALE);
        if(player != null)
        player.updateCoords(new FloatCoord(X,Y));
    }

    public void draw() {
        if(player == null) {
            textAlign(CENTER);
            fill(0);
            textSize(70);
            text("attempting to connect...", width/2,height/2);
            return;
        }
        fill(255,255,255);
        rect(-1, -1, width +2, height + 2);
        maze.translateTo(player.getFloatPosition());
        if (!maze.gameOver()) {
            run();
        } else {
            gameOverScreen();
        }
    }



    private void run(){

        pushMatrix();
        translate(width/2, height/2);
        rotate(rotation);

        maze.draw();
        popMatrix();
        fill(0);
        player.draw();
        text(player.moveListOutput(), 50, 50);
        handleIlegalMoves();
    }

    private void gameOverScreen(){
        textAlign(CENTER);
        fill(0);
        text("WINNER", width/2,height/2);
    }

    private void handleIlegalMoves() {
        Cell mark;
        if (player.lastLegal()!=null)
            mark = maze.getCell(player.lastLegal().getB());
        else  mark = null;

        maze.markLastLegal(mark);
    }

    public void keyPressed() {
        if (key == CODED) {
            switch(keyCode) {
                case UP:
                    player.move(Direction.UP);
                    break;
                case DOWN:
                    player.move(Direction.DOWN);
                    break;
                case LEFT:
                    player.move(Direction.LEFT);
                    break;
                case RIGHT:
                    player.move(Direction.RIGHT);
                    break;
            }
        }

        if (key == 'r') {
            rotation += PI/16;
        }
        if (key == 'e') {
            rotation -= PI/16;
        }
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
            switch(d) {
                case UP:
                    walls[0]  = w;
                    break;
                case DOWN:
                    walls[1]  = w;
                    break;
                case LEFT:
                    walls[2]  = w;
                    break;
                case RIGHT:
                    walls[3]  = w;
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
            switch(d) {
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
            return new FloatCoord(screenLocation.getX() + SCALE/2, screenLocation.getY() + SCALE/2);
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
            fill(255,50,100);
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

    public class FloatCoord{
        private float x,y;
        public FloatCoord(float x, float y){
            this.x = x;
            this.y = y;
        }
        public float getX(){
            return x;
        }

        public float getY(){
            return y;
        }

        public IntCoord getIntCoord(){
            return new IntCoord((int)( x / SCALE) ,(int)( y / SCALE));
        }

        public FloatCoord changeX(float rate){
            x+=rate;
            return copy();
        }

        public FloatCoord copy(){
            return new FloatCoord(x,y);
        }


        public FloatCoord changeY(float rate){
            y+=rate;
            return copy();
        }

        public FloatCoord getInversion(){
            return new FloatCoord(-x + SCALE , -y + SCALE);
        }

        public FloatCoord getDelta(FloatCoord other){
            return new FloatCoord(x - other.getX(), y - other.getY());
        }

        public float magnitude(){
            return sqrt((x * x) + (y * y));
        }

        public FloatCoord relocate(float translation){
            return new FloatCoord( translation* x / magnitude(), translation* y/magnitude());
        }

    }

    public class IntCoord{
        private int x,y;
        public IntCoord(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX(){
            return x;
        }

        public int getY(){
            return y;
        }

        public FloatCoord getFloatCoord(){
            return new FloatCoord(x * SCALE , y * SCALE );
        }

        public IntCoord copy(){
            return new IntCoord(x,y);
        }


        public String coordString(){
            return x + ", " + y;
        }

        public boolean equals(IntCoord other){
            return (x == other.getX() && y == other.getY());
        }
    }


    public class Maze {
        private Cell[][] grid;
        private int widthDim, heightDim;
        private FloatCoord translation;
        private Cell finish;
        private Cell lastLegal;
        public Maze(int widthDim, int heightDim) {
            grid = new Cell[widthDim][heightDim];
            this.widthDim = widthDim;
            this.heightDim = heightDim;
        }

        private void init(boolean firstInit) {

            for (int j = 0; j < heightDim; j++)
                for (int i = 0; i < widthDim; i++)
                {

                    if (firstInit) grid[i][j] = new Cell(i, j);
                    grid[i][j].setUnexplored();
                    Wall h = new Wall(i, j, Orientation.HORIZ);
                    Wall v = new Wall(i, j, Orientation.VERT);
                    grid[i][j].setWall(Direction.UP, h);
                    grid[i][j].setWall(Direction.LEFT, v);

                    if (i!=0) grid[i-1][j].setWall(Direction.RIGHT, v);
                    if (j!=0) grid[i][j-1].setWall(Direction.DOWN, h);


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


        public void generate(IntCoord start, IntCoord finish, int minLength) {

            int pathLength = 0;
            int count = 0;
            Stack<Cell> stack = new Stack<Cell>();
            Cell current  = getCell(start);
            current.setAsStart();
            Cell goal = getCell(finish);
            this.finish = goal;
            //mark current as start?
            goal.setAsFinish();
            stack.push(current) ;
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
                    count --;
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
                    if (cell  != null)
                        count ++;
                }
                cont = count != 0;
            }
            return null;
        }

        private int horizBiasRandom()
        {
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

            switch(d) {
                case UP:
                    y2 --;
                    break;
                case DOWN:
                    y2 ++;
                    break;
                case LEFT:
                    x2 --;
                    break;
                case RIGHT:
                    x2 ++;
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


            Direction d =  A_TO_B(a, b);
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
            translate(translation.getInversion().getX() - SCALE, translation.getInversion().getY()-SCALE);
            if (lastLegal != null) lastLegal.highlight();
            for (int i = 0; i < widthDim; i++)
                for (Cell c : grid[i])
                    c.draw();



            popMatrix();

            ArrowBoy guide =  new ArrowBoy(translation, finish.getCenter(), SCALE/1.7f, SCALE/3);
            stroke(255, 150, 70);
            guide.draw();

            if(lastLegal != null){
                ArrowBoy markGuide =  new ArrowBoy(translation, lastLegal.getCenter(), SCALE/1.7f, SCALE/3);
                stroke(255, 0 ,0 );
                markGuide.draw();
            }
        }

        public void markLastLegal(Cell c) {
            lastLegal = c;
        }

        public void translateTo(FloatCoord t) {
            translation = t;
        }

        public void blockCells(IntCoord topLeft, IntCoord botRight) {
            int x1 = topLeft.getX(), y1 = topLeft.getY(), x2 = botRight.getX(), y2 = botRight.getY();

            for (int y = y1; y <= y2; y ++)
                for (int x = x1; x <=x2; x ++) {
                    grid[x][y].setBlocked();
                }
        }

        public boolean gameOver() {
            return  (finish.getCenter().getDelta(translation).magnitude() < SCALE/2 && lastLegal == null);
        }
    }

    public class ArrowBoy {

        private FloatCoord tail, tip;
        public ArrowBoy(FloatCoord from, FloatCoord to, float size, float offset ) {

            FloatCoord delta = to.getDelta(from);

            tip = delta.relocate(size);
            tail = delta.relocate(offset);
        }

        public void draw() {

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
            this.maze =maze;
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
                if(!m.legal(maze)) cheating = true;

                if (lastLegal() != null)
                    if (B.equals(lastLegal().getB())) {
                        cheating = false;
                        moveList.remove(moveList.size()-1);
                    }

                intPosition = B;
            }
        }

        public String moveListOutput() {
            String out = "";
            for (Move m : moveList) {
                out+= m.getA().coordString()+ " -> " +m.getB().coordString() + " " + m.legal(maze) +" \n";
            }
            return out;
        }

        public Move lastLegal() {
            for (int i = 0; i < moveList.size(); i ++) {
                if (!moveList.get(i).legal(maze) && i != 0) {
                    return moveList.get(i-1);
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
            switch(d) {
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
            ellipse(width/2, height/2, 10, 10);
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

    public class Wall{
        private IntCoord pointA,pointB;
        public Orientation orientation;
        public Wall(int x, int y, Orientation o){
            pointA = new IntCoord(x,y);
            switch(o){
                case HORIZ:
                    pointB = new IntCoord(x+1,y);
                    break;
                case VERT:
                    pointB = new IntCoord(x,y + 1);
                    break;
                default:
                    pointB =  pointA.copy();
                    break;
            }
            orientation = o;
        }

        public void remove(){
            orientation = Orientation.OPEN;
        }

        public boolean isOpen(){
            return orientation == Orientation.OPEN;
        }


        public IntCoord getPointA(){return pointA;}
        public IntCoord getPointB(){return pointB;}

        public void draw(){
            if(orientation == Orientation.OPEN) return;
            float x1 = pointA.getFloatCoord().getX() , x2 = pointB.getFloatCoord().getX(),
                    y1 = pointA.getFloatCoord().getY() , y2 = pointB.getFloatCoord().getY();

            stroke(0);
            strokeWeight(2);
            line(x1,y1,x2,y2);
        }

    }

    public enum Orientation{
        OPEN,HORIZ,VERT
    }



}