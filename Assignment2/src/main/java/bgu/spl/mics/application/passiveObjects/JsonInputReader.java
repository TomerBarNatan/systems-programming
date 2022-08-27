package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * JsonInputReader is an object that reads a json file and returns a gson file.
 */
public class JsonInputReader {

    /**
     * gets input from json file
     * @param filePath is a path to where the json file is located
     * @return gson file with the data located on the json file
     * @throws IOException in case the file is not found in filePath
     */
    public static Input getInputFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Input.class);
        }
    }
}
