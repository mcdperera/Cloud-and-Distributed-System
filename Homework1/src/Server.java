
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

    private static int sendBiddingRequestCount = 0;

    private static int biddingClientIndex = 0;

    private static int playClientIndex = 0;

    private static int trickPlayCount = 0;

    private static int roundTrickPlayCount = 0;

    private final ArrayList<ClientThread> Clients;

    private final ArrayList<MatchStat> MatchStatList;

    private final SimpleDateFormat simpleDateFormat;

    private final int port;

    // When stoping the server needs to change.
    private boolean keepGoing;

    private String SelectedTrickCardSuit;

    private static final String redTeamName = "Red";

    private static final String blueTeamName = "Blue";

    private static final int winScore = 250;

    private static final int maxRoundTrickCount = 13;

    private MatchStat CurrentMatchStat;

    /**
     * Desired port to connect with the client.
     *
     * @param port
     */
    public Server(int port) {

        this.port = port;

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Clients = new ArrayList<>();

        MatchStatList = new ArrayList<>();
    }

    /**
     * Server going to start.
     *
     * @throws java.lang.InterruptedException
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

                    biddingClientIndex = 0;

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

        PackOfCard packOfCards = new PackOfCard();

        for (int i = Clients.size(); --i >= 0;) {
            ClientThread ct = Clients.get(i);

            String msg = "Server sends set of cards to'" + ct.username;

            Message cardMessage = new Message(
                    MessageType.DEAL_CARDS_TO_CLIENT.getValue(), false, msg,
                    false, ErrorMessageType.NONE.getValue());

            ArrayList<String> dealCards = packOfCards.getSetofCards(13);

            cardMessage.setCardMessage(new CardMessage(ct.playername,
                    dealCards));

            ct.setsOfCards = dealCards;

            ct.writeMsg(cardMessage);
        }

    }

    private void sendBiddingRequestMessage() {

        ClientThread ct = Clients.get(biddingClientIndex);

        String msg = "'" + ct.username + "' added his bid";

        Message dealMessage = new Message(
                MessageType.BIDDING_SERVERREQUEST.getValue(), false, msg,
                false, ErrorMessageType.NONE.getValue());

        dealMessage.setBiddingMessage(new BiddingMessage(ct.playername, 0));

        ct.writeMsg(dealMessage);

        if (biddingClientIndex == 3) {
            biddingClientIndex = 0;
        } else {
            biddingClientIndex++;
        }

        sendBiddingRequestCount++;
    }

    private void playTrick() {

        trickPlayCount++;

        ClientThread ct = Clients.get(playClientIndex);

        String msg = "'" + ct.username + "' needs to draw his/her card. ";

        Message PlayGameMessage = new Message(
                MessageType.PLAYGAME_SERVERREQUEST.getValue(), false, msg,
                false, ErrorMessageType.NONE.getValue());

        PlayGameMessage.setPlayGameMessage(
                new PlayGameMessage(ct.playername));

        ct.writeMsg(PlayGameMessage);

        if (playClientIndex == 3) {
            playClientIndex = 0;
        } else {
            playClientIndex++;
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

                        if (sendBiddingRequestCount == 4) {

                            sendBiddingRequestCount = 0;
                            biddingClientIndex = -1;

                            // Start the trick after the bidding approved.
                            trickPlayCount = 0;

                            sendTrickStatMessage();

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

                        if (CurrentMatchStat == null) {

                            ClientThread client = getClient(cardSelectedPlayername);

                            CurrentMatchStat = new MatchStat(client.id,
                                    MatchStatList.size() + 1, 0, 0);
                        }

                        if (trickPlayCount == 1) {
                            SelectedTrickCardSuit = suit;
                        }

                        if (!SelectedTrickCardSuit.equalsIgnoreCase(suit)
                                && checkAnyIlleaglePlay(cardSelectedPlayername, SelectedTrickCardSuit)) {

                            String usernameNotUniqueMessage = "User '" + username + "' play .";

                            display(usernameNotUniqueMessage);

                            returnMessage = new Message(
                                    MessageType.PLAYGAME_SERVERRESPONSE.getValue(),
                                    false, usernameNotUniqueMessage, true, ErrorMessageType.PLAY_CHEATCARD.getValue());

                            returnMessage.setPlayGameMessage(new PlayGameMessage(this.playername,
                                    card));

                            writeMsg(returnMessage);

                        } else {

                            display(this.message.getMessage());

                            ClientThread ct = getClient(cardSelectedPlayername);

                            ct.selectedCard = card;

                            ct.setsOfCards.remove(card);

                            display(cardSelectedPlayername + " selected " + card);

                            String returnMsg = cardSelectedPlayername + " selected " + card;

                            returnMessage = new Message(
                                    MessageType.PLAYGAME_SERVERRESPONSE.getValue(),
                                    false, returnMsg, false, ErrorMessageType.NONE.getValue());

                            returnMessage.setPlayGameMessage(new PlayGameMessage(this.playername,
                                    card));

                            boradastToOtherClients(this, returnMessage);

                            if (trickPlayCount == 4) {

                                trickPlayCount = 0;
                                roundTrickPlayCount++;

                                // Display Player who won the trick.
                                String wonPlayer = checkWonTrickPlayer();

                                SelectedTrickCardSuit = "";

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

                                sendTrickStatMessage();

                                if (roundTrickPlayCount == maxRoundTrickCount) {

                                    setRoundStatMessage();

                                    MatchWonMessage matchWonMessage = isMatchWon();

                                    if (matchWonMessage != null) {

                                        returnMsg = matchWonMessage.getWonTeam() + " wons the match.";

                                        returnMessage = new Message(
                                                MessageType.PLAYGAME_SERVERRESPONSE_TEAM_WON_MATCH.getValue(),
                                                false, returnMsg, false, ErrorMessageType.NONE.getValue());

                                        returnMessage.setMatchStatMessage(getMatchStat());

                                        returnMessage.setMatchWonMessage(matchWonMessage);

                                        boradastToAll(returnMessage);
                                    } else {

                                        try {
                                            TimeUnit.SECONDS.sleep(2);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                        CurrentMatchStat = null;

                                        returnMsg = "Either red team or blue team won't won the Match. So next round needs to start.";

                                        returnMessage = new Message(
                                                MessageType.PLAYGAME_SERVERRESPONSE_TEAM_WON_GAME_WITH_DEAL_CARDS.getValue(),
                                                false, returnMsg, false, ErrorMessageType.NONE.getValue());

                                        returnMessage.setMatchStatMessage(getMatchStat());

                                        boradastToAll(returnMessage);

                                        playClientIndex = nextRoundPlayerIndex();
                                        biddingClientIndex = playClientIndex;

                                        roundTrickPlayCount = 0;

                                        for (ClientThread clientThread : Clients) {
                                            clientThread.biddingAmount = 0;
                                            clientThread.selectedCard = "";
                                            clientThread.wonTrickCouont = 0;
                                        }

                                        dealCards();

                                        sendBiddingRequestMessage();
                                    }

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

        private String checkWonTrickPlayer() {

            String trickWonPlayer = "";

            ArrayList<String> selectedCards = new ArrayList<>();

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                String suit = ct.selectedCard.substring(0, 1);

                if (SelectedTrickCardSuit.equalsIgnoreCase(suit)) {
                    selectedCards.add(ct.selectedCard);
                }
            }

            String highestCard = getHighestPlayedCard(selectedCards);

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                if (ct.selectedCard.equalsIgnoreCase(highestCard)) {
                    trickWonPlayer = ct.username;
                    ct.wonTrickCouont++;
                    playClientIndex = ct.id;
                }
            }

            return trickWonPlayer;
        }

        private String getHighestPlayedCard(ArrayList<String> selectedCards) {

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

        private boolean checkAnyIlleaglePlay(String playerName, String suit) {

            ClientThread client = getClient(playerName);

            for (String card : client.setsOfCards) {

                if (card.substring(0, 1).equalsIgnoreCase(suit)) {
                    return true;
                }
            }

            return false;
        }

        private void sendTrickStatMessage() {

            String returnMsg = team + " team won the trick.";

            //String team;
            Message returnMessage = new Message(
                    MessageType.PLAYGAME_SERVERRESPONSE_TEAM_SCORE.getValue(),
                    false, returnMsg, false, ErrorMessageType.NONE.getValue());

            List<String> usernameBids = new ArrayList<>();

//            for (int i = Clients.size(); --i >= 0;) {
//                ClientThread ct = Clients.get(i);
//
//                usernameBids.add(ct.username + "(" + ct.team + ")" + "\t" + ct.getBid(ct) + "\t" + ct.getwonTrick(ct));
//            }
            for (int i = 0; i < Clients.size(); i++) {
                ClientThread ct = Clients.get(i);

                usernameBids.add(ct.username + "(" + ct.team + ")"
                        + "\t" + ct.getBid(ct) + "\t" + ct.getwonTrick(ct));
            }

            GameStatMessage gameStatMessage = new GameStatMessage("p1", "", usernameBids);

            returnMessage.setGameStatMessage(gameStatMessage);

            returnMessage.setMatchStatMessage(getMatchStat());

            boradastToAll(returnMessage);

        }

        private void setRoundStatMessage() {

            int redTeamScore = 0;
            int blueTeamScore = 0;

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                int bidTricks = ct.getBid(ct);
                int wonTricks = ct.getwonTrick(ct);

                int marks = wonTricks >= bidTricks ? bidTricks * 10
                        + wonTricks - bidTricks : bidTricks * -10;

                if (ct.team.equalsIgnoreCase(redTeamName)) {
                    redTeamScore += marks;
                } else {
                    blueTeamScore += marks;
                }
            }

            CurrentMatchStat.setRedTeamScore(redTeamScore);

            CurrentMatchStat.setBlueTeamScore(blueTeamScore);

            MatchStatList.add(CurrentMatchStat);
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

        private MatchWonMessage isMatchWon() {

            int redTeamMarks = 0, blueTeamMarks = 0;

            for (MatchStat matchStat : MatchStatList) {
                blueTeamMarks += matchStat.getBlueTeamScore();
                redTeamMarks += matchStat.getRedTeamScore();
            }

            String wonTeamName = "";

            if (redTeamMarks > winScore && redTeamMarks > blueTeamMarks) {

                wonTeamName = redTeamName;

            } else if (blueTeamMarks > winScore && blueTeamMarks > redTeamMarks) {

                wonTeamName = blueTeamName;
            } else if (blueTeamMarks > winScore && redTeamMarks > winScore) {
                {
                    if (redTeamMarks > blueTeamMarks) {
                        wonTeamName = redTeamName;
                    } else {
                        wonTeamName = blueTeamName;
                    }
                }
            }

            if (wonTeamName.isEmpty()) {
                return null;
            } else {
                return new MatchWonMessage("", wonTeamName);
            }
        }

        private ClientThread getClient(String playername) {
            ClientThread client = null;

            for (int i = Clients.size(); --i >= 0;) {
                ClientThread ct = Clients.get(i);

                if (ct.playername.equalsIgnoreCase(playername)) {

                    client = ct;

                    break;
                }
            }

            return client;
        }

        private String getMatchStat() {

            String returnString = "Round " + "\t" + "Blue" + "\t" + "Red" + "\n";

            int redTeamMarks = 0, blueTeamMarks = 0;

            for (MatchStat matchStat : MatchStatList) {

                redTeamMarks += matchStat.getRedTeamScore();
                blueTeamMarks += matchStat.getBlueTeamScore();

                returnString += matchStat.getRound() + "\t" + matchStat.getBlueTeamScore() + "\t" + matchStat.getRedTeamScore() + "\n";

            }
            returnString += "__________________________________" + "\n";

            returnString += MatchStatList.size() + "\t" + blueTeamMarks + "\t" + redTeamMarks + "\n";

            return returnString;
        }

        private int nextRoundPlayerIndex() {

            int index = 0;

            for (MatchStat matchStat : MatchStatList) {
                index = matchStat.getPlayerIndex();
            }

            if (index == 3) {
                index = 0;
            } else {
                index++;
            }

            return index;
        }

    }
}
