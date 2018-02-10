package lemmatization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Semen on 10.02.2018.
 */
public interface Lemmatizer {

    String lemmatize(String word, String postag) throws IOException;
    ArrayList<String> getWords(String fileName, HashMap<String, Integer> stopWords);
}
