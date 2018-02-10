package tokenization;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Semen on 10.02.2018.
 */
public class Tokenizer {
    public static String[] tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        StringTokenizer token = new StringTokenizer(line, sb.
                append(' ').
                append('!').
                append(',').
                append('?').
                append('-').
                append(':').
                append(';').
                append('.').
                toString());

        while (token.hasMoreTokens()) {
            tokens.add(token.nextToken());
        }
        return  tokens.toArray(new String[tokens.size()]);
    }

}
