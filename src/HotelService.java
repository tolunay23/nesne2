import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class HotelService {

    private final Repository<Room> roomRepository;
    private final Repository<Guest> guestRepository;
    private final Repository<Reservation> reservationRepository;

    private final Map<Integer, Room> roomCache = new HashMap<>();

    private int nextGuestId = 1;
    private int nextReservationId = 1;

    public HotelService(Repository<Room> roomRepository,
                        Repository<Guest> guestRepository,
                        Repository<Reservation> reservationRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;

        roomRepository.findAll().forEach(r -> roomCache.put(r.getId(), r));
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> reservations = reservationRepository.findAll();

        Set<Integer> occupiedIds = reservations.stream()
                .filter(r -> datesOverlap(checkIn, checkOut,
                        r.getCheckIn(), r.getCheckOut()))
                .map(r -> r.getRoom().getId())
                .collect(Collectors.toSet());

        return roomRepository.findAll().stream()
                .filter(room -> !occupiedIds.contains(room.getId()))
                .collect(Collectors.toList());
    }

    public Reservation createReservation(String name, String phone, String email,
                                         int roomId, LocalDate checkIn, LocalDate checkOut) {

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Çıkış tarihi giriş tarihinden sonra olmalı.");
        }

        Room room = roomCache.get(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Geçersiz oda ID.");
        }

        boolean occupied = reservationRepository.findAll().stream()
                .anyMatch(r -> r.getRoom().getId() == roomId &&
                        datesOverlap(checkIn, checkOut, r.getCheckIn(), r.getCheckOut()));

        if (occupied) {
            throw new IllegalArgumentException("Bu oda seçilen tarihlerde dolu.");
        }

        Guest guest = new Guest(nextGuestId++, name, phone, email);
        guestRepository.save(guest);

        Reservation res = new Reservation(
                nextReservationId++, guest, room, checkIn, checkOut);
        reservationRepository.save(res);

        return res;
    }

    public boolean cancelReservation(int id) {
        return reservationRepository.deleteById(id);
    }

    private boolean datesOverlap(LocalDate cIn, LocalDate cOut,
                                 LocalDate rIn, LocalDate rOut) {
        // [cIn,cOut) ile [rIn,rOut) kesişiyor mu?
        return !(cOut.compareTo(rIn) <= 0 || cIn.compareTo(rOut) >= 0);
    }
}
