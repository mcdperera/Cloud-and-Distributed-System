
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

    private String username;

    ConnectionMessage(String playerName, String username) {
        super(playerName);
        this.username = username;
    }

    String getUsername() {
        return username;
    }

}
