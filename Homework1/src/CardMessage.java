
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

    /**
     * The card
     */
    private String card;

    /**
     * Initial set of cards
     */
    private ArrayList<String> initialSetOfCards;

    /**
     * Returns the card.
     */
    String getCard() {
        return this.card;
    }

    /**
     * Construct the message.
     */
    CardMessage(String playerName, ArrayList<String> initialSetOfCards) {
        super(playerName);
        this.initialSetOfCards = initialSetOfCards;
    }

    /**
     * Get the initial set of cards
     */
    ArrayList<String> getInitialSetOfCards() {
        return this.initialSetOfCards;
    }

}
