package laboratorio.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Cart implements Serializable {
    private Integer id;
    private Set<CartProduct> cartProducts = new LinkedHashSet<>();
    private AvailableNode nodeDate;
    private String observation;
    private User user;
    private Date saleDate;
    private Date posibleDeliveryDate;

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

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = new HashSet<>(cartProducts);
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

    public void remove(List<CartProduct> listOfCartProducts) {
        cartProducts.removeAll(listOfCartProducts);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AvailableNode getNodeDate() {
        return nodeDate;
    }

    public void setNodeDate(AvailableNode nodeDate) {
        this.nodeDate = nodeDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public void reset() {
        cartProducts.clear();
        nodeDate = null;
        observation = null;
        user = null;
        saleDate = null;
    }

    public Date getPosibleDeliveryDate() {
        return posibleDeliveryDate;
    }

    public void setPosibleDeliveryDate(Date posibleDeliveryDate) {
        this.posibleDeliveryDate = posibleDeliveryDate;
    }

    @JsonIgnore
    public boolean isDeliverable() {
        return getNodeDate().getId() == null;
    }

    @JsonIgnore
    public boolean hasPosibleDeliveryDate() {
        return getPosibleDeliveryDate() != null;
    }
}
