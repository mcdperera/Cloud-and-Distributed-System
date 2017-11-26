
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client {

    private static int pingId;

    private static int searchMessageId;

    // for I/O
    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;

    // the server, the port and the username
    private final String server;
    private final int port;

    private static final List<Pair> pingMessageList = new ArrayList<>();

    private static Parameters parameters;

    Client(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        } // if it failed not much I can so
        catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        /* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ListenFromServer().start();
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be Message objects
//        try {
//            sOutput.writeObject(username);
//        } catch (IOException eIO) {
//            display("Exception doing login : " + eIO);
//            disconnect();
//            return false;
//        }
        // success we inform the caller that it worked
        return true;
    }

    private void display(String msg) {
        System.out.println(msg);

    }

    void sendMessage(Message msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            System.exit(0);
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

    public static void run(Parameters parameters) throws UnknownHostException {

        Client.parameters = parameters;

        if (parameters.getServerAddress() == null || parameters.getServerAddress().isEmpty()) {
            return;
        }

        Client client = new Client(parameters.getServerAddress(), parameters.getportNumber());

        if (!client.start()) {
            return;
        }

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                try {
                    client.sendPingMessage();
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0, 30000);
        
        if (parameters.isSearchPeer()) {
            
            client.sendSearchMessage();
            
            parameters.setIsSearchPeer(false);
        }
        
        // wait for messages from user
        Scanner scan = new Scanner(System.in);
        // loop forever for message from the user
        while (true) {
            System.out.print("> ");
            // read message from user
            String msg = scan.nextLine();

            if (msg.equalsIgnoreCase("EXIT")) {

                client.sendMessage(new Message(MessageType.EXIT));

                break;
            }
        }

        // done disconnect
        client.disconnect();

    }
  
    public  void sendSearchMessage() throws UnknownHostException {

        Message message = new Message(MessageType.SEARCH);

        message.setSearchMessage(parameters.getSearchMessage());

        this.sendMessage(message);
    }

    private void sendPingMessage() throws UnknownHostException {

        Message message = new Message(MessageType.PING);

        message.setPingMessage(getPingMessage());

        this.sendMessage(message);
    }

    private PingMessage getPingMessage() throws UnknownHostException {
        pingId++;

        PingMessage pingMessage = new PingMessage(parameters.getreadablePeerId(),
                pingId,
                parameters.getpeerId());

        pingMessageList.add(new Pair(pingId, pingMessage));

        return pingMessage;
    }

    class ListenFromServer extends Thread {

        public void run() {

            while (true) {
                try {
                    //String msg = (String) sInput.readObject();

                    Message message = (Message) sInput.readObject();

                    switch (message.getType()) {
                        case NONE:
                            System.out.print("None message type send form serer");
                            break;
                        case PINGRESPONSE:

                            PingMessage clientReceivePingMessage = message.getPingMessage();

                            addToRoutingTable(clientReceivePingMessage);

                            FileOperation.writeToFile(parameters, clientReceivePingMessage.toString());

                            break;
                    }

                } catch (IOException e) {
                    //display("Server has close the connection: " + e);

                    break;
                } // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) {
                }
            }
        }
    }

    private void addToRoutingTable(PingMessage pingMessage) throws IOException {

        boolean isPeerInclude = false;

        ArrayList<RoutingTable> routingTable = FileOperation.getRoutingTable(parameters.getreadablePeerId());

        for (RoutingTable routingValue : routingTable) {

            if (routingValue.getPeerId() == pingMessage.getReceiverPeerId()) {
                isPeerInclude = true;
            }
        }

        if (!isPeerInclude) {
            FileOperation.setRoutingTable(new RoutingTable(pingMessage.getReceiverPeerId(),
                    pingMessage.getReceiverIpAddress()));
        }
    }
}
