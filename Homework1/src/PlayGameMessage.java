
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

    PlayGameMessage(String playerName) {
        super(playerName);
    }

    PlayGameMessage(String playerName, String card) {
        super(playerName);
        this.card = card;
    }

    String getCard() {
        return this.card;
    }

    Panel getPanel() {
        return this.panel;
    }
}
