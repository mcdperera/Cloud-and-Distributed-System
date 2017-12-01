
import java.io.*;

/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no 
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class Message implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    private MessageType type;

    private PingMessage pingMessage;

    private SearchMessage searchMessage;

    private String words;

    Message(MessageType type, String words) {
        this.type = type;
        this.words = words;
    }

    // getters
    MessageType getType() {
        return type;
    }

    String getWords() {
        return words;
    }

    void setPingMessage(PingMessage pingMessage) {
        this.pingMessage = pingMessage;
    }

    PingMessage getPingMessage() {
        return this.pingMessage;
    }

    void setSearchMessage(SearchMessage searchMessage) {
        this.searchMessage = searchMessage;
    }

    SearchMessage getSearchMessage() {
        return this.searchMessage;
    }
}
