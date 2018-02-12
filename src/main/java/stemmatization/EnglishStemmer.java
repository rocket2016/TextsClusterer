package stemmatization;

import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import tokenization.Tokenizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class EnglishStemmer implements Stemmer {

    public final static  PorterStemmer stem = new PorterStemmer();
    @Override
    public String stem(String word) {
        word = word.toLowerCase();
        return stem.stem(word);
    }

    @Override
    public ArrayList<String> getWords(String fileName, HashMap<String, Integer> stopWords) {

        ArrayList<String> result = new ArrayList<>();

        try {
            InputStreamFactory isf = new MarkableFileInputStreamFactory(new File(fileName));
            ObjectStream<String> lineStream = new PlainTextByLineStream(isf, "UTF-8");
            String line;
            while ((line = lineStream.read()) != null) {


                String[] tokens = Tokenizer.tokenize(line);

                for (int i = 0; i < tokens.length; i++) {
                    String word = stem(tokens[i]);

                    if (stopWords.get(word) == null) {

                        result.add(word);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

//    public static void main(String[] args) {
//        Stemmer stemmer = new EnglishStemmer();
//        System.out.println(stemmer.stem("ортогональний"));
//    }
}
