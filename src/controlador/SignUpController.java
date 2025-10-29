package controlador;

import dao.CuentaDAO;
import dao.UsuarioDAO;
import modelo.Cuenta;
import modelo.Usuario;
import util.SecurityUtil;
import util.Validador;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Controlador para operaciones de registro
 */
public class SignupController {
    
    private static final Logger LOGGER = Logger.getLogger(SignupController.class.getName());
    private final UsuarioDAO usuarioDAO;
    private final CuentaDAO cuentaDAO;
    
    public SignupController() {
        this.usuarioDAO = new UsuarioDAO();
        this.cuentaDAO = new CuentaDAO();
    }
    
    /**
     * Genera un número de formulario único
     * @return Número de formulario
     */
    public String generarNumeroFormulario() {
        Random random = new Random();
        long numero = Math.abs((random.nextLong() % 9000L) + 1000L);
        return String.valueOf(numero);
    }
    
    /**
     * Registra un nuevo usuario
     * @param usuario Usuario a registrar
     * @return Número de formulario si se registró correctamente, null si falla
     */
    public String registrarUsuario(Usuario usuario) {
        // Validar datos del usuario
        if (!validarDatosUsuario(usuario)) {
            return null;
        }
        
        // Verificar si el email ya existe
        if (usuarioDAO.existeEmail(usuario.getEmail())) {
            LOGGER.warning("Email ya registrado: " + usuario.getEmail());
            return null;
        }
        
        // Generar número de formulario
        String formNo = generarNumeroFormulario();
        usuario.setFormNo(formNo);
        
        // Insertar usuario
        if (usuarioDAO.insertarUsuario(usuario)) {
            LOGGER.info("Usuario registrado exitosamente: " + formNo);
            return formNo;
        }
        
        return null;
    }
    
    /**
     * Crea una cuenta bancaria para un usuario
     * @param formNo Número de formulario del usuario
     * @param tipoCuenta Tipo de cuenta
     * @param servicios Servicios seleccionados
     * @return Objeto con número de tarjeta y PIN si se creó correctamente
     */
    public DatosCuenta crearCuenta(String formNo, String tipoCuenta, String servicios) {
        // Validar que el usuario existe
        Usuario usuario = usuarioDAO.buscarPorFormNo(formNo);
        if (usuario == null) {
            LOGGER.warning("Usuario no encontrado: " + formNo);
            return null;
        }
        
        // Generar número de tarjeta y PIN únicos
        String numeroTarjeta;
        do {
            numeroTarjeta = SecurityUtil.generarNumeroTarjeta();
        } while (cuentaDAO.existeTarjeta(numeroTarjeta));
        
        String pin = SecurityUtil.generarPinAleatorio();
        
        // Crear cuenta
        Cuenta cuenta = new Cuenta();
        cuenta.setFormNo(formNo);
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setNumeroTarjeta(numeroTarjeta);
        cuenta.setServicios(servicios);
        
        if (cuentaDAO.crearCuenta(cuenta, pin)) {
            LOGGER.info("Cuenta creada exitosamente: " + numeroTarjeta);
            return new DatosCuenta(numeroTarjeta, pin);
        }
        
        return null;
    }
    
    /**
     * Valida los datos del usuario
     * @param usuario Usuario a validar
     * @return true si los datos son válidos
     */
    private boolean validarDatosUsuario(Usuario usuario) {
        if (!Validador.esNombreValido(usuario.getNombre())) {
            LOGGER.warning("Nombre inválido");
            return false;
        }
        
        if (!Validador.esEmailValido(usuario.getEmail())) {
            LOGGER.warning("Email inválido");
            return false;
        }
        
        if (!Validador.noVacio(usuario.getDireccion())) {
            LOGGER.warning("Dirección vacía");
            return false;
        }
        
        if (!Validador.noVacio(usuario.getCiudad())) {
            LOGGER.warning("Ciudad vacía");
            return false;
        }
        
        if (!Validador.noVacio(usuario.getProvincia())) {
            LOGGER.warning("Provincia vacía");
            return false;
        }
        
        if (usuario.getFechaNacimiento() == null) {
            LOGGER.warning("Fecha de nacimiento no especificada");
            return false;
        }
        
        return true;
    }
    
    /**
     * Clase interna para devolver datos de cuenta
     */
    public static class DatosCuenta {
        private final String numeroTarjeta;
        private final String pin;
        
        public DatosCuenta(String numeroTarjeta, String pin) {
            this.numeroTarjeta = numeroTarjeta;
            this.pin = pin;
        }
        
        public String getNumeroTarjeta() {
            return numeroTarjeta;
        }
        
        public String getPin() {
            return pin;
        }
    }
}