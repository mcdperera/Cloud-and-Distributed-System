
import java.io.*;

public class Message implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    private MessageType type;

    private boolean isClientToServer;

    private String message;

    private ConnectionMessage connectionMessage;

    private BiddingMessage biddingMessage;

    private CardMessage cardMessage;

    private PlayGameMessage playGameMessage;

    private boolean isError;

    private int errorType;

    // constructor
    Message(int type, boolean isClientToServer, String message,
            boolean isError, int errorType) {
        this.type = MessageType.getEnum(type);
        this.isClientToServer = isClientToServer;
        this.message = message;
        this.isError = isError;
        this.errorType = errorType;
    }

    // getters
    MessageType getType() {
        return type;
    }

    String getMessage() {
        return message;
    }

    ConnectionMessage getConnectionMessage() {
        return this.connectionMessage;
    }

    void setConnectionMessage(ConnectionMessage connectionMessage) {
        this.connectionMessage = connectionMessage;
    }

    BiddingMessage getBiddingMessage() {
        return this.biddingMessage;
    }

    void setBiddingMessage(BiddingMessage biddingMessage) {
        this.biddingMessage = biddingMessage;
    }

    CardMessage getCardMessage() {
        return this.cardMessage;
    }

    void setCardMessage(CardMessage cardMessage) {
        this.cardMessage = cardMessage;
    }

    PlayGameMessage getPlayGameMessage() {
        return this.playGameMessage;
    }

    void setPlayGameMessage(PlayGameMessage playGameMessage) {
        this.playGameMessage = playGameMessage;
    }

    boolean isError() {
        return isError;
    }

    int getErrorType() {
        return errorType;
    }

    String getErrorMessage() {
        return ErrorMessageType.getErrorMessgae(this.errorType);
    }
}
