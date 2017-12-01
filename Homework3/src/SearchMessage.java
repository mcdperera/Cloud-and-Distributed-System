
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

    private String zIpAddress;

    private String zReadablePeerId;

    private int difference;

    private int neighbourPeerId;

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

    int getSenderPeerId() {
        return senderPeerId;
    }

    String getSenderIpAddress() {
        return senderIpAddress;
    }

    int getsearchPeerId() {
        return searchPeerId;
    }

    void setZPeerId(int peerIdOfZ) {
        this.peerIdOfZ = peerIdOfZ;
    }

    int getZPeerId() {
        return this.peerIdOfZ;
    }

    void setZIpAddress(String zIpAddress) {
        this.zIpAddress = zIpAddress;
    }

    String getZIpAddress() {
        return this.zIpAddress;
    }

    void setZReadablePeerId(String zReadablePeerId) {
        this.zReadablePeerId = zReadablePeerId;
    }

    String getZReadablePeerId() {
        return this.zReadablePeerId;
    }

    void incermentHop() {
        numberOfHops++;
    }

    void setMinimumDifference(int minimumDifference) {
        this.difference = minimumDifference;
    }

    int getMinimumDifference() {
        return this.difference;
    }

    void setNeighbourPeerId(int neighbourPeerId) {
        this.neighbourPeerId = neighbourPeerId;
    }

    int getNeighbourPeerId() {
        return this.neighbourPeerId;
    }

    @Override
    public String toString() {
        return "Search " + this.senderPeerId + ":" + this.searchId + ":" + this.searchPeerId
                + ":" + this.numberOfHops + ":" + ((System.nanoTime() - this.startTime) / 1000000.0) + ":" + peerIdOfZ;
    }
}
