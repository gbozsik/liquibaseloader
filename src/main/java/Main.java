import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private StringBuilder builder = new StringBuilder();
    private int count = 0;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.processCsv();
    }

    private void processCsv() throws IOException {
        buildFixPart();
        buildImportedPart();
        writeToFile();
    }

    public void buildFixPart() {
        builder.append("- changeSet: " + System.lineSeparator());
        builder.append(" \t id: DC-200-201901151800-" + System.lineSeparator());
        builder.append(" \t author: BG4" + System.lineSeparator());
        builder.append(" \t changes:" + System.lineSeparator());
    }


    public void buildStaticLines() {
        builder.append(" \t- insert:" + System.lineSeparator());
        builder.append(" \t\t\t tableName: CUSTOMER_RISK_RATING" + System.lineSeparator());
        builder.append(" \t\t\t columns:" + System.lineSeparator());
        builder.append(" \t\t\t - column:" + System.lineSeparator());
        builder.append(" \t\t\t\t\t\t name: CREATED_AT" + System.lineSeparator());
        builder.append(" \t\t\t\t\t\t value: GETDATE()" + System.lineSeparator());
        builder.append(" \t\t\t - column:" + System.lineSeparator());
        builder.append(" \t\t\t\t\t\t name: MODIFIED_AT" + System.lineSeparator());
        builder.append(" \t\t\t\t\t\t value: GETDATE()" + System.lineSeparator());
        builder.append(" \t\t\t - column:" + System.lineSeparator());
        builder.append(" \t\t\t\t\t\t name: VALID_FROM" + System.lineSeparator());
        builder.append(" \t\t\t\t\t\t value: GETDATE()" + System.lineSeparator());
        builder.append(" \t\t\t - column:" + System.lineSeparator());
        builder.append(" \t\t\t\t\t\t name: VERSION" + System.lineSeparator());
        builder.append(" \t\t\t\t\t\t value: 0" + System.lineSeparator());
    }

    public void buildImportedPart() {
        String csvFile = "/home/gbozsik/Downloads/CUSTOMER_RISK_RATING_csv.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                buildStaticLines();
                // use comma as separator
                String[] importedSplittedLine = line.split(cvsSplitBy);

                builder.append(" \t\t\t - column:" + System.lineSeparator());
                builder.append(" \t\t\t\t\t\t name: CODE" + System.lineSeparator());
                builder.append(" \t\t\t\t\t\t value: " + importedSplittedLine[0] + System.lineSeparator());
                builder.append(" \t\t\t - column:" + System.lineSeparator());
                builder.append(" \t\t\t\t\t\t name: DESCRIPTION" + System.lineSeparator());
                builder.append(" \t\t\t\t\t\t value:  " + importedSplittedLine[1] + System.lineSeparator());
                builder.append(" \t\t\t - column:" + System.lineSeparator());
                builder.append(" \t\t\t\t\t\t name: I18N" + System.lineSeparator());
                builder.append(" \t\t\t\t\t\t value: '[{\"lang\": \"hu\",\"key\": \"code\",\"text\": \"" + importedSplittedLine[0] + "\"}, {\"lang\": \"en\",\"key\": \"code\",\"text\": \"" + importedSplittedLine[0] + "\"}]'" + System.lineSeparator());
                count++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void writeToFile() throws IOException {
        final BufferedWriter writer;
        final Path dst = Paths.get("/home/gbozsik/Downloads/generated_CUSTOMER_RISK_RATING.csv");

        writer = Files.newBufferedWriter(dst, StandardCharsets.UTF_8);
        writer.write(builder.toString());
        System.out.println(builder);
        System.out.println("Insertek száma: " + count);
    }

}
