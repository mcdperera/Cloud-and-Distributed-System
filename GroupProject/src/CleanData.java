
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import org.json.simple.parser.JSONParser;

/**
 * Cleaning the data process
 *
 * @author Charmal
 */
public class CleanData {

    /**
     * While cleaning the data it write to a file.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

//        String inputPath = "input";
//        String outputPath = "output" + File.separator + "output.txt";

        String outputPath;
        String inputPath;

        if (args.length != 2) {
            System.out.println("User needs to enter 2 arguments to enter to the server");
            return;
        }

        inputPath = args[0];
        outputPath = args[1];

        DataCleaner(inputPath, outputPath);
    }

    /**
     * Data cleaning method.
     *
     * @param args
     * @throws IOException
     */
    private static void DataCleaner(String inputPath, String outputPath) {
        try {
            try (PrintWriter writer = new PrintWriter(outputPath)) {
                File dirr = new File(inputPath);
                File[] files = dirr.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        try ( //parse the json file using json object
                                FileReader fileReader = new FileReader(file)) {
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            String line;

                            JSONParser parser = new JSONParser();

                            JSONObject jObj;
                            String timestamp, message, id, geo, username, timezone;
                            Date date;
                            int badRecordCounter = 0;

                            SimpleDateFormat twitterDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy",
                                    Locale.ENGLISH);

                            SimpleDateFormat simpleTime = new SimpleDateFormat("dd-MM-yyyy-HH:mm");

                            while ((line = bufferedReader.readLine()) != null) {
                                try {

                                    jObj = (JSONObject) parser.parse(line);

                                    id = jObj.get("id_str").toString();

                                    // time 
                                    timestamp = jObj.get("created_at").toString();
                                    date = twitterDateFormat.parse(timestamp);
                                    timestamp = simpleTime.format(date);

                                    message = jObj.get("text").toString();

                                    // geo location details but all the time it's null
                                    geo = jObj.get("geo") == null ? "No Geo" : jObj.get("geo").toString();

                                    // access user object
                                    JSONObject userObj = (JSONObject) parser.parse(jObj.get("user").toString());
                                    username = userObj.get("screen_name").toString();
                                    timezone = userObj.get("time_zone") == null ? "No Time" : userObj.get("time_zone").toString();

//                                    if (message.toLowerCase().contains("trumph") || message.toLowerCase().contains("republican")) {
//
//                                        writer.println(removeMulitiLines(message));//message);
//                                    }

                                    writer.println(username + "::" + timestamp + "::" + timezone + "::" + removeMulitiLines(message));
                                    
                                } catch (ParseException ex) {
                                    badRecordCounter++;
                                }
                            }

                            if (badRecordCounter != 0) {
                                System.out.println("Bad record count " + badRecordCounter + " for file " + file.getPath());
                            }
                        }
                    }
                }
            }
        } catch (IOException | java.text.ParseException ex) {
        }

    }

    private static String removeMulitiLines(String message) {
        String[] lines = message.split("\r\n|\r|\n\r\n|\r|\n\r\n|\r|\n\r\n|\r|\n");

        String onelineMessage = "";
        for (String str : lines) {
            onelineMessage += str;
        }

        return onelineMessage;
    }
}
