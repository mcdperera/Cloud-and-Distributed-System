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
    NONE(0),
    USERNAME_EXISTS(1),    
    BIDDING_LARGERBID(2),
    DEAL_CHEATCARD(3);

    private final int value;

    private ErrorMessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ErrorMessageType getEnum(int value) {
        ErrorMessageType messageType = ErrorMessageType.NONE;
        switch (value) {
            case 1:
                messageType = ErrorMessageType.USERNAME_EXISTS;
                break;
            case 2:
                messageType = ErrorMessageType.BIDDING_LARGERBID;
                break;
            
            default:
                break;
        }

        return messageType;
    }

     public static String getErrorMessgae(int value) {
        String errorMessage = "";
        switch (value) {
            case 1:
                errorMessage = "Supplied username already exists";
                break;
            case 2:
                errorMessage = "Your team bidding is too large";
                break;
            
            default:
                break;
        }

        return errorMessage;
    }
}
