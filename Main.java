import java.util.*;

public class Main {

    private static long decodeValue(String base, String value) {
        int radix = Integer.parseInt(base);
        return Long.parseLong(value, radix);
    }

    private static long lagrangeInterpolation(List<long[]> points, long x) {
        int k = points.size();
        double constant = 0;

        for (int i = 0; i < k; i++) {
            long xi = points.get(i)[0];
            long yi = points.get(i)[1];

            double term = yi;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    long xj = points.get(j)[0];
                    term *= (double) (x - xj) / (xi - xj);
                }
            }
            constant += term;
        }
        return Math.round(constant);
    }

    public static long findConstant(String input) {
        try {
            Map<String, Object> data = parseJson(input);

            Map<String, Object> keys = (Map<String, Object>) data.get("keys");
            int n = (int) ((long) keys.get("n"));
            int k = (int) ((long) keys.get("k"));

            List<long[]> points = new ArrayList<>();

            for (int i = 1; i <= k; i++) {
                Map<String, String> root = (Map<String, String>) data.get(String.valueOf(i));
                String base = root.get("base");
                String value = root.get("value");

                points.add(new long[]{i, decodeValue(base, value)});
            }

            return lagrangeInterpolation(points, 0);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static Map<String, Object> parseJson(String input) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Stack<Object> stack = new Stack<>();
        stack.push(map);

        String key = null;
        StringBuilder value = new StringBuilder();
        boolean inString = false;

        for (char c : input.toCharArray()) {
            if (c == '"') {
                inString = !inString;
                if (!inString && key == null) key = value.toString();
                value.setLength(0);
            } else if (inString) {
                value.append(c);
            } else if (c == '{') {
                Map<String, Object> newMap = new HashMap<>();
                ((Map<String, Object>) stack.peek()).put(key, newMap);
                stack.push(newMap);
                key = null;
            } else if (c == '}') {
                stack.pop();
            } else if (c == ':') {
            } else if (c == ',' || c == ']') {
                if (value.length() != 0) { // Use length check instead of isEmpty()
                    ((Map<String, Object>) stack.peek()).put(key, value.toString());
                    value.setLength(0);
                }
                key = null;
            } else if (Character.isDigit(c)) {
                value.append(c);
            }
        }

        return map;
    }

    public static void main(String[] args) {
        String testcase1 = "{\n" +
                "    \"keys\": {\n" +
                "        \"n\": 4,\n" +
                "        \"k\": 3\n" +
                "    },\n" +
                "    \"1\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"4\"\n" +
                "    },\n" +
                "    \"2\": {\n" +
                "        \"base\": \"2\",\n" +
                "        \"value\": \"111\"\n" +
                "    },\n" +
                "    \"3\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"12\"\n" +
                "    }\n" +
                "}";

        String testcase2 = "{\n" +
                "    \"keys\": {\n" +
                "        \"n\": 10,\n" +
                "        \"k\": 7\n" +
                "    },\n" +
                "    \"1\": {\n" +
                "        \"base\": \"6\",\n" +
                "        \"value\": \"13444211440455345511\"\n" +
                "    },\n" +
                "    \"2\": {\n" +
                "        \"base\": \"15\",\n" +
                "        \"value\": \"aed7015a346d63\"\n" +
                "    },\n" +
                "    \"3\": {\n" +
                "        \"base\": \"15\",\n" +
                "        \"value\": \"6aeeb69631c227c\"\n" +
                "    },\n" +
                "    \"4\": {\n" +
                "        \"base\": \"16\",\n" +
                "        \"value\": \"e1b5e05623d881f\"\n" +
                "    },\n" +
                "    \"5\": {\n" +
                "        \"base\": \"8\",\n" +
                "        \"value\": \"316034514573652620673\"\n" +
                "    },\n" +
                "    \"6\": {\n" +
                "        \"base\": \"3\",\n" +
                "        \"value\": \"2122212201122002221120200210011020220200\"\n" +
                "    },\n" +
                "    \"7\": {\n" +
                "        \"base\": \"3\",\n" +
                "        \"value\": \"20120221122211000100210021102001201112121\"\n" +
                "    }\n" +
                "}";

        long c1 = findConstant(testcase1);
        long c2 = findConstant(testcase2);

        System.out.println("Constant for Testcase 1: " + c1);
        System.out.println("Constant for Testcase 2: " + c2);
    }
}
