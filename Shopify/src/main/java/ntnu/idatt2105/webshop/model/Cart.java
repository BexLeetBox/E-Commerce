package ntnu.idatt2105.webshop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity(name = "Cart")
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;


    @OneToMany(mappedBy = "cart")
    private List<Product> products;

    public Cart(User user, List<Product> product) {
        this.user = user;
        this.products = product;
    }

    public Cart() {

    }

    public void addProduct(Product product){
        this.products.add(product);
    }

    public void removeProduct(Product product){
        this.products.remove(product);
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}