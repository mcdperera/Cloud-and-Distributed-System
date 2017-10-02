
import java.io.*;

/**
 *
 * @author 502759576
 */
public class Message implements Serializable {

    /**
     * The serial version UID
     */
    protected static final long serialVersionUID = 1112122200L;

    /**
     * The message type.
     */
    private MessageType type;

    /**
     * The the way of message sending
     */
    private boolean isClientToServer;

    /**
     * The message
     */
    private String message;

    /**
     * The Connection message
     */
    private ConnectionMessage connectionMessage;

    /**
     * The Bidding message
     */
    private BiddingMessage biddingMessage;

    /**
     * The card message
     */
    private CardMessage cardMessage;

    /**
     * The play game message
     */
    private PlayGameMessage playGameMessage;

    /**
     * The game stat message
     */
    private GameStatMessage gameStatMessage;

    /**
     * The match stst message
     */
    private String matchStatMessage;

    /**
     * The Match won message
     */
    private MatchWonMessage matchWonMessage;

    /**
     * Whether telling there is an error or not.
     */
    private boolean isError;

    /**
     * The error type.
     */
    private int errorType;

    /**
     * Construct the message.
     */
    Message(int type, boolean isClientToServer, String message,
            boolean isError, int errorType) {
        this.type = MessageType.getEnum(type);
        this.isClientToServer = isClientToServer;
        this.message = message;
        this.isError = isError;
        this.errorType = errorType;
    }

    /**
     * Returns the type of the message.
     */
    MessageType getType() {
        return type;
    }

    /**
     * Returns the message.
     */
    String getMessage() {
        return message;
    }

    /**
     * Returns connection message.
     */
    ConnectionMessage getConnectionMessage() {
        return this.connectionMessage;
    }

    /**
     * Setting the connection message
     */
    void setConnectionMessage(ConnectionMessage connectionMessage) {
        this.connectionMessage = connectionMessage;
    }

    /**
     * Returns the bidding message
     */
    BiddingMessage getBiddingMessage() {
        return this.biddingMessage;
    }

    /**
     * Setting bidding message
     */
    void setBiddingMessage(BiddingMessage biddingMessage) {
        this.biddingMessage = biddingMessage;
    }

    /**
     * Returns card game message.
     */
    CardMessage getCardMessage() {
        return this.cardMessage;
    }

    /**
     * Setting card message.
     */
    void setCardMessage(CardMessage cardMessage) {
        this.cardMessage = cardMessage;
    }

    /**
     * Returns play game message.
     */
    PlayGameMessage getPlayGameMessage() {
        return this.playGameMessage;
    }

    /**
     * Setting play game message.
     */
    void setPlayGameMessage(PlayGameMessage playGameMessage) {
        this.playGameMessage = playGameMessage;
    }

    /**
     * Returns the game stat message.
     */
    GameStatMessage getGameStatMessage() {
        return this.gameStatMessage;
    }

    /**
     * Setting game game stat message.
     */
    void setGameStatMessage(GameStatMessage gameStatMessage) {
        this.gameStatMessage = gameStatMessage;
    }

    /**
     * Returns the match stat message.
     */
    String getMatchStatMessage() {
        return this.matchStatMessage;
    }

    /**
     * Setting the match stat message.
     */
    void setMatchStatMessage(String matchStatMessage) {
        this.matchStatMessage = matchStatMessage;
    }

    /**
     * Returns the match won message.
     */
    MatchWonMessage getMatchWonMessage() {
        return this.matchWonMessage;
    }

    /**
     * Setting the match won message
     */
    void setMatchWonMessage(MatchWonMessage matchWonMessage) {
        this.matchWonMessage = matchWonMessage;
    }

    /**
     * Returns whether the message is an error or not.
     */
    boolean isError() {
        return isError;
    }

    /**
     * Returns the error type.
     */
    int getErrorType() {
        return errorType;
    }

    /**
     * Returns the error message
     */
    String getErrorMessage() {
        return ErrorMessageType.getErrorMessgae(this.errorType);
    }

}
