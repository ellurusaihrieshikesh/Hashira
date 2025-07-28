import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.util.*;

class Point implements Comparable<Point> {
    int x;
    BigInteger y;
    Point(int x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public int compareTo(Point o) {
        return Integer.compare(this.x, o.x);
    }
}

public class ShamirSecret {
    public static void main(String[] args) throws Exception {
        String[] files = {"data1.json", "data2.json"};
        for (String file : files) {
            BigInteger secret = solve(file);
            System.out.println(secret);
        }
    }

    static BigInteger solve(String filename) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(content);

        JSONObject keysObj = obj.getJSONObject("keys");
        int n = keysObj.getInt("n");
        int k = keysObj.getInt("k");

        List<Point> points = new ArrayList<>();
        for (String key : obj.keySet()) {
            if (key.equals("keys")) continue;
            int x = Integer.parseInt(key);
            JSONObject point = obj.getJSONObject(key);
            String baseStr = point.getString("base");
            String valueStr = point.getString("value");
            int base = Integer.parseInt(baseStr);
            BigInteger y = new BigInteger(valueStr, base);
            points.add(new Point(x, y));
        }

        Collections.sort(points);
        List<Point> selectedPoints = points.subList(0, k);
        return interpolate(selectedPoints, k);
    }

    static BigInteger interpolate(List<Point> points, int k) {
        BigInteger[] numerators = new BigInteger[k];
        BigInteger[] denoms = new BigInteger[k];

        for (int i = 0; i < k; i++) {
            int x_i = points.get(i).x;
            BigInteger y_i = points.get(i).y;

            numerators[i] = y_i;
            denoms[i] = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                int x_j = points.get(j).x;
                numerators[i] = numerators[i].multiply(BigInteger.valueOf(-x_j));
                BigInteger diff = BigInteger.valueOf(x_i).subtract(BigInteger.valueOf(x_j));
                denoms[i] = denoms[i].multiply(diff);
            }
        }

        BigInteger common_denom = BigInteger.ONE;
        for (BigInteger d : denoms) {
            common_denom = common_denom.multiply(d);
        }

        BigInteger total_numerator = BigInteger.ZERO;
        for (int i = 0; i < k; i++) {
            BigInteger factor = common_denom.divide(denoms[i]);
            total_numerator = total_numerator.add(numerators[i].multiply(factor));
        }

        BigInteger[] divisionResult = total_numerator.divideAndRemainder(common_denom);
        if (!divisionResult[1].equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Division has a remainder: " + divisionResult[1]);
        }
        return divisionResult[0];
    }
}