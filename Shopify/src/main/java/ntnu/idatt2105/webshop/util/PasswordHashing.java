package ntnu.idatt2105.webshop.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Utility class for generating and validating password hashes using PBKDF2 with SHA-1.
 */
public final class PasswordHashing {

    private PasswordHashing() {
        // Prevent instantiation of utility class
    }

    /**
     * Generates a password hash for the given password using PBKDF2 with SHA-1.
     *
     * @param password the password to hash
     * @return the hashed password in the format "iterations:salt:hash"
     * @throws NoSuchAlgorithmException if the algorithm is not available
     * @throws InvalidKeySpecException  if the key specification is invalid
     */
    public static String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    /**
     * Validates the given password against a stored password hash using PBKDF2 with SHA-1.
     *
     * @param originalPassword the original password to check
     * @param storedPassword   the stored password hash in the format "iterations:salt:hash"
     * @return true if the original password matches the stored password hash, false otherwise
     * @throws NoSuchAlgorithmException if the algorithm is not available
     * @throws InvalidKeySpecException  if the key specification is invalid
     */
    public static boolean validatePassword(String originalPassword, String storedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    /**
     * Generates a random salt byte array to be used in password hashing.
     *
     * @return the randomly generated salt byte array
     * @throws NoSuchAlgorithmException if the specified cryptographic algorithm is not available
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }


    /**
     * Converts a byte array to its hexadecimal representation.
     *
     * @param array the byte array to convert
     * @return the hexadecimal string representation of the byte array
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * Converts a hexadecimal string representation to a byte array.
     *
     * @param hex the hexadecimal string to convert
     * @return the byte array representation of the hexadecimal string
     */
    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

}
