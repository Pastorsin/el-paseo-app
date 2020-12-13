package laboratorio.app.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Cart{
    public static final Cart instance = new Cart();
    //aprovedAt DateTime
    private final Set<CartProduct> cartProducts= new LinkedHashSet<CartProduct>();
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

    public void removeProduct(CartProduct product){
        cartProducts.remove(product);
    }

    public Double getTotal(){
        return cartProducts.stream()
                .mapToDouble(CartProduct::getTotalCartProductPrice)
                .sum();
    }

    public boolean isEmpty(){
        return cartProducts.isEmpty();
    }

    public List<CartProduct> getCartProducts(){
        return new ArrayList<>(cartProducts);
    }

    public boolean contains(CartProduct cartProduct) {
        return cartProducts.contains(cartProduct);
    }

    public boolean containsProduct(Product product) {
        return cartProducts.stream().anyMatch(cp -> cp.getProduct().equals(product));
    }

    public CartProduct getCartProductOf(Product product) {
        return cartProducts.stream()
                .filter(cp -> cp.getProduct().equals(product))
                .findFirst()
                .orElseGet(null);
    }

    public CartProduct getCartProductOrCreate(Product product) {
        if (containsProduct(product))
            return getCartProductOf(product);

        return new CartProduct(this, product, 1);
    }


}
