
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Damith
 */
public class FileOperation {

    static String peerId;

    static String delimeter = ":";

    private static String getOutputFilename(String readablePeerId, boolean isRandomRoutingType) {
        return readablePeerId + (isRandomRoutingType ? "random" : "dht") + ".txt";
    }

    static void printRoutingTable(Parameters parameters, List<Routing> routingTable) throws IOException {
        String routingTableMessage = "";

        for (int i = 0; i < routingTable.size(); i++) {

            Routing routingValue = routingTable.get(i);

            if (i == 0) {
                routingTableMessage = "*_Routing Table_*";
            }

            routingTableMessage += "\n" + routingValue.getPeerId() + delimeter + routingValue.getServerAddress();

        }

        writeToFile(parameters, routingTableMessage);
    }

    static void writeToFile(Parameters parameters, String message) throws FileNotFoundException, IOException {

        String outputFilePath = getOutputFilename(parameters.getreadablePeerId(), parameters.isRandomRoutingType());

        if (!message.isEmpty() && message != null) {
            try (FileWriter writer = new FileWriter(outputFilePath, true)) {
                writer.write(message + "\n");
            }
        }

    }

    static void deleteFiles(Parameters parameters) {
        String outputFilePath = getOutputFilename(parameters.getreadablePeerId(), parameters.isRandomRoutingType());

        File f = new File(outputFilePath);

        if (f.exists()) {
            f.delete();
        }
    }

    private static String getroutingTableFilePath() {
        return peerId + "RoutingTable.txt";
    }

    static void createFiles(String readablePeerId) throws IOException {

        peerId = readablePeerId;

        new FileWriter(getroutingTableFilePath());
    }

//    private static ArrayList<Routing> getRoutingTable(String readablePeerId) throws FileNotFoundException, IOException {
//
//        peerId = readablePeerId;
//
//        BufferedReader br = new BufferedReader(new FileReader(getroutingTableFilePath()));
//
//        String line;
//
//        ArrayList<Routing> routingTable = new ArrayList<>();
//
//        while ((line = br.readLine()) != null) {
//
//            try {
//                String[] content = line.split(delimeter);
//
//                routingTable.add(new Routing(Integer.parseInt(content[0]), content[1]));
//
//            } catch (Exception e) {
//                System.out.println(line);
//                System.out.println(e.getMessage());
//            }
//
//        }
//
//        try {
//            br.close();
//        } catch (IOException e) {
//            System.out.println("Failed to close buffered reader");
//        }
//
//        return routingTable;
//    }

//    public static void setRoutingTable(Routing routingTableValue) throws IOException {
//        try (FileWriter writer = new FileWriter(getroutingTableFilePath(), true)) {
//            writer.write(routingTableValue.getPeerId() + delimeter + routingTableValue.getServerAddress() + "\n");
//        }
//    }

//    static void printRoutingTable(Parameters parameters) throws IOException {
//
//        ArrayList<Routing> routingTable = getRoutingTable(parameters.getreadablePeerId());
//
//        String routingTableMessage = "";
//
//        if (routingTable.size() > 0) {
//            routingTableMessage = "*_Routing Table_*" + "\n";
//        }
//
//        for (Routing routingValue : routingTable) {
//            routingTableMessage += routingValue.getPeerId() + delimeter + routingValue.getServerAddress() + "\n";
//        }
//
//        writeToFile(parameters, routingTableMessage);
//    }

}
