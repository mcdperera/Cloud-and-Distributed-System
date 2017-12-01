
import java.io.*;

public class GameMessage implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    static final int MESSAGE = 0, SERVER_RESPONSE = 1, START_GAME = 2, PLAY_GAME = 3, GAME_WON = 4, LOGOUT = 5;

    private int type;
    private String message;
    private String username;  
    private String location;
    private String removeLocation;
    private boolean isFirstUser;

    // constructor
    GameMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    GameMessage(int type, String message, String username) {
        this.type = type;
        this.message = message;
        this.username = username;
    }

    GameMessage(int type, String message, String username, String location, String removeLocation) {
        this.type = type;
        this.message = message;
        this.username = username;
        this.location = location;
        this.removeLocation = removeLocation;
    }

    GameMessage(int type, String message, String username, boolean isFirstUser, String location, String removeLocation) {
        this.type = type;
        this.message = message;
        this.username = username;
        this.isFirstUser = isFirstUser;
        this.location = location;
        this.removeLocation = removeLocation;
    }

    // getters
    int getType() {
        return type;
    }

    String getMessage() {
        return message;
    }

    String getUsername() {
        return this.username;
    }

    String getLocation() {
        return location;
    }

    String getRemoveLocation() {
        return removeLocation;
    }

    boolean getIsFirstUser() {
        return this.isFirstUser;
    }
}
