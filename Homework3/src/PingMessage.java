
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

    private String senderReadablePeerId;

    private int senderPingId;

    private int senderPeerId;

    private String senderIpAddress;

    private String receiverReadablePeerId;

    private int receiverPeerId;

    private String receiverIpAddress;

    private long startTime;

    PingMessage(int peerId) {
        this.senderPeerId = peerId;
    }

    PingMessage(String senderReadablePeerId, int senderPingId, int senderPeerId,
            String senderIpAddress, String receiverIpAddress) {

        this.senderReadablePeerId = senderReadablePeerId;
        this.senderPingId = senderPingId;
        this.senderPeerId = senderPeerId;
        this.senderIpAddress = senderIpAddress;

        this.receiverIpAddress = receiverIpAddress;

        this.startTime = System.nanoTime();
    }

    int getPingId() {
        return this.senderPingId;
    }

    int getSenderPeerId() {
        return senderPeerId;
    }

    String getSernderIpAddress() {
        return senderIpAddress;
    }
  String getSenderReadablePeerId() {
        return senderReadablePeerId;
    }
    
//
//    void setReceiverIpAddresss(String receiverIpAddress) {
//        this.receiverIpAddress = receiverIpAddress;
//    }
//
    String getReceiverIpAddress() {
        return receiverIpAddress;
    }

    void setReceiverReadablePeerId(String receiverReadablePeerId) {
        this.receiverReadablePeerId = receiverReadablePeerId;
    }

    void setReceiverPeerId(int toPeerId) {
        this.receiverPeerId = toPeerId;
    }

    int getReceiverPeerId() {
        return receiverPeerId;
    }

    @Override
    public String toString() {

        return "Ping " + this.senderPeerId + ":"
                + this.senderIpAddress + ":" + this.receiverPeerId
                + ":" + this.receiverIpAddress + ":"
                + ((System.nanoTime() - this.startTime) / 1000000.0);
    }

}
