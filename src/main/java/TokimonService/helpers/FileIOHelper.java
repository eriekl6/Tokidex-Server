package TokimonService.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Helper class that is used to update our json file when necessary
 */
public class FileIOHelper {

    private static final String TOKIMON_JSON_FILE_PATH = "data/tokimon.json";

    public static void writeToFile(Object o) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(o);
        try {
            File file = new File(TOKIMON_JSON_FILE_PATH);
            FileWriter fw = new FileWriter(file);
            System.out.println("Writing Json to file: " + TOKIMON_JSON_FILE_PATH);
            System.out.println(json);
            fw.write(json);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
