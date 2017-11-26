
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
     * exit message type
     */
    EXIT(1),
    /**
     * ping message type
     */
    PING(2),
    /**
     * ping response message type
     */
    PINGRESPONSE(3),
    /**
     * search message type
     */
    SEARCH(4);
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
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the message type.
     *
     * @param value
     * @return
     */
    public static MessageType getEnum(int value) {
        MessageType messageType = MessageType.NONE;
        switch (value) {
            case 0:
                messageType = MessageType.NONE;
                break;
            case 1:
                messageType = MessageType.EXIT;
                break;
            case 2:
                messageType = MessageType.PING;
                break;
            case 3:
                messageType = MessageType.PINGRESPONSE;
                break;

            default:
                break;
        }

        return messageType;
    }

}
