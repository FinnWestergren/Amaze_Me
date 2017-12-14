package cashflow.getmoney.amazeme;

/**
 * Created by hlsho_000 on 12/13/2017.
 */

public class Maze {
    private int index;
    private String completionDate;
    private int height;
    private int width;
    private int score;
    private String mazeString;

    public Maze(int index, String completionDate, int height, int width, int score, String mazeString) {
        this.index = index;
        this.completionDate = completionDate;
        this.height = height;
        this.width = width;
        this.score = score;
        this.mazeString = mazeString;
    }



}
