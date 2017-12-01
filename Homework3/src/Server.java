
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
public class Server implements Runnable {

    private static Parameters Parameters;

    public Server(Parameters parameters) {
        Server.Parameters = parameters;
    }

    public void run() {

        boolean keepGoing = true;

        try (ServerSocket listener = new ServerSocket(Server.Parameters.getportNumber())) {
            while (keepGoing) {

                Socket socket = listener.accept();
                //try {
                ObjectInputStream sInput = new ObjectInputStream(socket.getInputStream());

                Message cm = (Message) sInput.readObject();

                switch (cm.getType()) {

                    case EXIT:
                        keepGoing = false;
                        break;

                    case PING:

                        PingMessage initialPing = cm.getPingMessage();

                        //Util.print("Server PING msg : " + Server.Parameters.getpeerId() + Server.Parameters.getreadablePeerId());
                        initialPing.setReceiverPeerId(Server.Parameters.getpeerId());
                        initialPing.setReceiverReadablePeerId(Server.Parameters.getreadablePeerId());

                        Parameters newParameters = Server.Parameters;

                        newParameters.setServerAddress(initialPing.getSernderIpAddress());
                        newParameters.setPingMessage(initialPing);

                        Central.sendClientPingResponse(newParameters, initialPing.getSernderIpAddress(), initialPing);

                        Central.addRoutingValueIfEmpty(new Routing(initialPing.getSenderPeerId(),
                                initialPing.getSernderIpAddress(),
                                initialPing.getSenderReadablePeerId()));

                        break;
                    case PINGRESPONSE:

                        PingMessage responsePing = cm.getPingMessage();

                        FileOperation.writeToFile(Server.Parameters, responsePing.toString());

                        Central.addRoutingValue(new Routing(responsePing.getReceiverPeerId(),
                                responsePing.getReceiverIpAddress(),
                                responsePing.getSenderReadablePeerId()), false);

                        break;

                    case SEARCH:

                        SearchMessage searchMessage = cm.getSearchMessage();

                        Util.print("My PeerId " + Server.Parameters.getreadablePeerId() + "Search for " + searchMessage.getsearchPeerId());

                        Central.Search(Server.Parameters, searchMessage, true);

                        break;
                    case SEARCHFINISH:
                        SearchMessage finishSearchMessage = cm.getSearchMessage();

                        try {

                            Util.print("Try to adding Z value to routing table");

                            FileOperation.writeToFile(Server.Parameters, finishSearchMessage.toString());

                            Central.addRoutingValue(new Routing(finishSearchMessage.getZPeerId(),
                                    finishSearchMessage.getZIpAddress(),finishSearchMessage.getZReadablePeerId()), true);

                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        break;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
