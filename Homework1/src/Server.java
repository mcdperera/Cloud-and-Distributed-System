
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static int uniqueId = -1;

    private static int clientIndex = -1;

    private static int firstPlayerIndex = 0;

    private static int trickPlayCount = 0;

    private static int gameTrickPlayCount = 0;

    private final ArrayList<ClientThread> Clients;

    private final SimpleDateFormat simpleDateFormat;

    private final int port;

    private PackOfCard packOfCard = new PackOfCard();

    // When stoping the server needs to change.
    private boolean keepGoing;

    private String SelectedTrickCardSuit;

    private static String redTeamName = "Red";

    private static String blueTeamName = "Blue";

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

                    TimeUnit.SECONDS.sleep(2);

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

    private synchronized void boradastToAll(Message message) {

        for (int i = Clients.size(); --i >= 0;) {
            ClientThread ct = Clients.get(i);
            ct.writeMsg(message);
        }
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

    private void playTrick() {

        trickPlayCount++;

        ClientThread ct = Clients.get(firstPlayerIndex);

        String msg = "'" + ct.username + "' needs to draw his/her card. ";

        Message PlayGameMessage = new Message(
                MessageType.PLAYGAME_SERVERREQUEST.getValue(), false, msg,
                false, ErrorMessageType.NONE.getValue());

        PlayGameMessage.setPlayGameMessage(
                new PlayGameMessage(ct.playername));

        ct.writeMsg(PlayGameMessage);

        if (firstPlayerIndex == 3) {
            firstPlayerIndex = 0;
        } else {
            firstPlayerIndex++;
        }
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

        String team;

        // the date I connect
        String date;

        //
        String playername;

        Integer biddingAmount;

        String selectedCard;

        Integer wonTrickCouont = 0;

        private ArrayList<String> setsOfCards;

        // Constructore
        ClientThread(Socket socket) {

            // a unique id
            id = ++uniqueId;

            this.socket = socket;

            this.playername = playerNames.get(id);

            try {

                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());

                // read the username
                Message message = (Message) inputStream.readObject();

                String userName = message.getConnectionMessage().getUsername();

                // check wether the username is unique among the players
                if (isUsernameUnique(userName)) {

                    this.team = id % 2 == 0 ? blueTeamName : redTeamName;

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

                        BiddingMessage biddingMessage = this.message.getBiddingMessage();

                        for (int i = 0; i < Clients.size(); i++) {
                            ClientThread ct = Clients.get(i);

                            display("uniqueid " + ct.id + " System player name "
                                    + ct.playername + " real username" + ct.username);

                            if (ct.playername.equalsIgnoreCase(biddingMessage.getPlayerName())) {
                                ct.biddingAmount = biddingMessage.getAmount();
                            }
                        }

                        if (clientIndex == 3) {

                            clientIndex = -1;

                            // Start the trick after the bidding approved.
                            trickPlayCount = 0;

                            sendTrickStatMessage(null);

                            playTrick();

                            break;
                        } else {
                            sendBiddingRequestMessage();
                        }
                        break;

                    case PLAYGAME_CLIENTRESPONSE:

                        PlayGameMessage playMessage = this.message.getPlayGameMessage();

                        String cardSelectedPlayername = playMessage.getPlayerName();
                        String card = playMessage.getCard();
                        String suit = card.substring(0, 1);

                        if (trickPlayCount == 1) {

                            SelectedTrickCardSuit = suit;
                        }

                        if (!SelectedTrickCardSuit.equalsIgnoreCase(suit)
                                && checkAnyIlleaglePlay(cardSelectedPlayername, suit)) {

                        } else {

                            display(this.message.getMessage());

                            for (int i = Clients.size(); --i >= 0;) {
                                ClientThread ct = Clients.get(i);

                                if (ct.playername.equalsIgnoreCase(cardSelectedPlayername)) {

                                    ct.selectedCard = card;

                                    ct.setsOfCards.remove(card);

                                    display(cardSelectedPlayername + " selected " + card);
                                }
                            }

                            String returnMsg = cardSelectedPlayername + " selected " + card;

                            returnMessage = new Message(
                                    MessageType.PLAYGAME_SERVERRESPONSE.getValue(),
                                    false, returnMsg, false, ErrorMessageType.NONE.getValue());

                            returnMessage.setPlayGameMessage(new PlayGameMessage(this.playername,
                                    card));

                            boradastToOtherClients(this, returnMessage);

                            if (trickPlayCount == 4) {

                                trickPlayCount = 0;
                                gameTrickPlayCount++;

                                // Display Player who won the trick.
                                String wonPlayer = checkWonGamePlayer();

                                returnMsg = wonPlayer + " player won the trick.";

                                returnMessage = new Message(
                                        MessageType.PLAYGAME_SERVERRESPONSE_PLAYER_WON_TRICK.getValue(),
                                        false, returnMsg, false, ErrorMessageType.NONE.getValue());

                                returnMessage.setPlayGameMessage(new PlayGameMessage(this.playername, wonPlayer,
                                        this.team));

                                boradastToAll(returnMessage);

                                try {
                                    TimeUnit.SECONDS.sleep(2);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                sendTrickStatMessage(null);

                                if (gameTrickPlayCount == 13) {
                                    sendTrickStatMessage(sendMatchStatMessage());

                                } else {
                                    playTrick();
                                }

                                break;
                            } else {
                                playTrick();
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

        private String checkWonGamePlayer() {

            String trickWonPlayer = "";

            String[] selectedCards = new String[4];

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);
                selectedCards[i] = ct.selectedCard;
            }

            String highestCard = getHighestPlayedCard(selectedCards);

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                if (ct.selectedCard.equalsIgnoreCase(highestCard)) {
                    trickWonPlayer = ct.username;
                    ct.wonTrickCouont++;
                    firstPlayerIndex = ct.id;
                }
            }

            return trickWonPlayer;
        }

        private String getHighestPlayedCard(String[] selectedCards) {

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

        private boolean checkAnyIlleaglePlay(String cardSelectedplayerName, String suit) {

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                if (ct.playername.equalsIgnoreCase(cardSelectedplayerName)) {

                    for (String card : ct.setsOfCards) {

                        if (card.substring(0).equalsIgnoreCase(suit)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private void gameWonMesage() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private void sendTrickStatMessage(MatchStatMessage matchStatMessage) {

            String returnMsg = team + " team won the game.";

            //String team;
            Message returnMessage = new Message(
                    MessageType.PLAYGAME_SERVERRESPONSE_TEAM_WON_GAME.getValue(),
                    false, returnMsg, false, ErrorMessageType.NONE.getValue());

            List<String> usernameBids = new ArrayList<>();

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                usernameBids.add(ct.username + " :" + ct.getBid(ct) + "/" + ct.getwonTrick(ct));

            }

            GameStatMessage gameStatMessage = new GameStatMessage("p1", "RedTeam", usernameBids);

            returnMessage.setGameStatMessage(gameStatMessage);

            if (matchStatMessage != null) {
                returnMessage.setMatchStatMessage(matchStatMessage);
            }

            boradastToAll(returnMessage);

        }

        private MatchStatMessage sendMatchStatMessage() {

            String returnMsg = team + " team won the game.";

            int redTeamScore = 0;
            int blueTeamScore = 0;

            //String team;
            Message returnMessage = new Message(
                    MessageType.PLAYGAME_SERVERRESPONSE_TEAM_WON_GAME.getValue(),
                    false, returnMsg, false, ErrorMessageType.NONE.getValue());

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                int bidDiff = ct.getBid(ct) - ct.getwonTrick(ct);

                if (ct.team.equalsIgnoreCase(redTeamName)) {
                    redTeamScore += (bidDiff < 0 ? -50 * bidDiff : bidDiff);
                } else {
                    blueTeamScore += (bidDiff < 0 ? -50 * bidDiff : bidDiff);
                }
            }

            return new MatchStatMessage("p2", redTeamScore, blueTeamScore);

        }

        private Integer getBid(ClientThread ct) {

            Integer bidAmount = 0;

            if (ct.biddingAmount != null) {

                bidAmount = ct.biddingAmount;
            }

            return bidAmount;
        }

        private Integer getwonTrick(ClientThread ct) {
            Integer wonTrickCount = 0;

            if (ct.wonTrickCouont != null) {

                wonTrickCount = ct.wonTrickCouont;
            }

            return wonTrickCount;
        }

    }
}
