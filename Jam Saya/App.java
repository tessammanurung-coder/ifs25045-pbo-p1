import java.util.Scanner;

public class App {
    
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            if (!sc.hasNextLine()) return;
            
            // 1. Membaca dan Memvalidasi Jam Awal
            String timeStr = sc.nextLine().trim();
            if (!isValidTimeFormat(timeStr)) {
                System.out.println("Jam tidak valid");
                return;
            }
            
            String[] parts = timeStr.split(":");
            int startH = Integer.parseInt(parts[0]);
            int startM = Integer.parseInt(parts[1]);
            
            if (!isValidTimeValues(startH, startM)) {
                System.out.println("Jam tidak valid");
                return;
            }
            
            // 2. Mengkonversi jam awal ke total menit dari 00:00
            int baseMin = convertToMinutes(startH, startM);
            
            // 3. Memproses semua perintah pergeseran waktu
            Integer deltaMin = processTimeShifts(sc);
            if (deltaMin == null) {
                System.out.println("Perintah tidak valid");
                return;
            }
            
            // 4. Menghitung dan mencetak hasil akhir
            calculateAndPrintResult(startH, startM, baseMin, deltaMin);
        }
    }

    private static boolean isValidTimeFormat(String timeStr) {
        return timeStr.matches("\\d+:\\d+");
    }

    private static boolean isValidTimeValues(int hours, int minutes) {
        return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
    }

    private static int convertToMinutes(int hours, int minutes) {
        return (hours * 60) + minutes;
    }

    private static Integer processTimeShifts(Scanner sc) {
        int deltaMin = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("---")) break;
            
            if (line.matches("[+-]\\d+")) {
                deltaMin += Integer.parseInt(line);
            } else {
                return null; // Mengembalikan null jika ada perintah invalid
            }
        }
        return deltaMin;
    }

    private static void calculateAndPrintResult(int startH, int startM, int baseMin, int deltaMin) {
        int finalTotalMin = baseMin + deltaMin;
        
        // Kalkulasi pergantian hari yang akurat (baik pergeseran maju maupun mundur)
        int days = Math.abs(Math.floorDiv(finalTotalMin, 1440));
        if (finalTotalMin < 0 && finalTotalMin % 1440 == 0) {
            // Penyesuaian jika tepat pada titik pergantian hari negatif
            days = Math.abs(finalTotalMin / 1440);
        }

        // Modulo 1440 agar sisa menit selalu berada di rentang [0, 1439]
        int finalModMin = Math.floorMod(finalTotalMin, 1440);
        
        int finalH = finalModMin / 60;
        int finalM = finalModMin % 60;
        
        String formattedDelta = (deltaMin == 0) ? "0" : String.format("%+d", deltaMin);
        
        System.out.printf("Jam Awal: %02d:%02d\n", startH, startM);
        System.out.printf("Jam Akhir: %02d:%02d\n", finalH, finalM);
        System.out.println("Total Menit: " + formattedDelta);
        System.out.println("Pergantian Hari: " + days);
    }
}