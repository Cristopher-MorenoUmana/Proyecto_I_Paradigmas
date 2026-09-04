package Java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesEngine {

    private static final String METRICS_FILE = "../csv/metricas.csv";
    private static final String RULES_FILE = "rules.txt";
    private static final String ALERTS_FILE = "../csv/alertas.csv";

    public static void main(String[] args) {

        try {
            Map<String, Double> metrics = readMetrics();

            List<Rule> rules = readRules();

            evaluateRules(metrics, rules);

        } catch (IOException | IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Map<String, Double> readMetrics() throws IOException {

        Map<String, Double> metrics = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(METRICS_FILE))) {

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length != 2) {
                    throw new IllegalArgumentException(
                        "Formato invalido en metricas.csv"
                    );
                }

                String name = parts[0].trim();
                double value = Double.parseDouble(parts[1].trim());

                metrics.put(name, value);
            }
        }

        return metrics;
    }

    private static List<Rule> readRules() throws IOException {

        List<Rule> rules = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(RULES_FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                RuleData data = RuleParser.parse(line);

                Rule rule = RuleParser.createRule(data);

                rules.add(rule);

                System.out.println(
                    "Regla creada: " + rule.getRuleName()
                );
            }
        }

        return rules;
    }

    private static void evaluateRules(
            Map<String, Double> metrics,
            List<Rule> rules) throws IOException {

        System.out.println();
        System.out.println("EVALUACION DE REGLAS");

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(ALERTS_FILE))) {

            writer.write("REGLA,RESULTADO");
            writer.newLine();

            for (Rule rule : rules) {

                String metricName = rule.getMetricName();

                Double metric = metrics.get(metricName);

                if (metric == null) {

                    throw new IllegalArgumentException(
                        "Metrica no encontrada: " + metricName
                    );
                }

                boolean result = rule.evaluate(metric);

                System.out.println(
                    metricName + " = " + metric + " -> " + result
                );

                int resultValue = result ? 1 : 0;

                writer.write(
                    rule.getRuleName() + "," + resultValue
                );

                writer.newLine();
            }
        }

        System.out.println();
        System.out.println("Archivo generado: " + ALERTS_FILE);
    }
}