
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
    NONE(0),
    CONNECTIONESTABLISH_CLIENTREQUEST(1),
    ONNECTIONESTABLISH_CLIENT_USERNAMEDUPLICATE_RESPONSE(2),
    CONNECTIONESTABLISH_SERVERESPONSE(3),
    CONNECTIONESTABLISH_SERVERESPONSE_OTHERPLAYERS(4),
    CONNECTIONESTABLISH_SERVERESPONSE_PREVIOUSPLAYERS(5),
    BIDDING_SERVERREQUEST(6),
    BIDDING_CLIENTRESPONSE(7),
    DEAL_CARDS_TO_CLIENT(8),
    DEAL_CARD_TO_SERVER(9),
    PLAYGAME_SERVERREQUEST(10),
    PLAYGAME_CLIENTRESPONSE(11),
    PLAYGAME_SERVERRESPONSE(12),
    PLAYGAME_SERVERRESPONSE_PLAYER_WON_TRICK(13),
    PLAYGAME_SERVERRESPONSE_TEAM_WON_GAME(14);

    private final int value;

    private MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

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
                messageType = MessageType.PLAYGAME_SERVERRESPONSE_TEAM_WON_GAME;
                break;
            default:
                break;
        }

        return messageType;
    }

}
