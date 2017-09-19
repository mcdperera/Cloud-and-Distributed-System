
import java.awt.Panel;
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 502759576
 */
public class PlayGameMessage extends PlayerMessage implements Serializable {

    private String card;
    private Panel panel;
    private String username;
    private String team;

    PlayGameMessage(String playerName) {
        super(playerName);
    }

    PlayGameMessage(String playerName, String card) {
        super(playerName);
        this.card = card;
    }

    PlayGameMessage(String playerName, String username, String team) {
        super(playerName);
        this.username = username;
        this.team = team;
    }

    String getCard() {
        return this.card;
    }

    Panel getPanel() {
        return this.panel;
    }

    String getUsername() {
        return username;
    }
    
    String getTeam() {
        return team;
    }
}
