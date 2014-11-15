package con4.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by ekor on 02/11/2014.
 */
public class Board {
    /** symbol of empty position */
    private final static int EMPTY_SYMB = 0;
    /** number of columns in board */
    private final static int NUM_COLUMNS = 7;
    /** number of rows in board */
    private final static int NUM_ROWS = 6;
    /** the win sequence length */
    private final static int WIN_LENGTH = 4;
    private Column[] boardArr;

    /** constructor - the keys are static for simplicity */
    public Board(){
        this.boardArr = new Column[NUM_COLUMNS];
        for (int i = 0 ; i< boardArr.length;i++){
            boardArr[i] = new Column(NUM_ROWS);
        }
    }

    //------- validate move --------------------------------------------------------------------------------------------

    /** check if move is possible and take it
     * @param playerSymb representation of the player to be stored in column
     * @param colIndex column num to add move to
     * @return Move object if move was successful */
    public Move takeTurn(int playerSymb,int colIndex)throws ArrayIndexOutOfBoundsException{
        if (colIndex<0||colIndex>=NUM_COLUMNS){
            throw new ArrayIndexOutOfBoundsException(colIndex);
        }
        return new Move(playerSymb,colIndex,this.boardArr[colIndex].move(playerSymb));
    }




    //------- validate win ---------------------------------------------------------------------------------------------

    /**
     * check board for win condition
     * @param playerSymbol the player identifier
     * @return true if condition found on board
     */
    public boolean checkWinBoard(int playerSymbol){
        boolean hasWinner= false;
        // columns, main diagonals (X,0)
        int col=0;
        while (col<this.boardArr.length && !hasWinner){
            if (checkWinDirection(playerSymbol, 0, 1, col, 0)// col |
                    || checkWinDirection(playerSymbol, -1, 1, col, 0)// left \
                    || checkWinDirection(playerSymbol, 1, 1, col, 0))// right /
            {
                hasWinner = true;
            }
            col = col + 1;
        }
        if (!hasWinner) {
            // rows, sides diagonals
            int row = 1;
            hasWinner = checkWinDirection(playerSymbol, 1, 0, 0, row);
            while (row < this.boardArr[0].getLength() && !hasWinner) {
                if (checkWinDirection(playerSymbol, 1, 0, 0, row)// row -
                        || checkWinDirection(playerSymbol, -1, 1, this.boardArr.length - 1, row)// left \
                        || checkWinDirection(playerSymbol, -1, -1, 0, row))// right /
                {
                    hasWinner = true;
                }
                row = row + 1;
            }
        }
        return hasWinner;
    }

    /**
     * check for win condition on a position column, row and diagonals
     * @param playerSymb the player identifier
     * @param col pos column
     * @param row pos row
     * @return true if win condition present in one or more of the directions
     */
    public boolean checkWinMove(int playerSymb, int col, int row){
        boolean hasWin = false;
        int leftDiagRow = Math.max(col+row - this.boardArr.length-1,0);
        int leftDiagCol = Math.min(col+row,this.boardArr.length);
        int rightDiagRow = Math.max(row - col, 0);
        int rightDiagCol =  Math.max(col-row,0);

        if (checkWinDirection(playerSymb, 0, 1, col, 0)
            || checkWinDirection(playerSymb, 1, 0, 0, row)
            || checkWinDirection(playerSymb, -1, 1, leftDiagCol, leftDiagRow)
            || checkWinDirection(playerSymb, 1, 1, rightDiagCol, rightDiagRow))
        {
            hasWin = true;
        }
        return hasWin;
    }

    /**
     * check the direction given for win condition
     * @param playerSymbol the player identifier
     * @param colMove horizontal move
     * @param rowMove vertical move
     * @param sCol starting column
     * @param sRow starting row
     * @return if direction contains a win
     */
    public boolean checkWinDirection(int playerSymbol, int colMove, int rowMove, int sCol, int sRow){
        boolean res = false;
        int counter = 0;
        if (!(rowMove==0 && colMove==0)) {
            while (counter < WIN_LENGTH && 0 <= sCol && sCol < this.boardArr.length && 0 <= sRow && sRow < this.boardArr[0].getLength()) {
                if (this.boardArr[sCol].valueOf(sRow) == playerSymbol){
                    counter = counter + 1;
                }else {
                    counter = 0;
                }
                sCol = sCol + colMove;
                sRow = sRow + rowMove;
            }
        }
        if (counter == WIN_LENGTH){
            res = true;
        }
        return res;
    }


    //------- validate board -------------------------------------------------------------------------------------------


    /**
     * check if there are no free spaces in the board
     * @return true is
     */
    @JsonIgnore
    public boolean isBoardFull(){
        boolean hasRoom = false;
        int i = 0 ;
        while (i< this.boardArr.length && !hasRoom)
        {
            hasRoom = !this.boardArr[i].isFull();
            i= i + 1;
        }
        return !hasRoom;
    }

//    /**
//     * check if move is possible
//     * @return
//     */
//    private boolean validateMove(int col){
//        return
//    }

    /** the columns in the board */
    public Column[] getBoardArr(){
        return this.boardArr;
    }

}
