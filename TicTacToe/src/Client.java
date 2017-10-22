
import java.net.*;
import java.io.*;
import java.util.*;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client {

    // input socket stream
    private ObjectInputStream sInput;

    // output socket stream
    private ObjectOutputStream sOutput;

    // the socket
    private Socket socket;

    // the server, the port and the username
    private String server;

    // the port no
    private int port;

    // the logged username
    private static String Username;

    // first player saved locations
    public static List<String> firstPlayerLocations = new ArrayList<>(3);

    // second player saved locations
    public static List<String> secondPlayerLocations = new ArrayList<>(3);

    // is user logout after wining or loosing game
    private static boolean isLogoutCalled = false;

    private static boolean isFirstUser = false;

    /*
	 *  Constructor called by console mode
	 *  server: the server address
	 *  port: the port number
	 *  username: the username
     */
    Client(String server, int port, String username) {
        // which calls the common constructor with the GUI set to null
        this.server = server;
        this.port = port;
        this.Username = username;
    }

    /*
	 * To start the dialog
     */
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        } // if it failed not much I can so
        catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ListenFromServer().start();

        try {
            sOutput.writeObject(Username);
        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }

        return true;
    }

    private void display(String msg) {
        System.out.println(msg);
    }

    void sendMessage(GameMessage msg) {
        try {
            if (socket != null) {
                sOutput.writeObject(msg);
            }
        } catch (IOException e) {
            display("Exception writing to server: " + e);
        }
    }

    private void disconnect() {
        try {
            if (sInput != null) {
                sInput.close();
            }
        } catch (Exception e) {
        }
        try {
            if (sOutput != null) {
                sOutput.close();
            }
        } catch (Exception e) {
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        }

    }

    public static void main(String[] args) {

        int portNumber = 1500;

        String serverAddress = "localhost";

        System.out.println("Enter your username: ");
        Scanner scanner = new Scanner(System.in);

        Client client = new Client(serverAddress, portNumber, scanner.nextLine());

        if (!client.start()) {
            return;
        }

        if (isLogoutCalled) {
            client.disconnect();
        }

    }

    private static void displayNewBoardArrangement() {

        System.out.println("# new arrangement");

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {

                String location = i + "," + j;

                boolean isInFirstList = firstPlayerLocations.contains(location);

                if (isInFirstList) {
                    if (j == 3) {
                        System.out.print("x");
                    } else {
                        System.out.print("x|");
                    }

                } else {
                    boolean isInSecondList = secondPlayerLocations.contains(location);
                    if (isInSecondList) {
                        if (j == 3) {
                            System.out.print("o");
                        } else {
                            System.out.print("o|");
                        }
                    } else if (j == 3) {
                        System.out.print(" ");
                    } else {
                        System.out.print(" |");
                    }
                }
            }

            System.out.println("");
        }

    }

    private static void displayInstructionGameBoard() {

        System.out.println("************************");
        System.out.println("****Instruction(s)******");
        System.out.println("Enter the locations");

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                System.out.print(i + "," + j + "|");
            }
            System.out.print("\n");
        }

    }

    class ListenFromServer extends Thread {

        public void run() {
            while (true) {
                try {

                    GameMessage message = (GameMessage) sInput.readObject();

                    switch (message.getType()) {
                        case GameMessage.SERVER_RESPONSE:

                            display(message.getMessage());

                            break;

                        case GameMessage.START_GAME:

                            displayInstructionGameBoard();

                            if (message.getUsername().toLowerCase().equals(Username.toLowerCase())) {
                                playGame(false);
                            }

                            break;
                        case GameMessage.PLAY_GAME:

                            isFirstUser = message.getIsFirstUser();

                            setupSavedLocations(message);

                            displayNewBoardArrangement();

                            if (message.getUsername().toLowerCase().equals(Username.toLowerCase())) {
                                playGame(false);
                            } else {
                                display("Wait until other player select the move ");
                            }

                            break;

                        case GameMessage.GAME_WON:

                            setupSavedLocations(message);

                            displayNewBoardArrangement();

                            display(message.getMessage());

                            callLogOut();

                            break;
                    }

                } catch (Exception e) {
                    display("Server has close the connection ");

                    break;
                }

            }
        }

        private void playGame(boolean isError) {

            if (isError) {
                display("Either wrong entry selection or already selected location ");
            } else {
                display(Username + (isFirstUser ? " 'o'" : " 'x'")
                        + ", enter your move (row[1-3] column[1-3]) format : ");
            }

            Scanner scanner = new Scanner(System.in);
            String location = scanner.nextLine();

            if (isNotDuplicateLocation(location)) {
                playGame(true);
            } else if (isNotValidate(location)) {
                playGame(true);
            } else {

                GameMessage gameMessage = new GameMessage(GameMessage.PLAY_GAME,
                        Username + " selects - " + location, Username, location);

                sendMessage(gameMessage);
            }
        }

        // Check whether the move location is not duplicated one
        private boolean isNotDuplicateLocation(String location) {

            return firstPlayerLocations.contains(location)
                    || secondPlayerLocations.contains(location);
        }

        // Checking the entry location is correct
        private boolean isNotValidate(String location) {

            if (location.length() != 3) {
                return true;
            }

            String s1 = location.substring(0, 1);
            String s2 = location.substring(2, 3);

            if (!tryParseInt(s1) || !tryParseInt(s2)) {
                return true;
            } else {

                Integer i1 = Integer.parseInt(s1);
                Integer i2 = Integer.parseInt(s2);

                if (!(1 <= i1 && i1 <= 3)) {
                    return true;
                }

                if (!(1 <= i2 && i2 <= 3)) {
                    return true;
                }

                return false;
            }

        }

        // Try to parse String value to integer
        boolean tryParseInt(String value) {
            try {
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // Depending on the server call it setups the locations 
        private void setupSavedLocations(GameMessage message) {

            if (message.getIsFirstUser()) {
                firstPlayerLocations.add(message.getLocation());
            } else {
                secondPlayerLocations.add(message.getLocation());
            }
        }

        // After winning or losing game normall called logout message.
        private void callLogOut() {
            GameMessage gameMessage = new GameMessage(GameMessage.LOGOUT,
                    Username + " logout ");

            sendMessage(gameMessage);
        }

    }

}
