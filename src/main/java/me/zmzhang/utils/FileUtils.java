package me.zmzhang.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by zmzhang2 on 8/5/17.
 */
public class FileUtils {
    public static String getFilePath(String fileName){
        File file = new File(fileName);
        return file.getPath();
    }
    public static String getFileContent(File file){
        StringBuilder result = new StringBuilder("");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
