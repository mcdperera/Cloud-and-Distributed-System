
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class PingResponseClient implements Runnable {

    private final Parameters parameters;

    public PingResponseClient(Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public void run() {
        try {
            
            try (Socket s = new Socket(parameters.getServerAddress(), parameters.getportNumber());
                    ObjectOutputStream sOutput = new ObjectOutputStream(s.getOutputStream())) {

                Message message = new Message(MessageType.PINGRESPONSE, "PINGRESPONSE");

                message.setPingMessage(parameters.getPingMessage());

                sOutput.writeObject(message);
            }

        } catch (Exception ex) {
            Util.print(ex.getMessage());
        }
    }
}
