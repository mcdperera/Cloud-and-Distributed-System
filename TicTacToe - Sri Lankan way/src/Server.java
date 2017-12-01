
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * The server that can be run on console application 
 */
public class Server {

    // a unique ID for each connection
    private static int uniqueId;

    // the winning possibility
    List<String> winningPossibility1 = new ArrayList<>(); 
    List<String> winningPossibility2 = new ArrayList<>();
    List<String> winningPossibility3 = new ArrayList<>();
    List<String> winningPossibility4 = new ArrayList<>();
    List<String> winningPossibility5 = new ArrayList<>();
    List<String> winningPossibility6 = new ArrayList<>();
    List<String> winningPossibility7 = new ArrayList<>();
    List<String> winningPossibility8 = new ArrayList<>();

    // an ArrayList to keep the list of the Client
    private final ArrayList<ClientThread> Clients;

    // to display time
    private final SimpleDateFormat simpleDateFormat;

    // the port number to listen for connection
    private final int port;

    // the boolean that will be turned of to stop the server
    private boolean keepGoing;

    // first player saved locations
    public List<String> firstPlayerLocations = new ArrayList<String>(3);

    // second player saved locations
    public List<String> secondPlayerLocations = new ArrayList<String>(3);

    // username of the 1st user
    private String FirstUsername;

    // username of the 2nd user
    private String SecondUsername;

    public Server(int port) {
        this.secondPlayerLocations = new ArrayList<>();
        this.port = port;

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Clients = new ArrayList<>();

        // setup the winning types.
        setupWinningPosibilities();
    }

    public void start() {

        keepGoing = true;

        display("Server started on port " + port + ".");

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            // infinite loop to wait for connections
            while (keepGoing) {

                Socket socket = serverSocket.accept();  	// accept connection

                if (!keepGoing) {
                    break;
                }

                ClientThread t = new ClientThread(socket);
                Clients.add(t);
                t.start();

                GameMessage chatMessage = null;

                // check wether first user logged to the system.
                if (Clients.size() == 1) {

                    display("Server waiting for another clients on port " + port + ".");

                    chatMessage = new GameMessage(GameMessage.SERVER_RESPONSE,
                            "Wait until another user logged to the system");

                    broadcast(chatMessage);
                } else if (Clients.size() == 2) {

                    chatMessage = new GameMessage(GameMessage.SERVER_RESPONSE,
                            "Two users are connected.");

                    broadcast(chatMessage);

                    FirstUsername = Clients.get(0).username;
                    SecondUsername = Clients.get(1).username;

                    chatMessage = new GameMessage(GameMessage.START_GAME,
                            FirstUsername + " starts the game", FirstUsername);

                    broadcast(chatMessage);

                }
            }

            try {
                serverSocket.close();
                for (int i = 0; i < Clients.size(); ++i) {
                    ClientThread tc = Clients.get(i);
                    try {
                        tc.inputStream.close();
                        tc.outputStream.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        display("IOException: " + ioE);
                    }
                }
            } catch (Exception e) {
                display("Exception closing the server and clients: " + e);
            }

        } // something went bad
        catch (IOException e) {
            String msg = simpleDateFormat.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }

    /*
     * For the GUI to stop the server
     */
    protected void stop() {
        keepGoing = false;
        // connect to myself as Client to exit statement 
        // Socket socket = serverSocket.accept();
        try {
            new Socket("localhost", port);
        } catch (Exception e) {
            // nothing I can really do
        }
    }

    private void display(String msg) {
        String time = simpleDateFormat.format(new Date()) + " " + msg;
        System.out.println(time);

    }

    private synchronized void broadcast(GameMessage message) {

        for (int i = Clients.size(); --i >= 0;) {
            ClientThread ct = Clients.get(i);

            if (!ct.writeObject(message)) {
                Clients.remove(i);
                display("Disconnected Client " + ct.username + " removed from list.");
            }
        }
    }

    // for a client who logoff using the LOGOUT message
    synchronized void remove(int id) {
        // scan the array list until we found the Id
        for (int i = 0; i < Clients.size(); ++i) {
            ClientThread ct = Clients.get(i);
            // found it
            if (ct.id == id) {
                Clients.remove(i);
                return;
            }
        }
    }

    // setting up the winning possibility senarios
    private void setupWinningPosibilities() {
        winningPossibility1.add("1,1");
        winningPossibility1.add("1,2");
        winningPossibility1.add("1,3");

        winningPossibility2.add("1,1");
        winningPossibility2.add("2,1");
        winningPossibility2.add("3,1");

        winningPossibility3.add("1,1");
        winningPossibility3.add("2,2");
        winningPossibility3.add("3,3");

        winningPossibility4.add("1,2");
        winningPossibility4.add("2,2");
        winningPossibility4.add("3,2");

        winningPossibility5.add("1,3");
        winningPossibility5.add("2,3");
        winningPossibility5.add("3,3");

        winningPossibility6.add("1,3");
        winningPossibility6.add("2,2");
        winningPossibility6.add("3,1");

        winningPossibility7.add("2,1");
        winningPossibility7.add("2,2");
        winningPossibility7.add("2,3");

        winningPossibility8.add("3,1");
        winningPossibility8.add("3,2");
        winningPossibility8.add("3,3");
    }

    public static void main(String[] args) {

        // start server on port 1500 unless a PortNumber is specified 
        int portNumber = 1500;

        // create a server object and start it
        Server server = new Server(portNumber);
        server.start();

    }

    /**
     * One instance of this thread will run for each client
     */
    class ClientThread extends Thread {

        // the socket where to listen/talk
        Socket socket;

        // input stream
        ObjectInputStream inputStream;

        //output stream
        ObjectOutputStream outputStream;

