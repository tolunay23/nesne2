import java.time.LocalDate;

public class Reservation implements Identifiable {

    private final int id;
    private final Guest guest;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    public Reservation(int id, Guest guest, Room room,
                       LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    @Override
    public int getId() {
        return id;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    @Override
    public String toString() {
        return "Rez{" +
                "id=" + id +
                ", oda=" + room.getNumber() +
                ", tip=" + room.getType() +
                ", misafir=" + guest +
                ", giris=" + checkIn +
                ", cikis=" + checkOut +
                '}';
    }
}
