
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
public class RoutingTable implements Serializable {

    private final int peerId;

    private final String ipAddress;

    RoutingTable(int peerId, String ipAddress) {
        this.peerId = peerId;
        this.ipAddress = ipAddress;
    }

    int getPeerId() {
        return this.peerId;
    }

    String getServerAddress() {
       return this.ipAddress;
    }
}
