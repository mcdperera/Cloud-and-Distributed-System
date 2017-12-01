
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 502759576
 */
public class Central {

    public static int dhtKey = 16;

    public static final int twoPowM = 65536;

    private static int pingMessageId;

    private static int searchMessageId;

    private static Parameters localParameters;

    public static ArrayList<Routing> routingTable;

    private static Routing successor;

    private static Routing predecessor;

    private static void server(Parameters parameters) throws IOException, Exception {
        Server myRunnable = new Server(parameters);
        Thread t = new Thread(myRunnable);
        t.start();
    }

    private static void sendClientPing(Parameters parameters) throws IOException, Exception {

        try {
            PingClient pingClient = new PingClient(parameters, ++pingMessageId);
            Thread t = new Thread(pingClient);
            t.start();

        } catch (Exception ex) {
            Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void sendClientPingWithRouting(Parameters parameters) throws IOException, Exception {

        routingTable.forEach((routing) -> {

            try {

                Parameters newParameter = new Parameters(parameters.getpeerId(), parameters.getreadablePeerId(),
                        routing.getServerAddress(), parameters.getportNumber(), parameters.getRoutingType());

                PingClient pingClient = new PingClient(newParameter, ++pingMessageId);

                Thread t = new Thread(pingClient);

                t.start();

            } catch (Exception ex) {
                Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

    public static void sendClientPingResponse(Parameters parameters, String ip, PingMessage pingMessage) {

        try {
            PingResponseClient pingResponseClient = new PingResponseClient(parameters);

            Thread t = new Thread(pingResponseClient);

            t.start();

        } catch (Exception ex) {
            Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void sendSearch(String serverAddress, int portNumber, SearchMessage searchMessage) {

        try {

            SearchClient searchClient = new SearchClient(serverAddress, portNumber, searchMessage);

            Thread t = new Thread(searchClient);

            t.start();

        } catch (Exception ex) {
            Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void sendSearchResponse(String serverAddress, int portNumber, SearchMessage searchMessage) {
        try {

            SearchResponseClient searchResponseClient
                    = new SearchResponseClient(serverAddress, portNumber, searchMessage);

            Thread t = new Thread(searchResponseClient);

            t.start();

        } catch (Exception ex) {
            Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, Exception {

//        int portNumber = 1500;
//        int routingType = 2;
//        String readablePeerId = "p2";
//        String serverAddress = "10.203.72.24";
        String readablePeerId = args[0];
        int routingType = Integer.parseInt((args.length > 1 ? args[1] : "1"));
        int portNumber = Integer.parseInt((args.length > 2 ? args[2] : "1500"));
        String serverAddress = (args.length > 3 ? args[3] : "");
        int peerId;

        if (routingType == 1) {
            peerId = getRandomId();
            routingTable = new ArrayList<>(5);
        } else {
            peerId = getDHTID(Util.getIpAddress());
            routingTable = new ArrayList<>(dhtKey);
        }

        Parameters parameters = new Parameters(peerId, readablePeerId, serverAddress, portNumber, routingType);

        localParameters = parameters;

        Util.print("***************************************************");
        Util.print("My Peer id: " + readablePeerId + "  Random peer id:"
                + peerId + " My IP :" + Util.getIpAddress());

        Util.print("***************************************************");

        FileOperation.deleteFiles(parameters);

        FileOperation.writeToFile(parameters, "*****************************"
                + "******************************");

        String initialMessage = "My Peer id:" + readablePeerId
                + " & Random peer id " + peerId + " My IP :" + Util.getIpAddress();

        FileOperation.writeToFile(parameters, initialMessage);

        FileOperation.writeToFile(parameters, "*****************************"
                + "******************************");

        server(parameters);

        if (parameters.getServerAddress() != null && !parameters.getServerAddress().isEmpty()) {
            sendClientPing(parameters);
        }

        Timer timer = new Timer();

        // send ping message to each neighbours
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    sendClientPingWithRouting(parameters);

                } catch (Exception ex) {
                    Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }, 0, 30000);

        // print the Routing table
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    Util.print("Printing Routing table : " + getRoutingPeerIds());
                    FileOperation.printRoutingTable(parameters, routingTable);

                } catch (IOException ex) {
                    Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }, 300000, 300000); 

        // search message
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Search(parameters, getSearchMessage(), false);

                } catch (IOException ex) {
                    Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private SearchMessage getSearchMessage() throws UnknownHostException {
                searchMessageId++;

                int randomSearchPeerId = getRandomId();

                SearchMessage searchMessage = new SearchMessage(searchMessageId,
                        parameters.getpeerId(),
                        randomSearchPeerId);

                searchMessage.setMinimumDifference(Math.abs(randomSearchPeerId - parameters.getpeerId()));

                return searchMessage;
            }

        }, 600000, 600000);
    }

    private static int getRandomId() {
        Random r = new Random();
        int Low = 0;
        int High = 65536;
        return r.nextInt(High - Low) + Low;
    }

    static void Search(Parameters parameters, SearchMessage searchMessage, boolean isFromServer) throws IOException {

        try {

            if (routingTable == null || routingTable.size() == 0) {
                return;
            }

            Routing neighbourRoutingValue = null;

            if (localParameters.isRandomRoutingType()) {

                int smallestDifference = localParameters.getpeerId() > searchMessage.getsearchPeerId()
                        ? localParameters.getpeerId() - searchMessage.getsearchPeerId()
                        : searchMessage.getsearchPeerId() - localParameters.getpeerId();

                boolean isNearestPeerFound = false;

                for (Routing routeValue : routingTable) {

                    Util.print("Search for " + searchMessage.getsearchPeerId());

                    Util.print(routeValue.getPeerId() + "_" + routeValue.getServerAddress());

                    int diffrence = routeValue.getPeerId() > searchMessage.getsearchPeerId()
                            ? routeValue.getPeerId() - searchMessage.getsearchPeerId()
                            : searchMessage.getsearchPeerId() - routeValue.getPeerId();

                    if (diffrence < smallestDifference) {

                        searchMessage.setMinimumDifference(diffrence);

                        neighbourRoutingValue = routeValue;

                        smallestDifference = diffrence;

                        isNearestPeerFound = true;
                    }

                }

                if (isNearestPeerFound) {

                    searchMessage.incermentHop();

                    Util.print("Found Neighbour " + neighbourRoutingValue.getPeerId());

                    sendSearch(neighbourRoutingValue.getServerAddress(),
                            localParameters.getportNumber(),
                            searchMessage);

                } else {

                    if (isFromServer) {
                        Util.print("Found Z Node " + localParameters.getpeerId());

                        searchMessage.setZPeerId(localParameters.getpeerId());
                        searchMessage.setZIpAddress(Util.getIpAddress());
                        searchMessage.setZReadablePeerId(localParameters.getreadablePeerId());

                        sendSearchResponse(searchMessage.getSenderIpAddress(),
                                localParameters.getportNumber(), searchMessage);
                    }

                }

            } else {

                for (Routing routing : routingTable) {
                    int lower = routing.getPeerId();

                    for (int i = 0; i < dhtKey; i++) {

                        int upper = lower + 2 ^ i;

                        if (lower <= searchMessage.getsearchPeerId() && searchMessage.getsearchPeerId() < upper) {

                            neighbourRoutingValue = routing;
                        }

                        lower = upper;
                    }
                }

                if (neighbourRoutingValue != null) {

                    searchMessage.incermentHop();

                    Util.print("Found Neighbour " + neighbourRoutingValue.getPeerId());

                    sendSearch(neighbourRoutingValue.getServerAddress(),
                            localParameters.getportNumber(),
                            searchMessage);
                } else {
                    if (isFromServer) {
                        Util.print("Found Z Node " + localParameters.getpeerId());

                        searchMessage.setZPeerId(localParameters.getpeerId());
                        searchMessage.setZIpAddress(Util.getIpAddress());
                        searchMessage.setZReadablePeerId(localParameters.getreadablePeerId());

                        sendSearchResponse(searchMessage.getSenderIpAddress(),
                                localParameters.getportNumber(), searchMessage);
                    }
                }

            }

        } catch (Exception ex) {
            Util.print(ex.getMessage());
        }
    }

    static void addRoutingValue(Routing routing, boolean isSearch) throws IOException, Exception {

        //if (localParameters.isRandomRoutingType()) {
        boolean isFound = false;

        for (Routing r : routingTable) {

            if (r.getPeerId() == routing.getPeerId() || r.getServerAddress() == routing.getServerAddress()) {
                isFound = true;
            }

        }

        if (!isFound && routingTable.size() < 5) {
            routingTable.add(routing);

            if (isSearch) {
                Util.print("Added routing when search "
                        + routing.getPeerId() + " IP "
                        + routing.getServerAddress());
            }

        }
//        } else {
//
//            //modifyCircle(routing, false);
//            addRoutingToFingerTable(routing);
//        }

    }

    static void addRoutingValueIfEmpty(Routing routing) throws IOException, Exception {

        //if (localParameters.isRandomRoutingType()) {
        if (routingTable.isEmpty()) {
            routingTable.add(routing);

            Util.print("Added routing at Empty " + routing.getPeerId() + " IP " + routing.getServerAddress());
        }
        //} else {

        //modifyCircle(routing, true);
        //addRoutingToFingerTable(routing);
        //}
    }

    static String getRoutingPeerIds() {
        String routePeerIds = "";

        routePeerIds = routingTable.stream().map((r) -> r.getPeerId() + ",").reduce(routePeerIds, String::concat);

        return routePeerIds;
    }

    static int getDHTID(String ip)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md = MessageDigest.getInstance("SHA-1");

        md.update(ip.getBytes("iso-8859-1"), 0, ip.length());

        BigInteger hashValueNumber = new BigInteger(md.digest());

        int nodeID = Math.abs(hashValueNumber.intValue()) % twoPowM;

        return nodeID;

    }

    private static void addRoutingToFingerTable(Routing routing) {
        int lower = localParameters.getpeerId();

        routingTable.add(routing);

//        for (int i = 0; i < dhtKey; i++) {
//
//            int upper = lower + 2 ^ i;
//
//            if (lower <= routing.getPeerId() && routing.getPeerId() < upper) {
//                routingTable.add(i, routing);
//            }
//
//            lower = upper;
//        }
    }

    private static void modifyCircle(Routing routing, boolean isEmpty) throws Exception {

//        Util.print("Is From Empty method : " + isEmpty);
//
//        Util.print("localParameters.getpeerId() : " + localParameters.getpeerId());
//
//        Util.print("routing.getPeerId() " + routing.getPeerId());
        if (localParameters.getpeerId() > routing.getPeerId()) {

            if (predecessor != null) {

                if (predecessor.getPeerId() != routing.getPeerId()) {

                    sendClientPing(new Parameters(routing.getPeerId(),
                            routing.getReadablePeerId(),
                            predecessor.getServerAddress(),
                            localParameters.getportNumber(),
                            localParameters.getRoutingType()));
                }

            }

            predecessor = routing;

        } else {
            if (successor != null) {
                if (successor.getPeerId() != routing.getPeerId()) {

                    sendClientPing(new Parameters(routing.getPeerId(),
                            routing.getReadablePeerId(),
                            successor.getServerAddress(),
                            localParameters.getportNumber(),
                            localParameters.getRoutingType()));
                }

            }

            successor = routing;
        }

    }

}
