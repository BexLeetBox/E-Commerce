package ntnu.idatt2105.webshop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Cart")
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToMany(mappedBy = "carts")
    private Set<Product> products = new HashSet<>();




    public Cart(User user, Set<Product> products) {
        this.user = user;
        this.products = products;
    }
    public Cart(User user) {
        this.user = user;
    }

    public Cart() {

    }

    public void addProduct(Product product){
        this.products.add(product);
        product.getCarts().add(this);
    }

    public void removeProduct(Product product){
        this.products.remove(product);
        product.getCarts().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}