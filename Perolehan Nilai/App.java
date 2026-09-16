import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class App {

    // Helper class untuk menyimpan pasangan skor dan bobot item
    private static class ScoreTracker {
        int totalBobot = 0;
        int totalSkor = 0;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int bobotPA, bobotT, bobotK, bobotP, bobotUTS, bobotUAS;

            try {
                bobotPA = Integer.parseInt(scanner.nextLine().trim());
                bobotT = Integer.parseInt(scanner.nextLine().trim());
                bobotK = Integer.parseInt(scanner.nextLine().trim());
                bobotP = Integer.parseInt(scanner.nextLine().trim());
                bobotUTS = Integer.parseInt(scanner.nextLine().trim());
                bobotUAS = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Format bobot tidak valid");
                return;
            }

            if (bobotPA + bobotT + bobotK + bobotP + bobotUTS + bobotUAS != 100) {
                System.out.println("Total bobot harus 100");
                return;
            }

            Map<String, ScoreTracker> trackerMap = new HashMap<>();
            String[] symbols = {"PA", "T", "K", "P", "UTS", "UAS"};
            for (String sym : symbols) {
                trackerMap.put(sym, new ScoreTracker());
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.equals("---")) break;

                String[] parts = line.split("\\|");
                if (parts.length != 3) {
                    System.out.println("Data tidak valid. Silahkan menggunakan format: Simbol|Bobot|Perolehan-Nilai");
                    continue;
                }

                try {
                    String simbol = parts[0].trim();
                    int bobotItem = Integer.parseInt(parts[1].trim());
                    int skorItem = Integer.parseInt(parts[2].trim());

                    if (!trackerMap.containsKey(simbol)) {
                        System.out.println("Simbol tidak dikenal");
                        continue;
                    }

                    skorItem = Math.max(0, Math.min(skorItem, bobotItem));

                    ScoreTracker tracker = trackerMap.get(simbol);
                    tracker.totalBobot += bobotItem;
                    tracker.totalSkor += skorItem;

                } catch (NumberFormatException e) {
                    System.out.println("Data tidak valid. Silahkan menggunakan format: Simbol|Bobot|Perolehan-Nilai");
                }
            }

            // Perhitungan Kontribusi
            double kPA = hitungKontribusi(trackerMap.get("PA"), bobotPA);
            double kT = hitungKontribusi(trackerMap.get("T"), bobotT);
            double kK = hitungKontribusi(trackerMap.get("K"), bobotK);
            double kP = hitungKontribusi(trackerMap.get("P"), bobotP);
            double kUTS = hitungKontribusi(trackerMap.get("UTS"), bobotUTS);
            double kUAS = hitungKontribusi(trackerMap.get("UAS"), bobotUAS);

            int pPA = hitungPersentase(trackerMap.get("PA"));
            int pT = hitungPersentase(trackerMap.get("T"));
            int pK = hitungPersentase(trackerMap.get("K"));
            int pP = hitungPersentase(trackerMap.get("P"));
            int pUTS = hitungPersentase(trackerMap.get("UTS"));
            int pUAS = hitungPersentase(trackerMap.get("UAS"));

            double nilaiAkhir = kPA + kT + kK + kP + kUTS + kUAS;
            nilaiAkhir = Math.round(nilaiAkhir * 100.0) / 100.0;

            String grade = tentukanGrade(nilaiAkhir);

            System.out.println("Perolehan Nilai:");
            cetakBarisNilai("Partisipatif", pPA, kPA, bobotPA);
            cetakBarisNilai("Tugas", pT, kT, bobotT);
            cetakBarisNilai("Kuis", pK, kK, bobotK);
            cetakBarisNilai("Proyek", pP, kP, bobotP);
            cetakBarisNilai("UTS", pUTS, kUTS, bobotUTS);
            cetakBarisNilai("UAS", pUAS, kUAS, bobotUAS);
            System.out.println();
            System.out.printf(Locale.US, ">> Nilai Akhir: %.2f\n", nilaiAkhir);
            System.out.println(">> Grade: " + grade);
        }
    }

    private static int hitungPersentase(ScoreTracker tracker) {
        if (tracker.totalBobot == 0) return 0;
        return (tracker.totalSkor * 100) / tracker.totalBobot;
    }

    private static double hitungKontribusi(ScoreTracker tracker, int bobotMaksimal) {
        int persentase = hitungPersentase(tracker);
        return (persentase / 100.0) * bobotMaksimal;
    }

    private static String tentukanGrade(double nilaiAkhir) {
        if (nilaiAkhir >= 79.5) return "A";
        if (nilaiAkhir >= 72) return "AB";
        if (nilaiAkhir >= 64.5) return "B";
        if (nilaiAkhir >= 57) return "BC";
        if (nilaiAkhir >= 49.5) return "C";
        if (nilaiAkhir >= 34) return "D";
        return "E";
    }

    private static void cetakBarisNilai(String nama, int persentase, double kontribusi, int bobotMaksimal) {
        System.out.printf(Locale.US, ">> %s: %d/100 (%.2f/%d)\n", nama, persentase, kontribusi, bobotMaksimal);
    }
}