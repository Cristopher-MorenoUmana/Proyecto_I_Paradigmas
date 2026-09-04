package Java;

public class PrecipitationRule extends Rule {

    public PrecipitationRule(String operator, double threshold) {
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
        return "PRECIPITACION_ACUMULADA";
    }

    @Override
    public String getRuleName() {
        return "LLUVIA_INTENSA";
    }
}
