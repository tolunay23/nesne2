public class Staff extends Person {

    private String position;

    public Staff(int id, String name, String phone, String position) {
        super(id, name, phone);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Personel{" + super.toString() + ", pozisyon='" + position + "'}";
    }
}
