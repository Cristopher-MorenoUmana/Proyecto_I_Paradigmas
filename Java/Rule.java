package Java;

public abstract class Rule {

    protected String operator;
    protected double threshold;

    public Rule(String operator, double threshold) {
        this.operator = operator;
        this.threshold = threshold;
    }

    public abstract boolean evaluate(double metric);

    public abstract String getMetricName();

    public abstract String getRuleName();
}