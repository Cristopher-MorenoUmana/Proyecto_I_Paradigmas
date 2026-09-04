package Java;

public class TemperatureRule extends Rule {

    public TemperatureRule(String operator, double threshold) {
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
        return "TEMPERATURA_MAXIMA";
    }

    @Override
    public String getRuleName() {
        return "TEMP_ALTA";
    }
}
