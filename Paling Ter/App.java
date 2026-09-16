import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class App {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // Menggunakan TreeMap agar key terurut secara alami untuk konsistensi tie-breaker
            Map<Long, Long> counts = new TreeMap<>();
            boolean hasData = readTokens(sc, counts);

            if (!hasData) return;

            analyzeAndPrintStatistics(counts);
        }
    }

    private static boolean readTokens(Scanner sc, Map<Long, Long> counts) {
        boolean hasData = false;
        while (sc.hasNext()) {
            String token = sc.next();
            if (token.equals("---")) break;
            
            try {
                long val = Long.parseLong(token);
                counts.put(val, counts.getOrDefault(val, 0L) + 1);
                hasData = true;
            } catch (NumberFormatException e) {
                // Mengabaikan input yang bukan angka
            }
        }
        return hasData;
    }

    private static void analyzeAndPrintStatistics(Map<Long, Long> counts) {
        long maxVal = Long.MIN_VALUE, minVal = Long.MAX_VALUE;
        long modeVal = 0, modeFreq = -1;
        long antiModeVal = 0, antiModeFreq = Long.MAX_VALUE;
        long maxProdVal = 0, maxProdSum = Long.MIN_VALUE;
        long minProdVal = 0, minProdSum = Long.MAX_VALUE;

        for (Map.Entry<Long, Long> entry : counts.entrySet()) {
            long val = entry.getKey();
            long freq = entry.getValue();
            
            // Mencegah/Menangani overflow pada perkalian nilai x frekuensi
            long prod;
            try {
                prod = Math.multiplyExact(val, freq);
            } catch (ArithmeticException e) {
                prod = (val > 0) ? Long.MAX_VALUE : Long.MIN_VALUE;
            }

            if (val > maxVal) maxVal = val;
            if (val < minVal) minVal = val;

            // Tie-Breaker untuk Terbanyak
            if (freq > modeFreq || (freq == modeFreq && val > modeVal)) {
                modeFreq = freq;
                modeVal = val;
            }
            // Tie-Breaker untuk Tersedikit
            if (freq < antiModeFreq || (freq == antiModeFreq && val < antiModeVal)) {
                antiModeFreq = freq;
                antiModeVal = val;
            }

            // Tie-Breaker untuk Jumlah Tertinggi
            if (prod > maxProdSum || (prod == maxProdSum && val > maxProdVal)) {
                maxProdSum = prod;
                maxProdVal = val;
            }
            // Tie-Breaker untuk Jumlah Terendah
            if (prod < minProdSum || (prod == minProdSum && val < minProdVal)) {
                minProdSum = prod;
                minProdVal = val;
            }
        }

        System.out.println("Tertinggi: " + maxVal);
        System.out.println("Terendah: " + minVal);
        System.out.println("Terbanyak: " + modeVal + " (" + modeFreq + "x)");
        System.out.println("Tersedikit: " + antiModeVal + " (" + antiModeFreq + "x)");
        System.out.println("Jumlah Tertinggi: " + maxProdVal + " * " + counts.get(maxProdVal) + " = " + maxProdSum);
        System.out.println("Jumlah Terendah: " + minProdVal + " * " + counts.get(minProdVal) + " = " + minProdSum);
    }
}