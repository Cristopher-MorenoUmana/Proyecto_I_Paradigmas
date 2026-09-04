package Java;

public class RuleData {

    private final String identifier;
    private final String operator;
    private final double value;

    public RuleData(String identifier, String operator, double value) {
        this.identifier = identifier;
        this.operator = operator;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getOperator() {
        return operator;
    }

    public double getValue() {
        return value;
    }
}
