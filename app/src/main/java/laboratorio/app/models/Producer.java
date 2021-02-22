package laboratorio.app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Producer implements Serializable {
    private String name;
    private String lastName;
    private String origin;

    private String email;
    private String phone;
    private String youtubeVideoId;

    private boolean isCompany;

    private Address address;
    private List<Image> images;
    private List<Product> products;
    //private List<Tag> tags;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public boolean hasMainImage() {
        Image mainImage = getMainImage();
        return mainImage != null && mainImage.getValue() != null;
    }

    public Image getMainImage() {
        List<Image> mainImages = getMainImages();

        if (!mainImages.isEmpty())
            return mainImages.get(0);

        if (images == null)
            return null;

        if (images.isEmpty())
            return null;

        return images.get(0);
    }

    public List<Image> getMainImages() {
        if (images == null)
            return new ArrayList<>();

        return images.stream().filter(Image::isMain).collect(Collectors.toList());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public boolean hasVideo() {
        return (youtubeVideoId != null);
    }

    public String getYouTubeVideoId() {
        return this.youtubeVideoId;
    }

    public boolean hasEmail() {
        return (email != null);
    }

    public boolean hasPhoneNumber() {
        return (phone != null);
    }

    public boolean hasAddress() {
        return (address != null && address.getStreet() != null);
    }

}
