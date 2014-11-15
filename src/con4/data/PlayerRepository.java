package con4.data;

import con4.internal.CustomWebApplicationException;
import con4.internal.JsonAbleObject;
import con4.model.Player;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekor on 03/11/2014.
 */

public class PlayerRepository extends JsonAbleObject {

    private static PlayerRepository instance;
    private static Map<String,Player> playerRepo;
    static{
        instance = new PlayerRepository();
        playerRepo = new HashMap<String, Player>();
    }
    /** singleton pattern - need to correct this as the private constructor is not possible */
    public static PlayerRepository getInstance(){
        return instance;
    }
    private PlayerRepository(){}


    //-------------------------- create players --------------------------
    /** add the player to the player repository if user not in
     * @param user login user name
     * @param name name of player
     * @return player obj */
    public synchronized Player createPlayer(String user,  String name) throws CustomWebApplicationException {
        Player player;
        if (user==null || name == null){
            throw new CustomWebApplicationException("please specify user and name params",Response.Status.BAD_REQUEST);
        }
        if (playerRepo.containsKey(user)){
            throw new CustomWebApplicationException(String.format("player %s already exists", user),Response.Status.BAD_REQUEST);
        }
        player = new Player(user,name);
        playerRepo.put(user,player);
        return player;
    }

    //-------- get players ---------------------------------------------------------------------------------------------
    /** get player for in program operations
     * @param user user name to get
     * @return player json
     * @throws CustomWebApplicationException returns appropriate response for possible errors */
    public Player getPlayer( String user) throws CustomWebApplicationException {
        if (user == null){
            throw new CustomWebApplicationException("please specify user param",Response.Status.BAD_REQUEST);
        }
        Player player = playerRepo.get(user);
        if (player == null){
            throw new CustomWebApplicationException(String.format("player %s does not exists", user),Response.Status.NOT_FOUND);
        }
        return player;
    }
    @Override
    public String toJSON(){
        return toJSON(playerRepo);
    }
}
