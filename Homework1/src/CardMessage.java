
import java.io.Serializable;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Charmal
 */
public class CardMessage extends PlayerMessage implements Serializable {

    private String card;
    private ArrayList<String> initialSetOfCards;

    String getCard() {
        return this.card;
    }

    CardMessage(String playerName, ArrayList<String> initialSetOfCards) {
        super(playerName);
        this.initialSetOfCards = initialSetOfCards;
    }

    ArrayList<String> getInitialSetOfCards() {
        return this.initialSetOfCards;
    }

}
