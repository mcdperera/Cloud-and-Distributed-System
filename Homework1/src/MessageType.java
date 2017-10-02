
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
public enum MessageType implements Serializable {

    /**
     * None message type
     */
    NONE(0),

    /**
     * Client request to connect to server message type
     */
    CONNECTIONESTABLISH_CLIENTREQUEST(1),

    /**
     * Server response to client that username duplicate message type
     */
    ONNECTIONESTABLISH_CLIENT_USERNAMEDUPLICATE_RESPONSE(2),

    /**
     * Connection well established  message type
     */
    CONNECTIONESTABLISH_SERVERESPONSE(3),

    /**
     * Connection established message to other player message type
     */
    CONNECTIONESTABLISH_SERVERESPONSE_OTHERPLAYERS(4),

    /**
     * Connection established message to previous player message type
     */
    CONNECTIONESTABLISH_SERVERESPONSE_PREVIOUSPLAYERS(5),

    /**
     * Bidding server request message type
     */
    BIDDING_SERVERREQUEST(6),

    /**
     * Bidding client response message type
     */
    BIDDING_CLIENTRESPONSE(7),

    /**
     * Deal cards to clients message type
     */
    DEAL_CARDS_TO_CLIENT(8),

    /**
     * Deal cards to server message type
     */
    DEAL_CARD_TO_SERVER(9),

    /**
     * Play game server request message type
     */
    PLAYGAME_SERVERREQUEST(10),

    /**
     * Play game client response message type
     */
    PLAYGAME_CLIENTRESPONSE(11),

    /**
     * Play game server response message type
     */
    PLAYGAME_SERVERRESPONSE(12),

    /** 
     * Player won trick server response message type
     */
    PLAYGAME_SERVERRESPONSE_PLAYER_WON_TRICK(13),

    /**
     * Team score server response message type
     */
    PLAYGAME_SERVERRESPONSE_TEAM_SCORE(14),

    /**
     * Round won with next round message type
     */
    PLAYGAME_SERVERRESPONSE_TEAM_WON_GAME_WITH_DEAL_CARDS(15),

    /**
     * Match won server response message type
     */
    PLAYGAME_SERVERRESPONSE_TEAM_WON_MATCH(16);

     /**
     * the value
     */
    private final int value;

     /**
     * Returns the message type.
     */
    private MessageType(int value) {
        this.value = value;
    }

    /**
     * Return the value.
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the message type.
     * @param value
     * @return
     */
    public static MessageType getEnum(int value) {
        MessageType messageType = MessageType.NONE;
        switch (value) {
            case 1:
                messageType = MessageType.CONNECTIONESTABLISH_CLIENTREQUEST;
                break;
            case 2:
                messageType = MessageType.ONNECTIONESTABLISH_CLIENT_USERNAMEDUPLICATE_RESPONSE;
                break;
            case 3:
                messageType = MessageType.CONNECTIONESTABLISH_SERVERESPONSE;
                break;
            case 4:
                messageType = MessageType.CONNECTIONESTABLISH_SERVERESPONSE_OTHERPLAYERS;
                break;
            case 5:
                messageType = MessageType.CONNECTIONESTABLISH_SERVERESPONSE_PREVIOUSPLAYERS;
                break;
            case 6:
                messageType = MessageType.BIDDING_SERVERREQUEST;
                break;
            case 7:
                messageType = MessageType.BIDDING_CLIENTRESPONSE;
                break;
            case 8:
                messageType = MessageType.DEAL_CARDS_TO_CLIENT;
                break;
            case 9:
                messageType = MessageType.DEAL_CARD_TO_SERVER;
                break;
            case 10:
                messageType = MessageType.PLAYGAME_SERVERREQUEST;
                break;
            case 11:
                messageType = MessageType.PLAYGAME_CLIENTRESPONSE;
                break;
            case 12:
                messageType = MessageType.PLAYGAME_SERVERRESPONSE;
                break;
            case 13:
                messageType = MessageType.PLAYGAME_SERVERRESPONSE_PLAYER_WON_TRICK;
                break;
            case 14:
                messageType = MessageType.PLAYGAME_SERVERRESPONSE_TEAM_SCORE;
                break;
            case 15:
                messageType = MessageType.PLAYGAME_SERVERRESPONSE_TEAM_WON_GAME_WITH_DEAL_CARDS;
                break;
            case 16:
                messageType = MessageType.PLAYGAME_SERVERRESPONSE_TEAM_WON_MATCH;
                break;
            default:
                break;
        }

        return messageType;
    }

}
