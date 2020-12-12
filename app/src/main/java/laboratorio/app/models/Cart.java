package laboratorio.app.models;

import java.util.ArrayList;
import java.util.List;

public class Cart{
    public static final Cart instance = new Cart();
    //aprovedAt DateTime
    private final List<CartProduct> cartProducts= new ArrayList<CartProduct>();
    //deletedAt DateTime
    //delivered DateTime
    //general General
    //public int id;
    //nodeDate AvailableNode
    //posibleDeliveryDate DateTime
    private String saleDate;
    private Double total;
    //user User

    private Cart(){}

    public void addProduct(CartProduct cartProduct){
        cartProducts.add(cartProduct);
    }

    public void addProduct(Product product, Integer quantity) {
        CartProduct cartProduct = new CartProduct(this, product,quantity);
        cartProducts.add(cartProduct);
    }

    public void removeProduct(CartProduct product){
        cartProducts.remove(product);
    }

    public Double getTotal(){
        total = 0.0;
        for (CartProduct p : cartProducts){
            total = total + p.getTotalCartProductPrice() ;
        }
        return total;
    }

    public boolean isEmpty(){
        return cartProducts.isEmpty();
    }

    public List<CartProduct> getCartProducts(){ return cartProducts; }

    public boolean contains(CartProduct cartProduct) {
        return cartProducts.contains(cartProduct);
    }
}
