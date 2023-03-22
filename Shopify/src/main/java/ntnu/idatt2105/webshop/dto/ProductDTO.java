package ntnu.idatt2105.webshop.dto;

    public class ProductDTO {
        private Long id;
        private String briefDescription;
        private String fullDescription;
        private String category;
        private double latitude;
        private double longitude;
        private double price;
        private byte[] image;
        private String sellerName;

        public ProductDTO() {
        }

        public ProductDTO(Long id, String briefDescription, String fullDescription, String category, double latitude,
                          double longitude, double price, byte[] image, String sellerName) {
            this.id = id;
            this.briefDescription = briefDescription;
            this.fullDescription = fullDescription;
            this.category = category;
            this.latitude = latitude;
            this.longitude = longitude;
            this.price = price;
            this.image = image;
            this.sellerName = sellerName;
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

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }
    }

