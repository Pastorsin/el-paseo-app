package laboratorio.app.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {
    private String apartament;
    private String between_streets;
    private String description;
    private String floor;
    private Double latitude;
    private Double longitude;
    private String number;
    private String street;

    @JsonCreator
    public Address(@JsonProperty("apartament") String apartament,
                   @JsonProperty("number") String number,
                   @JsonProperty("between_streets") String between_streets,
                   @JsonProperty("description") String description,
                   @JsonProperty("floor") String floor,
                   @JsonProperty("latitude") Double latitude,
                   @JsonProperty("longitude") Double longitude,
                   @JsonProperty("street") String street) {
        this.apartament = apartament;
        this.number = number;
        this.between_streets = between_streets;
        this.description = description;
        this.floor = floor;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
    }

    public String fullAddress() {
        return String.format("%s Nº %s %s",
                street,
                number,
                floor == null ? "" : floor
        );
    }

    public String getApartament() {
        return apartament;
    }

    public void setApartament(String apartament) {
        this.apartament = apartament;
    }

    public String getBetweenStreets() {
        return between_streets;
    }

    public void setBetween_streets(String between_streets) {
        this.between_streets = between_streets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(apartament, address.apartament) &&
                Objects.equals(between_streets, address.between_streets) &&
                Objects.equals(description, address.description) &&
                Objects.equals(floor, address.floor) &&
                Objects.equals(latitude, address.latitude) &&
                Objects.equals(longitude, address.longitude) &&
                Objects.equals(number, address.number) &&
                Objects.equals(street, address.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apartament, between_streets, description, floor, latitude, longitude, number, street);
    }
}
