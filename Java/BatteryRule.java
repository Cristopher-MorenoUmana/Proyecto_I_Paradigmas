package Java;

public class BatteryRule extends Rule {

    public BatteryRule(String operator, double threshold) {
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
        return "BATERIA_PROMEDIO";
    }

    @Override
    public String getRuleName() {
        return "BATERIA_BAJA";
    }
}