package util;

public class Score implements Comparable<Score> {

    public double score;
    public String name;

    public Score(String name, double score) {
        this.score = score;
        this.name = name;
    }

    @Override
    public int compareTo(Score o) {
        return score < o.score ? -1 : score > o.score ? 1 : 0;
    }
}