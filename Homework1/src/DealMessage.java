
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
public class DealMessage extends PlayerMessage implements Serializable {

    
    /**
     * the card that selected to deal the cards.
     */
    private String selectedCard;

    
    /**
     * Construct the deal message
     */
    DealMessage(String playerName, String selectedCard) {
        super(playerName);
        this.selectedCard = selectedCard;
    }

    
    /**
     * Construct the deal message.
     */
    DealMessage(String playerName) {
        super(playerName);
    }
}
