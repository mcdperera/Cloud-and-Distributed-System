
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
public class BiddingMessage extends PlayerMessage implements Serializable {

    /**
     * The bidding amount
     */
    private final Integer amount;

    /**
     * Construct the Bidding message.
     */
    BiddingMessage(String playerName, Integer amount) {
        super(playerName);
        this.amount = amount;
    }

    /**
     * Returns the bidding amount
     */
    Integer getAmount() {
        return this.amount;
    }

}
