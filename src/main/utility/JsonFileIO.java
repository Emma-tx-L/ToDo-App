package utility;

import model.Task;
import parsers.TaskParser;
import persistence.Jsonifier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

// File input/output operations
public class JsonFileIO {
    public static final File jsonDataFile = new File("./resources/json/tasks.json");
    
    // EFFECTS: attempts to read jsonDataFile and parse it
    //           returns a list of tasks from the content of jsonDataFile
    public static List<Task> read() {
        TaskParser parser = new TaskParser();
        return parser.parse(readFile(jsonDataFile.toString()));
    }

    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) {
        try {
            FileWriter writer = new FileWriter(jsonDataFile);
            System.out.println(Jsonifier.taskListToJson(tasks).toString());
            writer.write(Jsonifier.taskListToJson(tasks).toString());
/*            writer.write("hello");*/
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String path) {
        String output = "";

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            output =  new String(encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }
}
