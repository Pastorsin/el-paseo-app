package laboratorio.app.models;

import java.util.List;
import java.util.stream.Collectors;

public class Product {
    private int id;

    private String title;
    private String description;
    private String brand;

    private Double buyPrice;
    private Double price;

    private Integer unitQuantity;
    private Integer stock;

    private Producer producer;
    private List<Category> categories;
    private List<Image> images;

    private boolean isPromotion;
    private boolean needCold;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(Integer unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public void setPromotion(boolean promotion) {
        isPromotion = promotion;
    }

    public boolean isNeedCold() {
        return needCold;
    }

    public void setNeedCold(boolean needCold) {
        this.needCold = needCold;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public boolean hasCategory(Category category) {
        return categories.contains(category);
    }

    public boolean hasMainImage() {
        Image mainImage = getMainImage();
        return mainImage != null && mainImage.getValue() != null;
    }

    public Image getMainImage() {
        List<Image> mainImages = getMainImages();

        if (!mainImages.isEmpty())
            return mainImages.get(0);

        if (!images.isEmpty())
            return images.get(0);

        return null;
    }

    public List<Image> getMainImages() {
        return images.stream().filter(Image::isMain).collect(Collectors.toList());
    }
}
