import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class HotelApp {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HotelService hotelService = DemoDataFactory.createDemoHotelService();

        System.out.println("=== OTEL REZERVASYON SISTEMI ===");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Seciminiz: ");

            switch (choice) {
                case 1 -> listAllRooms(hotelService);
                case 2 -> listAvailableRooms(hotelService);
                case 3 -> createReservation(hotelService);
                case 4 -> listReservations(hotelService);
                case 5 -> cancelReservation(hotelService);
                case 0 -> {
                    System.out.println("Cikis yapiliyor...");
                    running = false;
                }
                default -> System.out.println("Gecersiz secim!");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("1) Tum odalari listele");
        System.out.println("2) Bos odalari listele");
        System.out.println("3) Yeni rezervasyon olustur");
        System.out.println("4) Rezervasyonlari listele");
        System.out.println("5) Rezervasyon iptal et");
        System.out.println("0) Cikis");
    }

    private static void listAllRooms(HotelService service) {
        System.out.println("\n--- TUM ODALAR ---");
        for (Room room : service.getAllRooms()) {
            System.out.println(room);
        }
    }

    private static void listAvailableRooms(HotelService service) {
        System.out.println("\n--- BOS ODALAR ---");
        LocalDate checkIn = readDate("Giris tarihi (YYYY-MM-DD): ");
        LocalDate checkOut = readDate("Cikis tarihi (YYYY-MM-DD): ");

        List<Room> available = service.getAvailableRooms(checkIn, checkOut);
        if (available.isEmpty()) {
            System.out.println("Bu tarihlerde bos oda yok.");
        } else {
            available.forEach(System.out::println);
        }
    }

    private static void createReservation(HotelService service) {
        System.out.println("\n--- YENI REZERVASYON ---");
        String name = readLine("Misafir adi: ");
        String phone = readLine("Telefon: ");
        String email = readLine("E-posta: ");

        LocalDate checkIn = readDate("Giris tarihi (YYYY-MM-DD): ");
        LocalDate checkOut = readDate("Cikis tarihi (YYYY-MM-DD): ");

        List<Room> available = service.getAvailableRooms(checkIn, checkOut);
        if (available.isEmpty()) {
            System.out.println("Bu tarihlerde bos oda yok.");
            return;
        }

        System.out.println("Musait odalar:");
        available.forEach(System.out::println);

        int roomId = readInt("Secmek istediginiz oda ID: ");

        try {
            Reservation reservation = service.createReservation(name, phone, email, roomId, checkIn, checkOut);
            System.out.println("Rezervasyon olusturuldu: " + reservation);
        } catch (Exception ex) {
            System.out.println("Hata: " + ex.getMessage());
        }
    }

    private static void listReservations(HotelService service) {
        System.out.println("\n--- REZERVASYON LISTESI ---");
        List<Reservation> reservations = service.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("Henuz rezervasyon yok.");
        } else {
            reservations.forEach(System.out::println);
        }
    }

    private static void cancelReservation(HotelService service) {
        System.out.println("\n--- REZERVASYON IPTALI ---");
        int id = readInt("Iptal etmek istediginiz rezervasyon ID: ");
        boolean result = service.cancelReservation(id);
        if (result) System.out.println("Rezervasyon iptal edildi.");
        else System.out.println("Bu ID'ye ait rezervasyon bulunamadi.");
    }

    private static int readInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Sayisal deger gir.");
            }
        }
    }

    private static LocalDate readDate(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Hatali tarih!");
            }
        }
    }

    private static String readLine(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }
}
