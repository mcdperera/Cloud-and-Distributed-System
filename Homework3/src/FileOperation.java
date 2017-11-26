
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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

    static String outputFolder = "output";

    private static String getroutingTableFilePath() {
        return outputFolder + File.separator + peerId + "RoutingTable.txt";
    }

    static String delimeter = ":";

    static void createFiles(String readablePeerId) throws IOException {

        peerId = readablePeerId;

        new FileWriter(getroutingTableFilePath());
    }

    public static ArrayList<RoutingTable> getRoutingTable(String readablePeerId) throws FileNotFoundException, IOException {

        peerId = readablePeerId;

        BufferedReader br = new BufferedReader(new FileReader(getroutingTableFilePath()));

        String line;

        ArrayList<RoutingTable> routingTable = new ArrayList<>();

        while ((line = br.readLine()) != null) {

            try {
                String[] content = line.split(delimeter);

                routingTable.add(new RoutingTable(Integer.parseInt(content[0]), content[1]));

            } catch (Exception e) {
                System.out.println(line);
                System.out.println(e.getMessage());
            }

        }

        try {
            br.close();
        } catch (IOException e) {
            System.out.println("Failed to close buffered reader");
        }

        return routingTable;
    }

    static void setRoutingTable(RoutingTable routingTableValue) throws IOException {
        try (FileWriter writer = new FileWriter(getroutingTableFilePath(), true)) {
            writer.write(routingTableValue.getPeerId() + delimeter + routingTableValue.getServerAddress() + "\n");
        }
    }

    static void printRoutingTable(Parameters parameters) throws IOException {

        ArrayList<RoutingTable> routingTable = getRoutingTable(parameters.getreadablePeerId());

        String routingTableMessage = "";

        if (routingTable.size() > 0) {
            routingTableMessage = "*_Routing Table_*" + "\n";
        }

        for (RoutingTable routingValue : routingTable) {
            routingTableMessage += routingValue.getPeerId() + delimeter + routingValue.getServerAddress();
        }

        writeToFile(parameters, routingTableMessage);
    }

    private static String getOutputFilename(String readablePeerId, boolean isRandomRoutingType) {
        return readablePeerId + (isRandomRoutingType ? "random" : "dht") + ".txt";
    }

    static void writeToFile(Parameters parameters, String message) throws FileNotFoundException, IOException {

        String outputFilePath = outputFolder + File.separator
                + getOutputFilename(parameters.getreadablePeerId(), parameters.isRandomRoutingType());

        try (FileWriter writer = new FileWriter(outputFilePath, true)) {
            writer.write(message + "\n");
        }
    }

}
