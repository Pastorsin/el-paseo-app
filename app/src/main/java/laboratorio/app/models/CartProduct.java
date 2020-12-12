package laboratorio.app.models;

import java.util.Objects;

public class CartProduct {
    private Cart cart;
    private boolean isCanceled;
    private Double price;
    private Product product;
    private Integer quantity;

    public CartProduct(Cart cart, Product product, int quantity){
        this.cart = cart;
        this.isCanceled = false;
        this.product = product;
        this.price = product.getBuyPrice();
        this.quantity = quantity;
    }

    public boolean isCanceled(){
        return isCanceled;
    }

    public void changeCanceledState(){
        this.isCanceled = !this.isCanceled;
    }

    public Number getPrice() {
        return price;
    }

    public Double getTotalCartProductPrice(){
        return (quantity * price);
    }

    public Product getProduct(){
        return product;
    }

    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean decrementQuantity() {
        if (quantity <= 1)
            return false;

        quantity--;

        return true;
    }

    public boolean incrementQuantity() {

        if ((quantity + 1) > product.getStockQuantity())
            return false;

        quantity++;

        return true;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProduct that = (CartProduct) o;
        return product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}
