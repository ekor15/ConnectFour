package con4.data;

import con4.internal.CustomWebApplicationException;
import con4.model.Game;
import con4.internal.JsonAbleObject;
import con4.model.Player;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekor on 03/11/2014.
 */
public class GameRepository extends JsonAbleObject {

    private static GameRepository instance;
    private static Map<Integer,Game> gameRepo;
    private static PlayerRepository playerRepo;
    private static Integer gameIds;

    static {
        instance = new GameRepository();
        gameRepo = new HashMap<Integer, Game>();
        playerRepo = PlayerRepository.getInstance();
        gameIds = 0;
    }
    public static GameRepository getInstance(){
        return instance;
    }

    private GameRepository(){
    }

    //------- create games ------------------------------------------------------------------------------------------------

    /**
     * create game and add it to game repository if player exists
     * @return gameID
     */
    public synchronized Game createGame(String user) throws CustomWebApplicationException {
        if (user == null){
            throw new CustomWebApplicationException("please specify user",Response.Status.BAD_REQUEST);
        }
        Player player = playerRepo.getPlayer(user);
        if (player == null){
            throw new CustomWebApplicationException(String.format("Player %s does not exists.", user),Response.Status.NOT_FOUND);
        }
        Game game = new Game(gameIds,player,2);
        gameRepo.put(gameIds,game);
        gameIds = gameIds +1;
        return game;
    }



    //------- get games ------------------------------------------------------------------------------------------------

    /** get game by gameId
     * @param gameId game id
     * @return game json
     * @throws con4.internal.CustomWebApplicationException if game id no found */
    public Game getGame(int gameId) throws CustomWebApplicationException {
        Game game = gameRepo.get(gameId);
        if (game == null){
            throw new CustomWebApplicationException("game id not found",Response.Status.NOT_FOUND);
        }
        return game;
    }
    @Override
    public String toJSON(){
        return toJSON(gameRepo);
    }

}