
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Sentiment {
    
    public static void main(String[] args) throws IOException {

//        String inputPath = "output" + File.separator + "output1.txt";
//        String outputPath = "output" + File.separator + "output2.txt";
        String inputPath;
        String outputPath;

        if (args.length != 2) {
            System.out.println("User needs to enter 2 arguments to enter to the server");
            return;
        }

        inputPath = args[0];
        outputPath = args[1];

        File file = new File(inputPath);

        if (!file.exists()) {
            System.out.println("File not fount :  " + inputPath);
        } else {
            doSentimentalAnalysis(inputPath, outputPath);
        }

    }

    private static void doSentimentalAnalysis(String inputPath, String outputPath) throws FileNotFoundException, IOException {

        try {
            try (PrintWriter writer = new PrintWriter(outputPath)) {

                File inFile = new File(inputPath);

                BufferedReader br = new BufferedReader(new FileReader(inFile));

                String line;

                NLP.init();

                while ((line = br.readLine()) != null) {

                    try {
                        writer.println(line + " :  " + NLP.findSentiment(line));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("Failed to close buffered reader" + e.getMessage());
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
