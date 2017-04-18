package model;

/**
 * Created by Andre on 18.04.2017.
 */
public class Patterns {
    protected static int[][] glider;
    protected static int[][] exploder;
    protected static int[][] painting;
    protected static int[][] smallExploder;
    protected static int[][] penta;
    protected static int[][] bunny;
    protected static int[][] geysir;

    static{
        glider = new int[][] {{0,0,1},{1,0,1},{0,1,1}};

        exploder = new int[][] {{1,0,1},
                                {1,0,1},
                                {1,0,1}};

        painting = new int[][] {{1,1,1},{1,0,1},{1,0,1},{1,0,1},{1,0,1},{1,0,1},{1,0,1},{1,0,1},{1,1,1}};
                /*
        smallExploder = new int[][]
        penta = new int[][]
        bunny = new int[][]
        geysir = new int[][]
        */
    }

}
