public class Guest extends Person {

    private String email;

    public Guest(int id, String name, String phone, String email) {
        super(id, name, phone);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Misafir{" + super.toString() + ", email='" + email + "'}";
    }
}
