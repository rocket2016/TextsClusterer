package entities;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class Article implements Clusterable {

    private String name;
    private String text;
    private double[] vector;

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public double[] getVector() {
        return vector;
    }

    public Article(String name, String text, double[] vector) {
        this.name = name;
        this.text = text;
        this.vector = vector;
    }

    public Article(String name, double[] vector) {
        this(name, null, vector);
    }

    @Override
    public double[] getPoint() {
        return vector;
    }
}
