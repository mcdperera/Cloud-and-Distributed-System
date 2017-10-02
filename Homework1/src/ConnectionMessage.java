
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
public class ConnectionMessage extends PlayerMessage implements Serializable {

    /**
     * the username
     */
    private String username;

    /**
     * Construct of the connection message.
     */
    ConnectionMessage(String playerName, String username) {
        super(playerName);
        this.username = username;
    }

    /**
     * Returns the username
     */
    String getUsername() {
        return username;
    }

}
