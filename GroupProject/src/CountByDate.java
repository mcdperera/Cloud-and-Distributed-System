
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountByDate {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {

//        String date = "21-10-2017";
//        String inputPath = "output" + File.separator + "output1.txt";
//        String outputPath = "output" + File.separator + "output_200.txt";
        String date;
        String inputPath;
        if (args.length != 2) {
            System.out.println("User needs to enter 2 arguments to enter to the server");
            return;
        }

        inputPath = args[0];
        date = args[1];

        File file = new File(inputPath);

        if (!file.exists()) {
            System.out.println("File not fount :  " + inputPath);
        } else {
            doCleandataBydate(date, inputPath);
        }

    }

    private static void doCleandataBydate(String date, String inputPath) throws FileNotFoundException, IOException, ParseException {

        try {

            File inFile = new File(inputPath);

            BufferedReader br = new BufferedReader(new FileReader(inFile));

            String line;

            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            Date date1 = sdf.parse(date);

            int j = 0;
            while ((line = br.readLine()) != null) {

                try {

                    String[] tweetData = line.split("::");

                    if (tweetData.length == 3) {

                        Date date2 = sdf.parse(tweetData[0]);

                        if (date1.compareTo(date2) == 0) {
                            j++;
                        }

                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("Tweet Count: " + j);

            try {
                br.close();
            } catch (IOException e) {
                System.out.println("Failed to close buffered reader" + e.getMessage());
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
