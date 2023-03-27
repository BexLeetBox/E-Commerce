package ntnu.idatt2105.webshop.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDTOTest {

    @Test
    public void testGettersAndSetters() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPhoneNumber("12345678");
        userDTO.setAddress("123 Main St");
        userDTO.setUsername("johndoe");

        assertEquals(1L, userDTO.getId());
        assertEquals("John", userDTO.getFirstName());
        assertEquals("Doe", userDTO.getLastName());
        assertEquals("johndoe@example.com", userDTO.getEmail());
        assertEquals("12345678", userDTO.getPhoneNumber());
        assertEquals("123 Main St", userDTO.getAddress());
        assertEquals("johndoe", userDTO.getUsername());
    }

    @Test
    public void testNewPassword() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNewPassword("password");
        assertEquals("password", userDTO.getNewPassword());
    }

    @Test
    public void testOldPassword() {
        UserDTO userDTO = new UserDTO();
        userDTO.setOldPassword("oldpassword");
        assertEquals("oldpassword", userDTO.getOldPassword());
    }
}