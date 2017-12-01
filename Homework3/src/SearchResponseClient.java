
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class SearchResponseClient implements Runnable {

    private final String serverAddress;
    private final int portNumber;
    private final SearchMessage searchMessage;

    public SearchResponseClient(String serverAddress, int portNumber, SearchMessage searchMessage) {
        this.serverAddress = serverAddress;
        this.portNumber = portNumber;
        this.searchMessage = searchMessage;
    }

    @Override
    public void run() {
        try {

            try (Socket s = new Socket(this.serverAddress, this.portNumber);
                    ObjectOutputStream sOutput = new ObjectOutputStream(s.getOutputStream())) {

                Message message = new Message(MessageType.SEARCHFINISH, "SEARCHFINISH");

                message.setSearchMessage(this.searchMessage);

                sOutput.writeObject(message);
            }

        } catch (IOException ex) {
            Util.print(ex.getMessage());
        }
    }
}
