
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
public class PlayerMessage implements Serializable {

    /**
     * The player name
     */
    private final String playerName;

    /**
     * Sets the player name
     *
     * @param playerName
     */
    public PlayerMessage(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the player name
     *
     * @return
     */
    public String getPlayerName() {
        return this.playerName;
    }
}
