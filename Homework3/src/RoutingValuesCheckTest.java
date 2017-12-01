
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class RoutingValuesCheckTest {

    public static final int m = 16;//peerID space (16 bits).  Nodes and Keys share the same ID space.
    public static final int twoPowM = 65536;

    public static void main(String[] args) {

        byte[][] addresses = new byte[10][4];

        for (int i = 0; i < 10; i++) {
            addresses[i][0] = 10;
            addresses[i][1] = -53;//203
            addresses[i][2] = 72;
        }
        addresses[0][3] = 24;
        addresses[1][3] = 25;
        addresses[2][3] = 23;
        addresses[3][3] = 14;
        addresses[4][3] = 11;
        addresses[5][3] = 13;
        addresses[6][3] = 6;
        addresses[7][3] = 7;
        addresses[8][3] = 5;
        addresses[9][3] = 4;
//        
//        addresses[0][0] = 24;
//        addresses[1][0] = 25;
//        addresses[2][0] = 23;
//        addresses[3][0] = 14;
//        addresses[4][0] = 11;
//        addresses[5][0] = 13;
//        addresses[6][0] = 6;
//        addresses[7][0] = 7;
//        addresses[8][0] = 5;
//        addresses[9][0] = 4;

        try {
            for (int csxi = 0; csxi < 10; csxi++) {

                MessageDigest mDigest = MessageDigest.getInstance("SHA1");
                byte[] hashValueBytes = mDigest.digest(addresses[csxi]);
                BigInteger hashValueNumber = new BigInteger(hashValueBytes);
                int nodeID = Math.abs(hashValueNumber.intValue()) % twoPowM;

                Byte b1 = addresses[csxi][0];
                Byte b2 = addresses[csxi][1];
                Byte b3 = addresses[csxi][2];
                Byte b4 = addresses[csxi][3];

                String ipAddress = b1.toString() + ".203." + b3.toString() + "." + b4.toString();

                //System.out.println(ipAddress + "- " + nodeID);
                System.out.println(ipAddress + "- " + SHA1(ipAddress));

                //System.out.println(b1 + "- " + nodeID);
                //System.out.println(nodeID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int SHA1(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");

        byte[] sha1hash = new byte[40];

        md.update(text.getBytes("iso-8859-1"), 0, text.length());

        sha1hash = md.digest();

        BigInteger hashValueNumber = new BigInteger(sha1hash);
        int nodeID = Math.abs(hashValueNumber.intValue()) % twoPowM;

        return nodeID;

        //convertToHex(sha1hash);
    }

}
