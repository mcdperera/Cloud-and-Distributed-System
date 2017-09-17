
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*
 * The server that can be run both as a console application or a GUI
 */
/**
 *
 * @author Charmal
 */
public class Server {

    List<String> playerNames = Arrays.asList("b1,r1,b2,r2".toLowerCase().split(","));

    static int maximumNumberOfClients = 4;

    private static int uniqueId = 0;

    private static int clientIndex = -1;

    private static int firstPlayerIndex = -1;

    private final ArrayList<ClientThread> Clients;

    private final SimpleDateFormat simpleDateFormat;

    private final int port;

    private PackOfCard packOfCard = new PackOfCard();

    // When stoping the server needs to change.
    private boolean keepGoing;

    private boolean gameStart;

    private String SelectedCardSuit;

    /**
     * Desired port to connect with the client.
     *
     * @param port
     */
    public Server(int port) {

        this.port = port;

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Clients = new ArrayList<>();
    }

    /**
     * Server going to start.
     */
    public void start() throws InterruptedException {
        keepGoing = true;
        /* create socket server and wait for connection requests */
        try {

            // the socket used by the server
            ServerSocket serverSocket = new ServerSocket(port);

            // infinite loop to wait for connections
            while (keepGoing) {

                display("Server waiting for Clients on port " + port + ".");

                Socket socket = serverSocket.accept(); // accept connection

                // If someting went wrong and keepGoing is flase 
                // Server needs to stop.
                if (!keepGoing) {
                    break;
                }

                ClientThread t = new ClientThread(socket);  // make a thread of it

                Clients.add(t);

                t.start();

                if (maximumNumberOfClients == Clients.size()) {
                    dealCards();

                    TimeUnit.SECONDS.sleep(5);

                    sendBiddingRequestMessage();
                }

            }

            // I was asked to stop
            try {
                serverSocket.close();

                for (int i = 0; i < Clients.size(); ++i) {
                    ClientThread tc = Clients.get(i);
                    try {
                        tc.inputStream.close();
                        tc.outputStream.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        display("Exception generated: " + ioE);
                    }
                }

            } catch (Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        } catch (IOException e) {
            String msg = simpleDateFormat.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }

//    protected void stop() {
//        keepGoing = false;
//        try {
//            new Socket("localhost", port);
//        } catch (Exception e) {
//        }
//    }
    private void display(String message) {
        System.out.println(
                simpleDateFormat.format(new Date()) + " : " + message);
    }

    private synchronized void boradastToOtherClients(ClientThread client, Message message) {

        if (Clients.size() > 0) {
            display("Sends other player details to '" + client.username + "' user");
        } else {
            display("No other players at this time.");
        }

        for (int i = Clients.size(); --i >= 0;) {
            ClientThread ct = Clients.get(i);
            ct.writeMsg(message);
        }
    }

    synchronized void remove(int id) {
        for (int i = 0; i < Clients.size(); ++i) {
            ClientThread ct = Clients.get(i);
            if (ct.id == id) {
                Clients.remove(i);
                return;
            }
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(1500);
        server.start();
    }

    /**
     *
     * @param username
     * @return
     */
    public boolean isUsernameUnique(String username) {

        for (int i = 0; i < Clients.size(); ++i) {
            ClientThread ct = Clients.get(i);
            if (ct.username.equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    private synchronized void dealCards() {
        for (int i = Clients.size(); --i >= 0;) {
            ClientThread ct = Clients.get(i);

            String msg = "Server sends set of cards to'" + ct.username;

            Message cardMessage = new Message(
                    MessageType.DEAL_CARDS_TO_CLIENT.getValue(), false, msg,
                    false, ErrorMessageType.NONE.getValue());

            ArrayList<String> dealCards = packOfCard.getSetofCards(13);

            cardMessage.setCardMessage(new CardMessage(ct.playername,
                    dealCards));

            ct.setsOfCards = dealCards;

            ct.writeMsg(cardMessage);
        }

    }

    private void sendBiddingRequestMessage() {
        clientIndex++;

        ClientThread ct = Clients.get(clientIndex);

        String msg = "'" + ct.username + "' added his bid";

        Message dealMessage = new Message(
                MessageType.BIDDING_SERVERREQUEST.getValue(), false, msg,
                false, ErrorMessageType.NONE.getValue());

        dealMessage.setBiddingMessage(new BiddingMessage(ct.playername, 0));

        ct.writeMsg(dealMessage);
    }

    private void StartGame() {

        firstPlayerIndex++;

        ClientThread ct = Clients.get(firstPlayerIndex);

        String msg = "'" + ct.username + "' needs to draw his/her card. ";

        Message PlayGameMessage = new Message(
                MessageType.PLAYGAME_SERVERREQUEST.getValue(), false, msg,
                false, ErrorMessageType.NONE.getValue());

        PlayGameMessage.setPlayGameMessage(
                new PlayGameMessage(ct.playername));

        ct.writeMsg(PlayGameMessage);
    }

    /**
     * One instance of this thread will run for each client
     */
    class ClientThread extends Thread {

        Socket socket;

        ObjectInputStream inputStream;

        ObjectOutputStream outputStream;

        // unique id
        int id;

        // the Username of the Client
        String username;

        // the only type of message a will receive
        Message message;

        // the date I connect
        String date;

        //
        String playername;

        Integer biddingAmount;

        String selectedCard;

        private ArrayList<String> setsOfCards;

        // Constructore
        ClientThread(Socket socket) {

            // a unique id
            id = uniqueId++;

            this.socket = socket;

            this.playername = playerNames.get(id--);

            try {

                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());

                // read the username
                Message message = (Message) inputStream.readObject();

                String userName = message.getConnectionMessage().getUsername();

                // check wether the username is unique among the players
                if (isUsernameUnique(userName)) {

                    String localMessage = "User '" + userName + "' just connected.";

                    display(localMessage);

                    sendMessageaboutPreiousUsers();

                    this.username = userName;

                    Message returnMessage = new Message(
                            MessageType.CONNECTIONESTABLISH_SERVERESPONSE.getValue(),
                            false, localMessage, false, ErrorMessageType.NONE.getValue());

                    returnMessage.setConnectionMessage(new ConnectionMessage(this.playername, this.username));

                    writeMsg(returnMessage);

                    Message returnMessageForOthers = new Message(
                            MessageType.CONNECTIONESTABLISH_SERVERESPONSE_OTHERPLAYERS.getValue(),
                            false, localMessage + "as other user",
                            false, ErrorMessageType.NONE.getValue());

                    returnMessageForOthers.setConnectionMessage(new ConnectionMessage(this.playername, this.username));

                    boradastToOtherClients(this, returnMessageForOthers);

                } else {

                    String usernameNotUniqueMessage = "User '" + userName + "' is not unique username.";

                    display(usernameNotUniqueMessage);

                    Message returnMessage = new Message(
                            MessageType.CONNECTIONESTABLISH_SERVERESPONSE.getValue(),
                            false, usernameNotUniqueMessage, true, ErrorMessageType.USERNAME_EXISTS.getValue());

                    returnMessage.setConnectionMessage(new ConnectionMessage(this.playername, this.username));

                    writeMsg(returnMessage);
                }

            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
                return;
            } catch (ClassNotFoundException e) {
            }

            date = new Date().toString() + "\n";
        }

        // what will run forever
        public void run() {

            boolean keepGoing = true;
            while (keepGoing) {

                try {
                    message = (Message) inputStream.readObject();
                } catch (IOException e) {
                    display(username + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }

                switch (this.message.getType()) {
                    case NONE:
                        break;
                    case ONNECTIONESTABLISH_CLIENT_USERNAMEDUPLICATE_RESPONSE:
                        String localMessage = "User '" + message.getConnectionMessage().getUsername() + "' just connected.";

                        display(localMessage);

                        sendMessageaboutPreiousUsers();

                        this.username = message.getConnectionMessage().getUsername();

                        Message returnMessage = new Message(
                                MessageType.CONNECTIONESTABLISH_SERVERESPONSE.getValue(),
                                false, localMessage, false, ErrorMessageType.NONE.getValue());

                        returnMessage.setConnectionMessage(new ConnectionMessage(this.playername, this.username));

                        writeMsg(returnMessage);

                        Message returnMessageForOthers = new Message(
                                MessageType.CONNECTIONESTABLISH_SERVERESPONSE_OTHERPLAYERS.getValue(),
                                false, localMessage + "as other user",
                                false, ErrorMessageType.NONE.getValue());

                        returnMessageForOthers.setConnectionMessage(new ConnectionMessage(this.playername, this.username));

                        boradastToOtherClients(this, returnMessageForOthers);

                    case BIDDING_CLIENTRESPONSE:

                        if (clientIndex == 3) {

                            clientIndex = -1;

                            gameStart = true;

                            StartGame();

                            break;
                        } else {

                            BiddingMessage biddingMessage = this.message.getBiddingMessage();

                            for (int i = Clients.size(); --i >= 0;) {
                                ClientThread ct = Clients.get(i);
                                display("uniqueid " + ct.id + " System player name "
                                        + ct.playername + " real username" + ct.username);

                                if (ct.playername == biddingMessage.getPlayerName()) {
                                    ct.biddingAmount = biddingMessage.getAmount();
                                }
                            }

                            sendBiddingRequestMessage();
                        }
                        break;

                    case PLAYGAME_CLIENTRESPONSE:

                        if (firstPlayerIndex == 3) {

                            firstPlayerIndex = -1;

                            checkWonGamePlayer();

                            break;
                        } else {

                            PlayGameMessage playMessage = this.message.getPlayGameMessage();

                            String cardSelectedUsername = playMessage.getPlayerName();
                            String card = playMessage.getCard();
                            String suit = card.substring(0);

                            if (gameStart) {
                                gameStart = false;

                                SelectedCardSuit = suit;
                            }

                            if (!SelectedCardSuit.equalsIgnoreCase(suit) && checkAnyIlleaglePlay(cardSelectedUsername, suit)) {

                            } else {

                                display(this.message.getMessage());

                                for (int i = Clients.size(); --i >= 0;) {
                                    ClientThread ct = Clients.get(i);

                                    if (ct.playername == cardSelectedUsername) {
                                        ct.selectedCard = card;

                                        display(cardSelectedUsername + " selected " + card);
                                    }
                                }

                                String returnMsg = cardSelectedUsername + " selected " + card;

                                returnMessage = new Message(
                                        MessageType.PLAYGAME_SERVERRESPONSE.getValue(),
                                        false, returnMsg, false, ErrorMessageType.NONE.getValue());

                                returnMessage.setPlayGameMessage(new PlayGameMessage(this.playername,
                                        card));

                                boradastToOtherClients(this, returnMessage);

                                StartGame();
                            }
                        }
                        break;
                    case DEAL_CARD_TO_SERVER:
                        break;
                    default:
                        throw new AssertionError(this.message.getType().name());
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

        /*
		 * Write a String to the Client output stream
         */
        private boolean writeMsg(Message msg) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream
            try {
                outputStream.writeObject(msg);
            } // if an error occurs, do not abort just inform the user // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending message to " + username);
                display(e.toString());
            }

            return true;
        }

        private void sendMessageaboutPreiousUsers() {

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                String msg = "'" + ct.username + "' user preiously registered";

                Message preiousUserMessage = new Message(
                        MessageType.CONNECTIONESTABLISH_SERVERESPONSE.getValue(),
                        false, msg, false, ErrorMessageType.NONE.getValue());

                preiousUserMessage.setConnectionMessage(new ConnectionMessage(ct.playername, ct.username));

                writeMsg(preiousUserMessage);
            }

        }

        private void checkWonGamePlayer() {

            String[] selectedCards = new String[5];

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                selectedCards[i] = ct.selectedCard;
            }

        }

        private String highestCard(String[] selectedCards) {

            String highestCard = "";
            int highestValue = 0;

            for (String card : selectedCards) {

                Integer cardValue = Integer.parseInt(card.substring(1, card.length()));

                if (highestValue < cardValue) {
                    highestValue = cardValue;
                    highestCard = card;
                }
            }

            return highestCard;

        }

        private boolean checkAnyIlleaglePlay(String cardSelectedUsername, String suit) {
            return false;
        }

    }
}
