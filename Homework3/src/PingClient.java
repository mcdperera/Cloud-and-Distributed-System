
import java.io.IOException;
import java.io.ObjectOutputStream;
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
public class PingClient implements Runnable {

    private final Parameters parameters;

    private final int pingMessageId;

    public PingClient(Parameters parameters, int pingMessageId) {
        this.parameters = parameters;
        this.pingMessageId = pingMessageId;
    }

    @Override
    public void run() {
        try (Socket s = new Socket(parameters.getServerAddress(), parameters.getportNumber());
                ObjectOutputStream sOutput = new ObjectOutputStream(s.getOutputStream())) {

            Message message = new Message(MessageType.PING, "ping message");

            PingMessage pingMessage = new PingMessage(parameters.getreadablePeerId(),
                    this.pingMessageId,
                    parameters.getpeerId(),
                    Util.getIpAddress(),
                    parameters.getServerAddress());

            message.setPingMessage(pingMessage);

            sOutput.writeObject(message);
        } catch (IOException ex) {
            Util.print(ex.getMessage());
        }
    }
}
