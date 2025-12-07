public class Room implements Identifiable {

    private final int id;
    private String number;
    private RoomType type;
    private int capacity;
    private double pricePerNight;

    public Room(int id, String number, RoomType type, int capacity, double pricePerNight) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public RoomType getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    @Override
    public String toString() {
        return "Oda{" +
                "id=" + id +
                ", no='" + number + '\'' +
                ", tip=" + type +
                ", kapasite=" + capacity +
                ", fiyat=" + pricePerNight +
                '}';
    }
}
