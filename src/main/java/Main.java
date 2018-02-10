import entities.Article;
import entities.Pair;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import stemmatization.Stemmer;
import stemmatization.UkrainianStemmer;
import utils.Utils;

import java.io.File;
import java.util.*;

public class Main {

    private static final String fileDir = "C:\\Users\\Semen\\IdeaProjects\\ClusterCreater\\filesUkr";
    private static final String stopWordsFile = "C:\\Users\\Semen\\IdeaProjects\\ClusterCreater\\stopWords\\stopwordsUkr_new.txt";

    private static final HashMap<String, Integer> stopWords = new HashMap<>();
    private static final HashMap<String, Integer> allWords = new HashMap<>();

    private static int vectorSize = 200;
    private static final LinkedHashMap<String, Integer> vectorTPL = new LinkedHashMap<>();

    private static final Stemmer stemmer = new UkrainianStemmer();

    private static final int clusterCount = 3;


    private static void initStopWords() {

        String text = Utils.readFile(stopWordsFile);
        String[] array = text.split("\n");

        stopWords.clear();

        for(String item: array) {
            stopWords.put(item.trim().toLowerCase(), 0);
        }

    }
    /**/
    public static LinkedHashMap<String, Integer> getVectorTemplate() {

        final File folder = new File(fileDir);

        for (final File fileEntry : folder.listFiles()) {


            ArrayList<String> words = stemmer.getWords(fileEntry.getAbsolutePath(), stopWords);

            for(String word: words) {

                if (stopWords.get(word) != null) {
                    continue;
                }

                Utils.addWord(word, allWords);
            }
        }

        ArrayList<Pair<String, Integer>> list = new ArrayList<>();

        for(Map.Entry<String, Integer> entry: allWords.entrySet()) {
            list.add(new Pair<String, Integer>(entry.getKey(), entry.getValue()));
        }
        list.sort(new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {//сравниваем слова по частоте
                return o2.getR() - o1.getR();
            }
        });

        vectorSize = Math.min(vectorSize, list.size());

        for(int i = 0; i < vectorSize; i++) {
            vectorTPL.put(list.get(i).getL(), 0);
        }

        return vectorTPL;

    }

    private static ArrayList<Article> vertorizeTexts() {

        ArrayList<Article> result = new ArrayList<>();

        final File folder = new File(fileDir);

        for (final File fileEntry : folder.listFiles()) {
            double[] vector = vectorizeText(fileEntry.getAbsolutePath());
            Article article = new Article(fileEntry.getName(), vector);
            result.add(article);
        }

        return result;

    }

    private static double[] vectorizeText(String path) {

//        String text = Utils.readFile(path);
//        String[] words = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        ArrayList<String> words = stemmer.getWords(path, stopWords);

        HashMap<String, Integer> tmp = new HashMap<>();

        for(String word: words) {

            if (vectorTPL.get(word) == null) {
                continue;
            }

            Utils.addWord(word, tmp);
        }

        double[] result = new double[vectorTPL.size()];

        int index = 0;
        for(Map.Entry<String, Integer> entry: vectorTPL.entrySet()) {

            Integer count = tmp.get(entry.getKey());
            count = count == null ? 0 : count;
            result[index] = (double)count / words.size();

            index++;
        }

        return result;
    }



    public static String getClusters() {

        initStopWords();
        getVectorTemplate();
        ArrayList<Article> vectors = vertorizeTexts();
//
//        RandomGenerator rg = new JDKRandomGenerator();
//        rg.setSeed(123);

        final DistanceMeasure dm = new EuclideanDistance();

        final KMeansPlusPlusClusterer<Article> clusterer = new KMeansPlusPlusClusterer<>(clusterCount, 100000, dm);

        List<CentroidCluster<Article>> clusterResults = clusterer.cluster(vectors);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clusterResults.size(); i++) {
            sb.append("\n\ncluster #" + (i + 1));
            CentroidCluster<Article> cluster = clusterResults.get(i);
            for (Article article : cluster.getPoints()) {
                sb.append("\nArticle :" + article.getName());

            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
        return sb.toString();

    }

    public static void main(String[] args) {

        getClusters();
        //System.out.println(result);

    }
}
