package laboratorio.app.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Product implements Serializable {
    private int id;

    private String title;
    private String description;
    private String brand;

    private Double buyPrice;

    private Integer unitQuantity;
    private Integer stock;

    private Producer producer;
    private List<Category> categories;
    private List<Image> images;
    private Unit unit;

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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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

    public Integer getStockQuantity() {
        return stock / unitQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean hasStock() {
        return stock >= unitQuantity;
    }
}
