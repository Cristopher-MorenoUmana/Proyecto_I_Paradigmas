package Java;

import java.util.Set;

public class RuleParser {

    private static final Set<String> VALID_IDENTIFIERS = Set.of(
        "TEMP_ALTA",
        "LLUVIA_INTENSA",
        "VIENTO_FUERTE",
        "BATERIA_BAJA"
    );

    private static final Set<String> VALID_OPERATORS = Set.of(
        ">",
        "<",
        ">=",
        "<="
    );

    public static RuleData parse(String line) {

        line = line.trim();

        if (line.isEmpty()) {
            throw new IllegalArgumentException("Regla vacia");
        }

        String[] parts = line.split("\\s+");

        if (parts.length != 3) {
            throw new IllegalArgumentException(
                "La regla debe tener: IDENTIFICADOR OPERADOR NUMERO"
            );
        }

        String identifier = parts[0];
        String operator = parts[1];
        String numberText = parts[2];

        if (!VALID_IDENTIFIERS.contains(identifier)) {
            throw new IllegalArgumentException(
                "Identificador no valido: " + identifier
            );
        }

        if (!VALID_OPERATORS.contains(operator)) {
            throw new IllegalArgumentException(
                "Operador no valido: " + operator
            );
        }

        double value;

        try {
            value = Double.parseDouble(numberText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Numero no valido: " + numberText
            );
        }

        return new RuleData(identifier, operator, value);
    }

    public static Rule createRule(RuleData data) {

        return switch (data.getIdentifier()) {

            case "TEMP_ALTA" ->
                new TemperatureRule(
                    data.getOperator(),
                    data.getValue()
                );

            case "LLUVIA_INTENSA" ->
                new PrecipitationRule(
                    data.getOperator(),
                    data.getValue()
                );

            case "VIENTO_FUERTE" ->
                new WindRule(
                    data.getOperator(),
                    data.getValue()
                );

            case "BATERIA_BAJA" ->
                new BatteryRule(
                    data.getOperator(),
                    data.getValue()
                );

            default ->
                throw new IllegalArgumentException(
                    "Identificador no valido: " + data.getIdentifier()
                );
        };
    }
}