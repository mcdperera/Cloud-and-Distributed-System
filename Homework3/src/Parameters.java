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

    private static int peerId;

    private static String readablePeerId;

    private static String serverIpAddress;

    private static int portNumber;

    private static boolean isRandomRoutingType;

    private static int searchPeerId;

    private static boolean isSearchPeer;

    private static SearchMessage searchMessage;


    public Parameters(int peerId, String readablePeerId, String serverIpAddress,
            int portNumber, int routingType) {
        Parameters.peerId = peerId;
        Parameters.readablePeerId = readablePeerId;
        Parameters.serverIpAddress = serverIpAddress;
        Parameters.portNumber = portNumber;
        Parameters.isRandomRoutingType = (routingType == 1);
    }

    int getpeerId() {
        return peerId;
    }

    String getreadablePeerId() {
        return readablePeerId;
    }

    int getportNumber() {
        return portNumber;
    }

    void setSearchPeerId(int searchPeerId) {
        Parameters.searchPeerId = searchPeerId;
    }

    int getSearchPeerId() {
        return Parameters.searchPeerId;
    }

    void setServerAddress(String serverIpAddress) {
        Parameters.serverIpAddress = serverIpAddress;
    }

    String getServerAddress() {
        return serverIpAddress;
    }

    boolean isRandomRoutingType() {
        return isRandomRoutingType;
    }

    boolean isSearchPeer() {
        return isSearchPeer;
    }

    void setIsSearchPeer(boolean isSearchPeer) {
        this.isSearchPeer = isSearchPeer;
    }

    void setSearchMessage(SearchMessage searchMessage) {
        Parameters.searchMessage = searchMessage;
    }

    SearchMessage getSearchMessage() {
        return Parameters.searchMessage;
    }

}
