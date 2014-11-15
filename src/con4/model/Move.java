package con4.model;

/**
 * class that represent a move
 */
public class Move {
    private final int playerSymb;
    private final int col;
    private final int row;

    public Move(int playerSymb, int col, int row) {
        this.playerSymb = playerSymb;
        this.col = col;
        this.row = row;
    }

    /** player representation for the board */
    public int getPlayerSymb() {
        return playerSymb;
    }

    /** column num */
    public int getCol() {
        return col;
    }

    /** row num */
    public int getRow() {
        return row;
    }

    /** player index in game player list */
    public int getPlayerIndex() {
        return playerSymb-1;
    }

    /** check if move have been made */
    public boolean isSuccessful(){
        return !(row == -1);
    }
}
