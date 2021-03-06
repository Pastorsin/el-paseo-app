package laboratorio.app.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

import laboratorio.app.auth.Encryptor;

public class User implements Serializable {
    public static final int API_USER_ROLE = 2;

    private Integer id;

    private String email;
    private String encryptedPassword;

    private String firstName;
    private String lastName;
    private String name;
    private Integer age;

    private String phone;
    private Address address;
    private Address deliveryAddress;

    private UserCart userCart;

    private Integer role;

    public User(String email,
                String password,
                String firstName,
                String lastName,
                Integer age,
                String phone,
                Address address,
                Address deliveryAddress) {
        this.email = email;
        this.encryptedPassword = Encryptor.encrypt(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = String.format("%s %s", firstName, lastName);
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.deliveryAddress = deliveryAddress;
        this.role = API_USER_ROLE;
    }

    @JsonCreator
    public User(@JsonProperty("id") Integer id,
                @JsonProperty("email") String email,
                @JsonProperty("encryptedPassword") String encryptedPassword,
                @JsonProperty("firstName") String firstName,
                @JsonProperty("lastName") String lastName,
                @JsonProperty("name") String name,
                @JsonProperty("age") Integer age,
                @JsonProperty("phone") String phone,
                @JsonProperty("address") Address address,
                @JsonProperty("deliveryAddress") Address deliveryAddress,
                @JsonProperty("cart") UserCart userCart,
                @JsonProperty("role") Integer role) {
        this.id = id;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.deliveryAddress = deliveryAddress;
        this.userCart = userCart;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public UserCart getUserCart() {
        return userCart;
    }

    public void setUserCart(UserCart userCart) {
        this.userCart = userCart;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(encryptedPassword, user.encryptedPassword) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(age, user.age) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(address, user.address) &&
                Objects.equals(deliveryAddress, user.deliveryAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, encryptedPassword, firstName, lastName, name, age, phone, address, deliveryAddress, userCart, role);
    }

    @JsonIgnore
    public void setNonCredentialInformation(String firstName, String lastName, Integer age, String phone, Address residencyaddress, Address deliveryAddress) {
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setPhone(phone);
        setAddress(residencyaddress);
        setDeliveryAddress(deliveryAddress);
    }
}
