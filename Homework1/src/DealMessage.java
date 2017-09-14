
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

    private String selectedCard;

    DealMessage(String playerName, String selectedCard) {
        super(playerName);
        this.selectedCard = selectedCard;
    }

    DealMessage(String playerName) {
        super(playerName);
    }
}
