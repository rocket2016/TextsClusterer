package stemmatization;

import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import tokenization.Tokenizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UkrainianStemmer implements Stemmer {
    private static final Pattern INFINITIVE = Pattern.compile("(ти|учи|ячи|вши|ши|ати|яти|ючи|)$");
    //суфікси
    private static final Pattern SUFFIX = Pattern.compile("((ив|ивши|ившись)| ((&lt;=[ая])(в|вши|вшись)))");
    //закінчення
    private static final Pattern REFLEXIVE = Pattern.compile("(с[яьи])$");
    //прикметник
    private static final Pattern ADJECTIVE = Pattern.compile("(ими|ій|ий|а|е|ова|ове|ів|є|їй|єє|еє| я|ім|ем|им|ім|их|іх|ою|йми|іми|у|ю|ого|ому|о)$");
    //дієприкметник
    private static final Pattern PARTICIPLE = Pattern.compile("(ий|ого|ому|им|ім|а|ій|у|ою|ій|і|их| йми|их)$");

    private static final Pattern VERB = Pattern.compile("(сь|ся|ив|ать|ять|у|ю|ав|али|учи|ячи| вши|ши|е|ме|ати|яти|є)$");

    private static final Pattern NOUN = Pattern.compile("(а|ев|ов|е|ями|ами|еи|и|ей|ой|ий|й| иям|ям|ием|ем|ам|ом|о|у|ах|иях|ях|ы|ь|ию|ью|ю| ия|ья|я|і|ові|ї|ею|єю|ою|є|еві|ем|єм|ів|їв|ꞌю)$");

    private static final Pattern RVRE = Pattern.compile("^(.*?[аеиоуюяіїє])(.*)$");
    // формування нових слів
    private static final Pattern DERIVATIONAL = Pattern.compile("[^аеиоуюяіїє][аеиоуюяіїє]+ [^аеиоуюяіїє]+[аеиоуюяіїє].*сть?$");

    private static final Pattern DER = Pattern.compile("сть?$");
    //найвищий ступінь
    private static final Pattern SUPERLATIVE = Pattern.compile("(ищи|іши|жч)$");

    private static final Pattern I = Pattern.compile("і$");
    private static final Pattern P = Pattern.compile("ь$");
    private static final Pattern NN = Pattern.compile("нн$");

    @Override
    public String stem(String word) {
        word = word.toLowerCase();
        Matcher m = INFINITIVE.matcher(word);  //RVRE ...
        if (m.matches()) {
            String pre = m.group(0);// the parentheses are not in the group we need
            //System.out.println(pre);
            String rv = m.group(1);
            //System.out.println(rv);
            String temp = SUFFIX.matcher(rv).replaceFirst("");
            //System.out.println(temp);
            if (temp.equals(rv)) {
                rv = REFLEXIVE.matcher(rv).replaceFirst("");
                //System.out.println(temp);
                temp = ADJECTIVE.matcher(rv).replaceFirst("");
                if (!temp.equals(rv)) {
                    rv = temp;
                    rv = PARTICIPLE.matcher(rv).replaceFirst("");
                } else {
                    temp = VERB.matcher(rv).replaceFirst("");
                    if (temp.equals(rv)) {
                        rv = NOUN.matcher(rv).replaceFirst("");
                    } else {
                        rv = temp;
                    }
                }

            } else {
                rv = temp;
            }

            rv = I.matcher(rv).replaceFirst("");

            if (DERIVATIONAL.matcher(rv).matches()) {
                rv = DER.matcher(rv).replaceFirst("");
            }

            temp = P.matcher(rv).replaceFirst("");
            if (temp.equals(rv)) {
                rv = SUPERLATIVE.matcher(rv).replaceFirst("");// insert "" to rgex place
                rv = NN.matcher(rv).replaceFirst("");
                rv = P.matcher(rv).replaceFirst("");
            }else{
                rv = temp;
            }
            word = pre + rv;

        }
        System.out.println(word);
        return word;
    }

    public ArrayList<String> getWords(String fileName, HashMap<String, Integer> stopWords) {

        ArrayList<String> result = new ArrayList<>();

        try {
            InputStreamFactory isf = new MarkableFileInputStreamFactory(new File(fileName));
            ObjectStream<String> lineStream = new PlainTextByLineStream(isf, "windows-1251");
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
}
