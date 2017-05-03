package model;

/**
 * Created by Andre on 18.04.2017.
 */
public class Patterns {


    public static int[][] glider, exploder, painting;


    /**
     *
     * Creates the static pre-made Patterns.
     *
     */
    static{
        glider = new int[][] {{0,0,1},{1,0,1},{0,1,1}};

        exploder = new int[][] {{1,1,1},
                                {1,0,1},
                                {1,1,1}};

        painting = new int[][] {{1,1,1},{1,0,1},{1,0,1},{1,0,1},{1,0,1},{1,0,1},{1,0,1},{1,0,1},{1,1,1}};

    }

}