        // the unique id
        int id;

        // the Username of the Client
        String username;

        // the only type of message a will receive
        GameMessage gameMessage;

        // Constructore
        ClientThread(Socket socket) {
            // a unique id
            id = ++uniqueId;

            this.socket = socket;

            try {

                // create output first
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());

                // read the username
                username = (String) inputStream.readObject();

                display(username + " just connected.");

            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
            } catch (ClassNotFoundException e) {
                display("Exception cclass not found " + e);
            }

        }

        // what will run forever
        public void run() {

            boolean keepGoing = true;

            while (keepGoing) {

                try {
                    gameMessage = (GameMessage) inputStream.readObject();
                } catch (IOException e) {
                    display(username + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }

                // the messaage part of the ChatMessage
                display(gameMessage.getMessage());

                GameMessage chatMessage = null;

                // Switch on the type of message receive
                switch (gameMessage.getType()) {

                    case GameMessage.MESSAGE:
                        break;
                    case GameMessage.PLAY_GAME:

                        boolean needToRemove = false;

                        if (gameMessage.getRemoveLocation() != null && !gameMessage.getRemoveLocation().isEmpty()) {
                            needToRemove = true;
                        }

                        String nextPlayingUsername = "";
                        boolean isFirstUser = false;

                        if (FirstUsername.equalsIgnoreCase(gameMessage.getUsername().toLowerCase())) {

                            if (needToRemove) {
                                firstPlayerLocations.remove(gameMessage.getRemoveLocation());
                            }

                            firstPlayerLocations.add(gameMessage.getLocation());

                            nextPlayingUsername = SecondUsername;

                            isFirstUser = true;
                        } else if (SecondUsername.equalsIgnoreCase(gameMessage.getUsername().toLowerCase())) {

                            if (needToRemove) {
                                secondPlayerLocations.remove(gameMessage.getRemoveLocation());
                            }

                            secondPlayerLocations.add(gameMessage.getLocation());

                            nextPlayingUsername = FirstUsername;
                        }

                        int playerWon = 0;

                        if (firstPlayerLocations.size() == 3 || secondPlayerLocations.size() == 3) {
                            playerWon = whichPlayerWon(firstPlayerLocations) ? 1 : (whichPlayerWon(secondPlayerLocations) ? 2 : 0);
                        }

                        if (playerWon == 0) {

                            chatMessage = new GameMessage(GameMessage.PLAY_GAME,
                                    "", nextPlayingUsername, isFirstUser, gameMessage.getLocation(), gameMessage.getRemoveLocation());

                            broadcast(chatMessage);

                        } else {

                            String wonMessage = "Game won by user " + (playerWon == 1 ? FirstUsername : SecondUsername);

                            display(wonMessage);

                            chatMessage = new GameMessage(GameMessage.GAME_WON,
                                    wonMessage,
                                    nextPlayingUsername, isFirstUser, gameMessage.getLocation(), gameMessage.getRemoveLocation());

                            broadcast(chatMessage);
                        }

                        break;
                    case GameMessage.LOGOUT:
                        keepGoing = false;
                        break;
                }
            }

            remove(id);

            close();

        }

        // try to close everything
        private void close() {
            // try to close the connection
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
            };
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
            }
        }

        // writing the object to the output stream
        private boolean writeObject(GameMessage message) {

            if (!socket.isConnected()) {
                close();
                return false;
            }

            try {
                outputStream.writeObject(message);
            } catch (IOException e) {
                display("Error sending message to " + username);
                display(e.toString());
            }
            return true;
        }

    }

    private boolean whichPlayerWon(List<String> playerLocations) {

        boolean player1Won = false;

        for (int i = 0; i < winningPossibility1.size(); i++) {
            if (!playerLocations.contains(winningPossibility1.get(i))) {
                player1Won = false;
                break;
            } else {
                player1Won = true;
            }
        }

        if (!player1Won) {
            for (int i = 0; i < winningPossibility2.size(); i++) {
                if (!playerLocations.contains(winningPossibility2.get(i))) {
                    player1Won = false;
                    break;
                } else {
                    player1Won = true;
                }
            }
        }

        if (!player1Won) {

            for (int i = 0; i < winningPossibility3.size(); i++) {
                if (!playerLocations.contains(winningPossibility3.get(i))) {
                    player1Won = false;
                    break;
                } else {
                    player1Won = true;
                }
            }
        }

        if (!player1Won) {

            for (int i = 0; i < winningPossibility4.size(); i++) {
                if (!playerLocations.contains(winningPossibility4.get(i))) {
                    player1Won = false;
                    break;
                } else {
                    player1Won = true;
                }
            }
        }

        if (!player1Won) {

            for (int i = 0; i < winningPossibility5.size(); i++) {
                if (!playerLocations.contains(winningPossibility5.get(i))) {
                    player1Won = false;
                    break;
                } else {
                    player1Won = true;
                }
            }
        }
        if (!player1Won) {
            for (int i = 0; i < winningPossibility6.size(); i++) {
                if (!playerLocations.contains(winningPossibility6.get(i))) {
                    player1Won = false;
                    break;
                } else {
                    player1Won = true;
                }
            }
        }
        if (!player1Won) {
            for (int i = 0; i < winningPossibility7.size(); i++) {
                if (!playerLocations.contains(winningPossibility7.get(i))) {
                    player1Won = false;
                    break;
                } else {
                    player1Won = true;
                }
            }
        }
        if (!player1Won) {
            for (int i = 0; i < winningPossibility8.size(); i++) {
                if (!playerLocations.contains(winningPossibility8.get(i))) {
                    player1Won = false;
                    break;
                } else {
                    player1Won = true;
                }
            }
        }

        return player1Won;

    }

}
