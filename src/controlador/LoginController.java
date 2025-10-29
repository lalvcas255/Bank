package controlador;

import dao.CuentaDAO;
import modelo.Cuenta;
import util.Validador;

import java.util.logging.Logger;

/**
 * Controlador para operaciones de login
 */
public class LoginController {
    
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());
    private final CuentaDAO cuentaDAO;
    
    public LoginController() {
        this.cuentaDAO = new CuentaDAO();
    }
    
    /**
     * Intenta iniciar sesión con las credenciales proporcionadas
     * @param numeroTarjeta Número de tarjeta
     * @param pin PIN
     * @return Cuenta si el login es exitoso, null si falla
     */
    public Cuenta iniciarSesion(String numeroTarjeta, String pin) {
        // Validar formato de entrada
        if (!Validador.esTarjetaValida(numeroTarjeta)) {
            LOGGER.warning("Número de tarjeta inválido: " + numeroTarjeta);
            return null;
        }
        
        if (!Validador.esPinValido(pin)) {
            LOGGER.warning("PIN inválido");
            return null;
        }
        
        // Validar credenciales
        if (cuentaDAO.validarLogin(numeroTarjeta, pin)) {
            return cuentaDAO.obtenerCuenta(numeroTarjeta);
        }
        
        return null;
    }
    
    /**
     * Verifica si existe una tarjeta
     * @param numeroTarjeta Número de tarjeta
     * @return true si existe
     */
    public boolean existeTarjeta(String numeroTarjeta) {
        return cuentaDAO.existeTarjeta(numeroTarjeta);
    }
}