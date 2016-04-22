package time_day;

/**
 * Created by JTdavy on 2016/2/23.
 */


public class PersonInfo {
    private int id;
    private String name;
    private String phone;
    private String address;
    private int money;


    public PersonInfo(int id, String name, String phone, String address,
                      int money) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.money = money;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", phone=" + phone
                + ", address=" + address + ", money=" + money ;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }


}
