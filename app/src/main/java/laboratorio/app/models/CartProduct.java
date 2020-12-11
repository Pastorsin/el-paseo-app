package laboratorio.app.models;

public class CartProduct {
    private Cart cart;
    private int id;
    private boolean isCanceled;
    private Double price;
    private Product product;
    private int quantity;

    public CartProduct(Cart cart, int id, Product product, int quantity){
        this.cart = cart;
        this.id = id;
        this.isCanceled = false;
        this.product = product;
        this.price = product.getPrice();
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

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

}
