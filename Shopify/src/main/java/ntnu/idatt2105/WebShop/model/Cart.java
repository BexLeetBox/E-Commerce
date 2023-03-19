package ntnu.idatt2105.WebShop.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @OneToOne
    private User user;
    @OneToMany
    private Product product;

    public Cart(User user, Product product) {
        this.user = user;
        this.product = product;
    }
}