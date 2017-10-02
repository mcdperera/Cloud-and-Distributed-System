
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
public enum ErrorMessageType implements Serializable {

    /**
     * None error message type.
     */
    NONE(0),

    /**
     * Username exists error message type.
     */
    USERNAME_EXISTS(1),

    /**
     * bidding large amount error message type.
     */
    BIDDING_LARGERBID(2),

    /**
     * Play cheat card error message type.
     */
    PLAY_CHEATCARD(3);
    
    /**
     * the value
     */
    private final int value;

    
    /**
     * Construct of the error message type.
     */
    private ErrorMessageType(int value) {
        this.value = value;
    }

    /**
     * Returns the value.
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * 
     * @param value
     * @return
     */
    public static ErrorMessageType getEnum(int value) {
        ErrorMessageType messageType = ErrorMessageType.NONE;
        switch (value) {
            case 1:
                messageType = ErrorMessageType.USERNAME_EXISTS;
                break;
            case 2:
                messageType = ErrorMessageType.BIDDING_LARGERBID;
                break;
            case 3:
                messageType = ErrorMessageType.PLAY_CHEATCARD;
                break;
            default:
                break;
        }

        return messageType;
    }

    /**
     *
     * @param value
     * @return
     */
    public static String getErrorMessgae(int value) {
        String errorMessage = "";
        switch (value) {
            case 1:
                errorMessage = "Supplied username already exists";
                break;
            case 2:
                errorMessage = "Your team bidding is too large";
                break;  
            case 3:
                errorMessage = "You draw a worng card";
                break;

            default:
                break;
        }

        return errorMessage;
    }
}
