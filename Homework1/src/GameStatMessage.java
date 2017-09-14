
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Charmal
 */
public class GameStatMessage extends PlayerMessage implements Serializable {

    String[] getUsernames() {
        return this.usernames;
    }
    
    String[] getBids() {
        return this.bids;
    }
    
     String[] getWonTricks() {
        return this.wonTricks;
    }
  
    public String[] usernames = {"", "OSU", "OSU1", "OSU2", "OSU3"};
    
    public String[] bids = {"Bids", "5", "5", "5", "5"};
    
    public String[] wonTricks = {"Wontricks" ,"5", "5", "5", "5"};
    
    public GameStatMessage(String playerName) {
        super(playerName);
    }
    
    
    
}
