
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Damith
 */
public class Routing implements Serializable {

    private final int peerId;

    private final String ipAddress;

    private final String readablePeerId;

    Routing(int peerId, String ipAddress, String readablePeerId) {
        this.peerId = peerId;
        this.ipAddress = ipAddress;
        this.readablePeerId = readablePeerId;
    }

    int getPeerId() {
        return this.peerId;
    }

    String getServerAddress() {
        return this.ipAddress;
    }

    String getReadablePeerId() {
        return this.readablePeerId;
    }
}
