package ntnu.idatt2105.WebShop.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String briefDescription;
    private String fullDescription;
    private String category;
    private double latitude, longitude;
    private double price;
    private String imagePath;
    @ManyToOne
    private User seller;



    public Product(String briefDescription, String fullDescription, String category, double latitude, double longitude, double price, String imagePath) {
        this.briefDescription = briefDescription;
        this.fullDescription = fullDescription;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.imagePath = imagePath;
    }

}