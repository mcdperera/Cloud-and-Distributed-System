
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

    /**
     * The card
     */
    private String card;
    
    /**
     * The panel
     */
    private Panel panel;
    
    /**
     * The username
     */
    private String username;
    
    /**
     * The team
     */
    private String team;

    /**
     * Construct Play Game Message
     */
    PlayGameMessage(String playerName) {
        super(playerName);
    }

    /**
     * Construct Play Game Message
     */
    PlayGameMessage(String playerName, String card) {
        super(playerName);
        this.card = card;
    }

    /**
     * Construct Play Game Message
     */
    PlayGameMessage(String playerName, String username, String team) {
        super(playerName);
        this.username = username;
        this.team = team;
    }

    /**
     * Returns the card.
     */
    String getCard() {
        return this.card;
    }

    /**
     * Returns the panel
     */
    Panel getPanel() {
        return this.panel;
    }

    /**
     * Returns the username
     */
    String getUsername() {
        return username;
    }

    /**
     * Returns the team
     */
    String getTeam() {
        return team;
    }
}
