package con4.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * game column class
 */
public class Column {
    private final int length;
    private int numOccupied;
    private int[] colArr;

    /** constructor */
    public Column(int length){
        this.length = length;
        this.numOccupied = 0;
        colArr = new int[length];
    }

    /** check if col has no free spaces
     * @return true if full */
    @JsonIgnore
    public boolean isFull(){
        return this.getNumOccupied() >= this.getLength();
    }

    /** try to make move
     * @param playerSymb int symbol of the player
     * @return int index of the move and -1 if move not done */
    public int move(int playerSymb){
        int moveIndex = -1;
        if (!isFull()) {
            moveIndex = getNumOccupied();
            getColArr()[moveIndex] = playerSymb;
            addNumOccupied();
        }
        return moveIndex;
    }

    public int valueOf(int index){
        return this.getColArr()[index];
    }

    /** length of the column */
    public int getLength() {
        return length;
    }

    /** number of occupied positions */
    public int getNumOccupied() {
        return numOccupied;
    }
    private void addNumOccupied(){
        this.numOccupied = this.numOccupied+1;
    }
    /** column array */
    public int[] getColArr() {
        return colArr;
    }
}
