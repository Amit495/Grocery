package aka.studios.shribalaji.model;

public class Address {
    public int id;
    public int user_id;
    public String first_name;
    public String last_name;
    public String mobile;
    public String pincode;
    public String address;
    public String landmark;
    public String city;
    public String state;
    public String country;
    public int type;

    public Address(int id, int user_id, String first_name, String last_name, String mobile, String pincode, String address, String landmark, String city, String state, String country, int type) {
        this.id = id;
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile = mobile;
        this.pincode = pincode;
        this.address = address;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.country = country;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPincode() {
        return pincode;
    }

    public String getAddress() {
        return address;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public int getType() {
        return type;
    }
}
