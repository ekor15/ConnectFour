package con4.model;

import con4.internal.CustomWebApplicationException;
import con4.internal.JsonAbleObject;

import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ekor on 02/11/2014.
 */
public class Game extends JsonAbleObject {
    private final int gameId;
    private GameStatus status;
    private List<Player> players = new LinkedList<Player>();
    private final int numOfPlayers;
    private int turnPlayer ;
    private Board board = new Board();

    /** enum that represent the game status.
     *  the entries contain a int code: 0-99 in game, 100-199 completed, 200-* other
     */
    public static enum GameStatus {
        ACTIVE(0),COMPLETED_WIN(100),COMPLETED_FULL(101),CANCELED(200);
        private int code;
        private GameStatus(int code){
            this.code = code;
        }
        public int getCode(){
            return this.code;
        }
    }

    /**
     * constructor
     * @param gameId game id - provided by game repository
     * @param player creator player
     * @param numOfPlayers number of players - for possible more players
     */
    public Game (int gameId,Player player,int numOfPlayers){
        this.gameId = gameId;
        this.numOfPlayers = numOfPlayers;
        this.players.add(player);
        this.turnPlayer = 0;
        this.status = GameStatus.ACTIVE;
    }

    //------- game changes ---------------------------------------------------------------------------------------------

    public synchronized void joinGame(Player player) throws CustomWebApplicationException {
        if (this.players.size() >= this.numOfPlayers){
            throw new CustomWebApplicationException("cannot join in, game is full", Response.Status.BAD_REQUEST);
        }
        if (this.getGamePlayerIndex(player)!=-1){
            throw new CustomWebApplicationException(String.format("user %s is already in the game",player.getUser()), Response.Status.BAD_REQUEST);
        }
        this.players.add(player);
    }

    /**
     * check if player is in game
     * @param player player to check
     * @return index of player or -1 if player not in player list
     */
    public int getGamePlayerIndex(Player player){
        return this.getPlayers().indexOf(player);
    }

    /** fill the player turn to the appropriate column
     * @param colIndex the column
     * @param player player. the player index in player list + 1 is the value that is inserted
     * @throws CustomWebApplicationException */
    public synchronized void takeTurn(int colIndex,Player player)throws CustomWebApplicationException{
        int playerIndex = this.getGamePlayerIndex(player);
        if (playerIndex==-1){
            throw new CustomWebApplicationException(String.format("user %s is not in the game",player.getUser()), Response.Status.FORBIDDEN);
        }
        if (this.turnPlayer != playerIndex){
            // used forbidden even though it implies not to try again
            throw new CustomWebApplicationException(String.format("not user %s turn",player.getUser()), Response.Status.FORBIDDEN);
        }
        Move move;
        try {
            move = this.board.takeTurn(playerIndex+1,colIndex);
            validateGameState(move);
        }catch (ArrayIndexOutOfBoundsException e){
            throw new CustomWebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        }
    }

    private void validateGameState(Move move)throws CustomWebApplicationException{
        if (move.isSuccessful()) {
            this.setTurnPlayer();
            if (this.board.checkWinMove(move.getPlayerSymb(),move.getCol(),move.getRow())){
                this.setStatus(GameStatus.COMPLETED_WIN);
                this.getPlayers().get(move.getPlayerIndex()).addWinCount();
            }
            if (this.board.isBoardFull()){
                this.setStatus(GameStatus.COMPLETED_FULL);
            }
        }else{
            throw new CustomWebApplicationException("illegal move - column is full", Response.Status.BAD_REQUEST);
        }
    }






    //------- game properties ---------------------------------------------------------------------------------------------
    /** game id */
    public int getGameId() {
        return gameId;
    }

    /** status of the game */
    public GameStatus getStatus() {
        return status;
    }
    private void setStatus(GameStatus status) {
        this.status = status;
    }
    /** array of players in the game */
    public List<Player> getPlayers() {
        return players;
    }

    /** number of players */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /** current player turn */
    public int getTurnPlayer() {
        return turnPlayer;
    }
    private void setTurnPlayer() {
        this.turnPlayer = (this.turnPlayer+1) % numOfPlayers;
    }

    /** board of the game */
    public Board getBoard() {
        return board;
    }
    public boolean isGameOver(){
        return (this.getStatus().getCode()>=100);
    }

}
