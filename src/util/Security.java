package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Clase para manejar la seguridad de contraseñas/PINs
 * Usa SHA-256 con salt para hash de PINs
 */
public class SecurityUtil {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Genera un salt aleatorio
     */
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hash del PIN con salt
     * @param pin PIN en texto plano
     * @return PIN hasheado en formato "salt:hash"
     */
    public static String hashPin(String pin) {
        try {
            String salt = generateSalt();
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(Base64.getDecoder().decode(salt));
            
            byte[] hashedPin = md.digest(pin.getBytes());
            String hash = Base64.getEncoder().encodeToString(hashedPin);
            
            return salt + ":" + hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear el PIN", e);
        }
    }
    
    /**
     * Verifica si el PIN ingresado coincide con el hasheado
     * @param pin PIN en texto plano
     * @param hashedPin PIN hasheado en formato "salt:hash"
     * @return true si coinciden
     */
    public static boolean verifyPin(String pin, String hashedPin) {
        try {
            String[] parts = hashedPin.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            String salt = parts[0];
            String hash = parts[1];
            
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(Base64.getDecoder().decode(salt));
            
            byte[] hashedInput = md.digest(pin.getBytes());
            String hashInput = Base64.getEncoder().encodeToString(hashedInput);
            
            return hash.equals(hashInput);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Genera un PIN aleatorio de 4 dígitos
     */
    public static String generarPinAleatorio() {
        SecureRandom random = new SecureRandom();
        int pin = 1000 + random.nextInt(9000);
        return String.valueOf(pin);
    }
    
    /**
     * Genera un número de tarjeta aleatorio de 16 dígitos
     */
    public static String generarNumeroTarjeta() {
        SecureRandom random = new SecureRandom();
        StringBuilder cardNumber = new StringBuilder();
        
        // Primer dígito: 4 (Visa) o 5 (Mastercard)
        cardNumber.append(random.nextBoolean() ? "4" : "5");
        
        // Siguientes 15 dígitos
        for (int i = 0; i < 15; i++) {
            cardNumber.append(random.nextInt(10));
        }
        
        return cardNumber.toString();
    }
}