
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {
    // a unique ID for each connection

    private static int uniqueId = 0;

    private static Parameters parameters;

    static void Search(SearchMessage searchMessage) throws IOException {

        RoutingTable neighbourRoutingTable = null;

        ArrayList<RoutingTable> routingTable
                = FileOperation.getRoutingTable(Server.parameters.getreadablePeerId());

        if (routingTable == null || routingTable.size() == 0) {
            return;
        }

        if (Server.parameters.isRandomRoutingType()) {

            int smallest = 0;

            int i = 0;
            for (RoutingTable routeValue : routingTable) {

                Util.print(routeValue.getServerAddress());

                int diffrence = routeValue.getPeerId() > searchMessage.getsearchPeerId()
                        ? routeValue.getPeerId() - searchMessage.getsearchPeerId()
                        : searchMessage.getsearchPeerId() - routeValue.getPeerId();

                if (i == 0) {
                    neighbourRoutingTable = routeValue;
                    smallest = diffrence;
                }
                i++;

                if (diffrence < smallest && smallest < searchMessage.getMinimumDifference() ) {

                    neighbourRoutingTable = routeValue;
                    smallest = diffrence;

                } 
                
//                else if (smallest == diffrence) {
//
//                    neighbourRoutingTable = null;
//                    FileOperation.writeToFile(parameters, searchMessage.toString());
//                }
            }

            if (neighbourRoutingTable != null) {

                Parameters newParameters = parameters;

                newParameters.setServerAddress(neighbourRoutingTable.getServerAddress());
                newParameters.setIsSearchPeer(true);

                searchMessage.setMinimumDifference(smallest);
                searchMessage.incrementHop();

                newParameters.setSearchMessage(searchMessage);

                Util.print("Search for : " + searchMessage.getsearchPeerId());
                Util.print("send search message to  : " + neighbourRoutingTable.getPeerId());

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Client.run(newParameters);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }.start();

            }

        } else {

        }

    }

    // an ArrayList to keep the list of the Client
    private static ArrayList<ClientThread> clientList;

    private final SimpleDateFormat sdf;

    private final int port;

    private boolean keepGoing;

    public Server(int port) {
        // the port
        this.port = port;

        // to display hh:mm:ss
        sdf = new SimpleDateFormat("HH:mm:ss");

        // ArrayList for the Client list
        clientList = new ArrayList<>();
    }

    public void start() {
        keepGoing = true;
        /* create socket server and wait for connection requests */
        try {
            // the socket used by the server
            ServerSocket serverSocket = new ServerSocket(port);

            display("My Peer id:" + Server.parameters.getreadablePeerId() + " & Random peer id " + Server.parameters.getpeerId());

            // infinite loop to wait for connections
            while (keepGoing) {
                // format message saying we are waiting
                display("Server waiting for Clients on port " + port + ".");

                Socket socket = serverSocket.accept();  	// accept connection
                // if I was asked to stop
                if (!keepGoing) {
                    break;
                }

                ClientThread t = new ClientThread(socket);  // make a thread of it

                clientList.add(t);									// save it in the ArrayList

                t.start();
            }
            // I was asked to stop
            try {
                serverSocket.close();
                for (int i = 0; i < clientList.size(); ++i) {
                    ClientThread tc = clientList.get(i);
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        // not much I can do
                    }
                }
            } catch (Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        } // something went bad // something went bad // something went bad // something went bad
        catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
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

    /*
	 * Display an event (not a message) to the console or the GUI
     */
    private void display(String msg) {
        String time = sdf.format(new Date()) + " " + msg;

        System.out.println(time);

    }

    /*
	 *  to broadcast a message to all Clients
     */
    private synchronized void broadcast(String message) {
        // add HH:mm:ss and \n to the message
        String time = sdf.format(new Date());
        String messageLf = time + " " + message + "\n";
        // display message on console or GUI

        System.out.print(messageLf);

        // we loop in reverse order in case we would have to remove a Client
        // because it has disconnected
        for (int i = clientList.size(); --i >= 0;) {
            ClientThread ct = clientList.get(i);
            // try to write to the Client if it fails remove it from the list
//            if (!ct.writeMsg(messageLf)) {
//                clientList.remove(i);
//                display("Disconnected Client " + ct.id + " removed from list.");
//            }
        }
    }

    // for a client who logoff using the LOGOUT message
    synchronized void remove(int id) {
        // scan the array list until we found the Id
        for (int i = 0; i < clientList.size(); ++i) {
            ClientThread ct = clientList.get(i);
            // found it
            if (ct.id == id) {
                clientList.remove(i);
                return;
            }
        }
    }

    public static void run(Parameters parameters) { //int portNumber, int peerID, String readPeerId) throws FileNotFoundException {

        Server.parameters = parameters;

        // create a server object and start it
        Server server = new Server(parameters.getportNumber());
        server.start();
    }

    /**
     * One instance of this thread will run for each client
     */
    class ClientThread extends Thread {
        // the socket where to listen/talk

        Socket socket;

        ObjectInputStream sInput;

        ObjectOutputStream sOutput;

        // my unique id (easier for deconnection)
        int id;

        // the only type of message a will receive
        Message cm;

        // the date I connect
        String date;

        int senderPeerId;

        // Constructore
        ClientThread(Socket socket) {
            // a unique id
            id = ++uniqueId;
            this.socket = socket;
            /* Creating both Data Stream */

            try {
                // create output first
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                // read the username

                display("Client " + id + " just connected.");
            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
                return;
            }
            // but I read a String, I am sure it will work
            // have to catch ClassNotFoundException
            // but I read a String, I am sure it will work
            date = new Date().toString() + "\n";
        }

        // what will run forever
        public void run() {
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a String (which is an object)
                try {
                    cm = (Message) sInput.readObject();
                } catch (IOException e) {
                    display(this.id + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }

                // Switch on the type of message receive
                switch (cm.getType()) {

                    case EXIT:
                        display("Clilent " + id + " disconnected with a exit message.");
                        keepGoing = false;
                        break;

                    case PING:
                        PingMessage fromPingMessage = cm.getPingMessage();

                        this.senderPeerId = fromPingMessage.getSenderPeerId();

                        display("Clilent " + id + " sends ping message :" + fromPingMessage.getPingId());

                        try {
                            addToRoutingTable(fromPingMessage);
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        fromPingMessage.setReceiverPeerId(Server.parameters.getpeerId());
                        fromPingMessage.setReceiverIpAddress();

                        Message message = new Message(MessageType.PINGRESPONSE);

                        message.setPingMessage(fromPingMessage);

                        writeMsg(message);
                        break;

                    case SEARCH:
                        SearchMessage searchMessage = cm.getSearchMessage();

                        display("Clilent " + id + " receive search message from :" + searchMessage.getPingId());

                        ArrayList<RoutingTable> routingTable;
                        try {
                            
                            Search(searchMessage);
//                            routingTable
//                                    = FileOperation.getRoutingTable(Server.parameters.getreadablePeerId());
//
//                            if (routingTable == null || routingTable.size() == 0) {
//                                return;
//                            }
//                            int randomPeerId = searchMessage.getsearchPeerId();
//
//                            int diffrencePeerId = 0;
//
//                            for (RoutingTable routeValue : routingTable) {
//
//                                Util.print(routeValue.getServerAddress());
//
//                                int diffrence = routeValue.getPeerId() > randomPeerId
//                                        ? routeValue.getPeerId() - randomPeerId
//                                        : randomPeerId - routeValue.getPeerId();
//
//                                if (diffrencePeerId < diffrence) {
//
//                                    diffrencePeerId = diffrence;
//                                } else if (diffrencePeerId == diffrence) {
//
//                                }
//                            }

                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        break;
                }
            }

            // remove myself from the arrayList containing the list of the
            // connected Clients
            remove(id);
            close();
        }

        // try to close everything
        private void close() {
            // try to close the connection
            try {
                if (sOutput != null) {
                    sOutput.close();
                }
            } catch (Exception e) {
            }
            try {
                if (sInput != null) {
                    sInput.close();
                }
            } catch (Exception e) {
            };
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
            }

            System.exit(0);
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
                sOutput.writeObject(msg);
            } // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending message to " + this.id);
                display(e.toString());
            }
            return true;
        }

        private void addToRoutingTable(PingMessage pingMessage) throws IOException {

            ArrayList<RoutingTable> routingTable = FileOperation.getRoutingTable(parameters.getreadablePeerId());

            if (routingTable.isEmpty()) {
                FileOperation.setRoutingTable(new RoutingTable(pingMessage.getSenderPeerId(),
                        pingMessage.getSernderIpAddress()));
            }
        }

    }

}
