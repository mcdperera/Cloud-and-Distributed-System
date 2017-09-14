
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
public class PlayerMessage implements Serializable  {

    private String playerName;

    public PlayerMessage(String playerName)
    {
        this.playerName = playerName;
    }
    
    public String getPlayerName() {
        return this.playerName;
    }
}
