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
    private String line = "";
    private String cvsSplitBy = ",";

    //File what you read
    private String csvFile = "/home/gbozsik/Downloads/CUST_RISK_SEGMENT_RATING.csv";

    //File to write
    final Path dst = Paths.get("/home/gbozsik/Downloads/generated_cust_risk_segment_rating.csv");


    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.processCsv();
    }

    private void processCsv() throws IOException {
        /*********FROM CSV TO YAML*************/
//        buildFixPart();
//        buildImportedPart();
        /*********FROM CSV TO YAML**************
         **OR**
         **********FROM CSV TO NATIVE SQL********/
        createSqlInsert();
        /*********FROM CSV TO NATIVE SQL********/

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

    public void buildImportedPart() throws FileNotFoundException {
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
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
        writer = Files.newBufferedWriter(dst, StandardCharsets.UTF_8);
        writer.write(builder.toString());
        System.out.println(builder);
        System.out.println("Insertek száma: " + count);
        writer.close();
    }

    private void createSqlInsert() throws IOException {
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(csvFile));
        br.readLine();
        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] importedSplittedLine = line.split(cvsSplitBy);

            builder.append("INSERT INTO cust_risk_segment_rating (customer_risk_segment_id, customer_risk_rating_id, valid_from) values ((SELECT id FROM customer_risk_segment WHERE code='" +
                    importedSplittedLine[0] + "'), (select id from CUSTOMER_RISK_RATING where code='" + importedSplittedLine[1] + "'), (GETDATE()))" + System.lineSeparator());
            count++;
        }
    }
}
