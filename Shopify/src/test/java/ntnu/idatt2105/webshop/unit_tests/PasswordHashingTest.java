package ntnu.idatt2105.webshop.unit_tests;

import ntnu.idatt2105.webshop.util.PasswordHashing;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordHashingTest {

    @Test
    public void testPasswordHashing() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = "password123";
        String hashedPassword = PasswordHashing.generatePasswordHash(password);

        assertTrue(PasswordHashing.validatePassword(password, hashedPassword));
        assertFalse(PasswordHashing.validatePassword("wrongpassword", hashedPassword));
    }
}