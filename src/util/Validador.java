package util;

import java.util.regex.Pattern;

/**
 * Clase para validar datos de entrada
 */
public class Validador {
    
    // Expresiones regulares para validación
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern NOMBRE_PATTERN = 
        Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,50}$");
    
    private static final Pattern TELEFONO_PATTERN = 
        Pattern.compile("^[0-9]{9,15}$");
    
    private static final Pattern CODIGO_POSTAL_PATTERN = 
        Pattern.compile("^[0-9]{5}$");
    
    private static final Pattern PIN_PATTERN = 
        Pattern.compile("^[0-9]{4}$");
    
    private static final Pattern TARJETA_PATTERN = 
        Pattern.compile("^[0-9]{16}$");
    
    /**
     * Valida que el texto no esté vacío
     */
    public static boolean noVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    /**
     * Valida formato de email
     */
    public static boolean esEmailValido(String email) {
        if (!noVacio(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Valida formato de nombre (solo letras y espacios)
     */
    public static boolean esNombreValido(String nombre) {
        if (!noVacio(nombre)) {
            return false;
        }
        return NOMBRE_PATTERN.matcher(nombre).matches();
    }
    
    /**
     * Valida formato de teléfono
     */
    public static boolean esTelefonoValido(String telefono) {
        if (!noVacio(telefono)) {
            return false;
        }
        return TELEFONO_PATTERN.matcher(telefono).matches();
    }
    
    /**
     * Valida formato de código postal español
     */
    public static boolean esCodigoPostalValido(String codigoPostal) {
        if (!noVacio(codigoPostal)) {
            return false;
        }
        return CODIGO_POSTAL_PATTERN.matcher(codigoPostal).matches();
    }
    
    /**
     * Valida formato de PIN (4 dígitos)
     */
    public static boolean esPinValido(String pin) {
        if (!noVacio(pin)) {
            return false;
        }
        return PIN_PATTERN.matcher(pin).matches();
    }
    
    /**
     * Valida formato de tarjeta (16 dígitos)
     */
    public static boolean esTarjetaValida(String tarjeta) {
        if (!noVacio(tarjeta)) {
            return false;
        }
        return TARJETA_PATTERN.matcher(tarjeta).matches();
    }
    
    /**
     * Valida que un número sea positivo
     */
    public static boolean esMontoValido(double monto) {
        return monto > 0;
    }
    
    /**
     * Valida longitud mínima
     */
    public static boolean longitudMinima(String texto, int min) {
        return noVacio(texto) && texto.trim().length() >= min;
    }
    
    /**
     * Valida longitud máxima
     */
    public static boolean longitudMaxima(String texto, int max) {
        return noVacio(texto) && texto.trim().length() <= max;
    }
    
    /**
     * Obtiene mensaje de error para validación
     */
    public static String getMensajeError(String campo, String tipo) {
        switch (tipo) {
            case "vacio":
                return "El campo " + campo + " es obligatorio";
            case "email":
                return "El email ingresado no es válido";
            case "nombre":
                return "El nombre solo puede contener letras";
            case "telefono":
                return "El teléfono debe tener entre 9 y 15 dígitos";
            case "postal":
                return "El código postal debe tener 5 dígitos";
            case "pin":
                return "El PIN debe tener 4 dígitos";
            case "tarjeta":
                return "El número de tarjeta debe tener 16 dígitos";
            default:
                return "Dato inválido en " + campo;
        }
    }
}