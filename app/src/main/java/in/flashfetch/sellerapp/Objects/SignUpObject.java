package in.flashfetch.sellerapp.Objects;

/**
 * Created by kranthikumar_b on 7/7/2016.
 */
public class SignUpObject {

    private String name, email, password, phone, shopName, shopId, shopTelephone, shopAddress1, shopAddress2, shopLocation;
    private boolean isDummyObject;

    public SignUpObject(String name, String email, String password, String phone, String shopName, String shopId, String shopTelephone, String shopAddress1, String shopAddress2, String shopLocation, boolean isDummyObject){
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.shopName = shopName;
        this.shopId = shopId;
        this.shopTelephone = shopTelephone;
        this.shopAddress1 = shopAddress1;
        this.shopAddress2 = shopAddress2;
        this.shopLocation = shopLocation;
        this.isDummyObject = isDummyObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopTelephone() {
        return shopTelephone;
    }

    public void setShopTelephone(String shopTelephone) {
        this.shopTelephone = shopTelephone;
    }

    public String getShopAddress1() {
        return shopAddress1;
    }

    public void setShopAddress1(String shopAddress1) {
        this.shopAddress1 = shopAddress1;
    }

    public String getShopAddress2() {
        return shopAddress2;
    }

    public void setShopAddress2(String shopAddress2) {
        this.shopAddress2 = shopAddress2;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public boolean isDummyObject() {
        return isDummyObject;
    }

    public void setDummyObject(boolean dummyObject) {
        isDummyObject = dummyObject;
    }
}
