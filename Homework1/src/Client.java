
import java.net.*;
import java.io.*;

/*
 * The Client that can be run both as a console or a GUI
 */
//<remarks>http://www.dreamincode.net/forums/topic/259777-a-simple-chat-program-with-clientserver-gui-optional/</remarks>
public class Client {

    /**
     * The Input stream
     */
    public ObjectInputStream sInput;

    /**
     * The output stream
     */
    private ObjectOutputStream sOutput;

    /**
     * The socket
     */
    private Socket socket;

    /**
     * Construct the client object.
     */
    Client() {

    }

    /**
     * Returns the match stat message.
     */
    public boolean start(String server, String port, String username) {

        try {
            socket = new Socket(server, Integer.parseInt(port));
        } catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        try {
            sInput = new ObjectInputStream(socket.getInputStream());

            sOutput = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        Message message = new Message(MessageType.CONNECTIONESTABLISH_CLIENTREQUEST.getValue(),
                true, "username send", false, ErrorMessageType.NONE.getValue());

        message.setConnectionMessage(new ConnectionMessage("", username));

        this.sendMessage(message);

        return true;
    }

    /*
	 * To send a message to the console or the GUI
     */
    public static void display(String msg) {
        System.out.println(msg);

    }

    /*
	 * To send a message to the server
     */
    void sendMessage(Message msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            display("Exception writing to server: " + e);
        }
    }

    /*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    public void disconnect() {
        try {
            if (sInput != null) {
                sInput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) {
                sOutput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        } // not much else I can do

    }

}
