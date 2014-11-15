package con4.model;

import con4.internal.JsonAbleObject;

/**
 * Created by ekor on 03/11/2014.
 */
public class Player extends JsonAbleObject {
    private final String user;
    private String name;
    private int winCount;

    /** constructor */
    public Player(String user, String name){
        this.user = user;
        this.name = name;
        this.winCount = 0;
    }


    /** player id - should be UUID and int but sines no db keeping it simple */
    public String getUser() {
        return user;
    }

    /** player name */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /** number of winnings */
    public int getWinCount() {
        return winCount;
    }
    public void addWinCount() {
        this.winCount++;
    }

}
