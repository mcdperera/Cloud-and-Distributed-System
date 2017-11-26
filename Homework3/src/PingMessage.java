
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
public class PingMessage implements Serializable {

    private String readablePeerId;

    private int pingId;

    private int senderPeerId;

    private String senderIpAddress;

    private int receiverPeerId;

    private String receiverIpAddress;

    private long startTime;

    PingMessage(int peerId) {
        this.senderPeerId = peerId;
    }

    PingMessage(String readablePeerId, int pingId, int senderPeerId) {
        this.readablePeerId = readablePeerId;
        this.pingId = pingId;
        
        this.senderPeerId = senderPeerId;
        this.senderIpAddress = Util.getIpAddress();
        
        this.startTime = System.nanoTime();
    }

    int getPingId() {
        return this.pingId;
    }

    int getSenderPeerId() {
        return senderPeerId;
    }

    String getSernderIpAddress() {
        return senderIpAddress;
    }

    void setReceiverIpAddress() {
        this.receiverIpAddress = Util.getIpAddress();
    }

    String getReceiverIpAddress() {
        return receiverIpAddress;
    }
    
    void setReceiverPeerId(int toPeerId) {
        this.receiverPeerId = toPeerId;
    }

    int getReceiverPeerId() {
        return receiverPeerId;
    }
    
    @Override
    public String toString() {
        return "Ping " + this.readablePeerId + ":" + this.senderPeerId + ":"
                + this.senderIpAddress + ":" + this.receiverPeerId
                + ":" + this.receiverIpAddress + ":"
                + ((System.nanoTime() - this.startTime) / 1000000.0);
    }

}
