package laboratorio.app.models;

public class Product {
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
}
