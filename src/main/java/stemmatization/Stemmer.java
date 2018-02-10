package stemmatization;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Semen on 10.02.2018.
 */
public interface Stemmer {
   String stem(String word);

   ArrayList<String> getWords(String fileName, HashMap<String, Integer> stopWords);

}
