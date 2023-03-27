package ntnu.idatt2105.webshop.dto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductDTOTest {

    @Test
    public void testGettersAndSetters() {
        // Create a new product DTO and set its properties
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setBriefDescription("Brief Description");
        productDTO.setFullDescription("Full Description");
        productDTO.setCategory("Category");
        productDTO.setLatitude(12.34);
        productDTO.setLongitude(56.78);
        productDTO.setPrice(9.99);
        byte[] image = new byte[] {0x01, 0x02, 0x03};
        productDTO.setImage(image);
        productDTO.setSellerName("Seller");

        // Ensure that the product DTO's properties are set correctly
        assertEquals(1L, productDTO.getId());
        assertEquals("Brief Description", productDTO.getBriefDescription());
        assertEquals("Full Description", productDTO.getFullDescription());
        assertEquals("Category", productDTO.getCategory());
        assertEquals(12.34, productDTO.getLatitude());
        assertEquals(56.78, productDTO.getLongitude());
        assertEquals(9.99, productDTO.getPrice());
        assertEquals(image, productDTO.getImage());
        assertEquals("Seller", productDTO.getSellerName());
    }
}
