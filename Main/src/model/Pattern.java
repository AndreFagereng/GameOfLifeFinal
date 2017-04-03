package model;

/**
 * Created by Andre on 31.03.2017.
 */
public class Pattern {




    int[][] test = {{1,0,1},
                    {0,1,0},
                    {1,0,1}};



    public void testMethod(int[][] array, Board board){

        for(int x = 0; x<array.length;x++){
            for(int y = 0; y<array.length; y++){
                if(array[x][y] == 1){
                    board.cellGrid[x][y].setState(true);
                }else if(array[x][y] == 0){
                    board.cellGrid[x][y].setState(false);
                }

            }
        }
    }
}
