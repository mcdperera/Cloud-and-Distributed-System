
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Damith
 */
public class Util {

    public static String getIpAddress() {

        String[] ip = null;

        try {
            ip = InetAddress.getLocalHost().toString().split("/");
        } catch (UnknownHostException ex) {
            Logger.getLogger(PingMessage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ip[1];
    }

    public static void print(String msg) {
        System.out.println(msg);
    }
}
