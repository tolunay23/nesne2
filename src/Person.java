public abstract class Person implements Identifiable {

    private final int id;
    private String name;
    private String phone;

    public Person(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "ID=" + id + ", ad='" + name + "', tel='" + phone + "'";
    }
}
