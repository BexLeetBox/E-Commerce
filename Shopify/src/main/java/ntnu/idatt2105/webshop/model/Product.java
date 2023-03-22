package ntnu.idatt2105.webshop.model;

import javax.persistence.*;

@Entity(name = "Product")
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String briefDescription;
    private String fullDescription;
    private String category;
    private double latitude, longitude;
    private double price;

    @Lob
    @Column(name = "image", columnDefinition="BLOB")
    private byte[] image;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public Product(String briefDescription, String fullDescription, String category, double latitude, double longitude,
                   double price, byte[] image, User user) {
        this.briefDescription = briefDescription;
        this.fullDescription = fullDescription;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.image = image;
        this.seller = user;
    }

    public Product() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }
}