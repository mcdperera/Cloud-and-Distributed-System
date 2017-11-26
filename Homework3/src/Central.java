
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

    /**
     *
     */
    public static Parameters parameters;

    private static int searchMessageId;

    private static void server() throws IOException {
        Server.run(parameters);
    }

    private static void client() throws IOException {
        Client.run(parameters);
    }

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        String serverAddress = "";//10.203.72.24";
        int portNumber = 1500;
        int routingType = 1;
        String readablePeerId = "P2";
        int peerId = getRandomId();

//        String readablePeerId = args[0];
//        int routingType = Integer.parseInt((args.length > 1 ? args[1] : "1"));
//        int portNumber = Integer.parseInt((args.length > 2 ? args[2] : "1500"));
//        String serverAddress = (args.length > 3 ? args[3] : "");
        FileOperation.createFiles(readablePeerId);

        parameters = new Parameters(peerId, readablePeerId, serverAddress, portNumber, routingType);

        FileOperation.writeToFile(parameters, "**************************"
                + "**********");

        String initialMessage = "My Peer id:" + readablePeerId
                + " & Random peer id " + peerId;

        FileOperation.writeToFile(parameters, initialMessage);

        FileOperation.writeToFile(parameters, "*************************"
                + "***********");

        new Thread() {
            @Override
            public void run() {
                try {
                    server();
                } catch (IOException e) {
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    client();
                } catch (IOException e) {
                }
            }
        }.start();

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    FileOperation.printRoutingTable(parameters);
                } catch (IOException ex) {
                    Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }, 0, 40000);

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    Server.Search(getSearchMessage());
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

        },
                100000, 60000);
    }

    private static int getRandomId() {
        Random r = new Random();
        int Low = 0;
        int High = 65536;
        return r.nextInt(High - Low) + Low;
    }
}
