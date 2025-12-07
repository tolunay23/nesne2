import java.util.List;

public class DemoDataFactory {

    public static HotelService createDemoHotelService() {

        // id, odaNo, tip, kapasite, fiyat
        List<Room> rooms = List.of(
                new Room(1, "101", RoomType.SINGLE, 1, 1000),
                new Room(2, "102", RoomType.DOUBLE, 2, 1800),
                new Room(3, "201", RoomType.SUITE, 4, 3000),
                new Room(4, "202", RoomType.DOUBLE, 2, 1900)
        );

        Repository<Room> roomRepo = new InMemoryRepository<>(rooms);
        Repository<Guest> guestRepo = new InMemoryRepository<>();
        Repository<Reservation> reservationRepo = new InMemoryRepository<>();

        return new HotelService(roomRepo, guestRepo, reservationRepo);
    }
}
