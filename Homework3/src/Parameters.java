
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 502759576
 */
public class Parameters {

    private int peerId;

    private String readablePeerId;

    private String serverIpAddress;

    private int serverPortNumber;

    private boolean isRandomRoutingType;

    private PingMessage pingMessage;

    private int searchPeerId;

    private SearchMessage searchMessage;

    private List<Routing> routingTable;

    private int routingType;

    public Parameters(int peerId, String readablePeerId, String serverIpAddress,
            int portNumber, int routingType) {
        this.peerId = peerId;
        this.readablePeerId = readablePeerId;
        this.serverIpAddress = serverIpAddress;
        this.serverPortNumber = portNumber;
        this.isRandomRoutingType = (routingType == 1);
        this.routingType = routingType;
    }

    int getpeerId() {
        return peerId;
    }

    String getreadablePeerId() {
        return readablePeerId;
    }

    int getportNumber() {
        return serverPortNumber;
    }

    int getRoutingType() {
        return routingType;
    }

    void setSearchPeerId(int searchPeerId) {
        this.searchPeerId = searchPeerId;
    }

    int getSearchPeerId() {
        return this.searchPeerId;
    }

    void setServerAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }

    String getServerAddress() {
        return serverIpAddress;
    }

    boolean isRandomRoutingType() {
        return isRandomRoutingType;
    }

    void setSearchMessage(SearchMessage searchMessage) {
        this.searchMessage = searchMessage;
    }

    SearchMessage getSearchMessage() {
        return this.searchMessage;
    }

    void setPingMessage(PingMessage pingMessage) {
        this.pingMessage = pingMessage;
    }

    PingMessage getPingMessage() {
        return this.pingMessage;
    }

    void setRoutingTable(List<Routing> routingTable) {
        this.routingTable = routingTable;
    }

    List<Routing> getRoutingTable() {
        return this.routingTable;
    }

}
