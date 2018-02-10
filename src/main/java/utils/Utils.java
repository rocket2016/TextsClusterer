package utils;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by Semen on 10.02.2018.
 */
public class Utils {

    public static String readFile(String path){
        String result = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            Charset cs = Charset.forName("windows-1251");
            result = new String(encoded, cs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void addWord(String word, HashMap<String, Integer> words) {
        Integer count = words.get(word);
        count = count == null ? 1 : count + 1;
        words.put(word, count);
    }
}
