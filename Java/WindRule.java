package Java;

public class WindRule extends Rule {

    public WindRule(String operator, double threshold) {
        super(operator, threshold);
    }

    @Override
    public boolean evaluate(double metric) {

        return switch (operator) {
            case ">" -> metric > threshold;
            case "<" -> metric < threshold;
            case ">=" -> metric >= threshold;
            case "<=" -> metric <= threshold;
            default -> false;
        };
    }

    @Override
    public String getMetricName() {
        return "VIENTO_MAXIMO";
    }

    @Override
    public String getRuleName() {
        return "VIENTO_FUERTE";
    }
}