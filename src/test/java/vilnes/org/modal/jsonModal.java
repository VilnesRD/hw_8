package vilnes.org.modal;

public class jsonModal {
    public String name;
    public String id;
    public int age;
    public boolean married;
    public Address address;

    public static class Address {
        public String street;
        public String city;
        public String country;
    }
}