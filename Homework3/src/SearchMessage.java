
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
public class SearchMessage implements Serializable {

    private int searchId;

    private int senderPeerId;

    private String senderIpAddress;

    private int searchPeerId;

    private int numberOfHops;

    private int peerIdOfZ;

    private int difference;

    private long startTime;

    SearchMessage(int searchId, int senderPeerId, int searchPeerId) {
        this.searchId = searchId;
        this.senderPeerId = senderPeerId;
        this.senderIpAddress = Util.getIpAddress();

        this.searchPeerId = searchPeerId;
        this.startTime = System.nanoTime();
    }

    int getPingId() {
        return this.searchId;
    }

    int getFromPeerId() {
        return senderPeerId;
    }

    String getFromIpAddress() {
        return senderIpAddress;
    }

    int getsearchPeerId() {
        return searchPeerId;
    }

    void setIdOfZ(int peerIdOfZ) {
        this.peerIdOfZ = peerIdOfZ;
    }

    void incermentHop() {
        numberOfHops++;
    }

    @Override
    public String toString() {
        return "search " + this.senderPeerId + ":" + this.searchId + ":" + this.searchPeerId
                + ":" + this.numberOfHops + ":" + ((System.nanoTime() - this.startTime) / 1000000.0) + ":" + peerIdOfZ;
    }

    void incrementHop() {
        numberOfHops++;
    }

    void setMinimumDifference(int minimumDifference) {
        this.difference = minimumDifference;
    }

    int getMinimumDifference() {
        return this.difference;
    }

}
