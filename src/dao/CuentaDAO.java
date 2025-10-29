package dao;

import modelo.Cuenta;
import util.DatabaseConnection;
import util.SecurityUtil;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para operaciones relacionadas con Cuenta
 */
public class CuentaDAO {
    
    private static final Logger LOGGER = Logger.getLogger(CuentaDAO.class.getName());
    private final DatabaseConnection dbConnection;
    
    public CuentaDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Crea una nueva cuenta bancaria
     * @param cuenta Cuenta a crear
     * @param pinTextoPlano PIN en texto plano (será hasheado)
     * @return true si se creó correctamente
     */
    public boolean crearCuenta(Cuenta cuenta, String pinTextoPlano) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Hashear el PIN antes de guardarlo
            String pinHasheado = SecurityUtil.hashPin(pinTextoPlano);
            
            // Insertar en signupthree
            String sql1 = "INSERT INTO signupthree (formno, tipo_cuenta, numero_tarjeta, " +
                          "pin, servicios) VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(sql1)) {
                pstmt1.setString(1, cuenta.getFormNo());
                pstmt1.setString(2, cuenta.getTipoCuenta());
                pstmt1.setString(3, cuenta.getNumeroTarjeta());
                pstmt1.setString(4, pinHasheado);
                pstmt1.setString(5, cuenta.getServicios());
                pstmt1.executeUpdate();
            }
            
            // Insertar en login
            String sql2 = "INSERT INTO login (formno, numero_tarjeta, pin) VALUES (?, ?, ?)";
            
            try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setString(1, cuenta.getFormNo());
                pstmt2.setString(2, cuenta.getNumeroTarjeta());
                pstmt2.setString(3, pinHasheado);
                pstmt2.executeUpdate();
            }
            
            // Crear registro de saldo inicial
            String sql3 = "INSERT INTO bank (pin, fecha, tipo, monto) VALUES (?, NOW(), 'Depósito', 0)";
            
            try (PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {
                pstmt3.setString(1, pinHasheado);
                pstmt3.executeUpdate();
            }
            
            conn.commit(); // Confirmar transacción
            LOGGER.info("Cuenta creada correctamente: " + cuenta.getNumeroTarjeta());
            return true;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear cuenta", e);
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir cambios en caso de error
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al hacer rollback", ex);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error al restaurar autocommit", e);
                }
            }
        }
        
        return false;
    }
    
    /**
     * Valida el login de un usuario
     * @param numeroTarjeta Número de tarjeta
     * @param pin PIN en texto plano
     * @return true si las credenciales son válidas
     */
    public boolean validarLogin(String numeroTarjeta, String pin) {
        String sql = "SELECT pin FROM login WHERE numero_tarjeta = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroTarjeta);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String pinHasheado = rs.getString("pin");
                boolean valido = SecurityUtil.verifyPin(pin, pinHasheado);
                
                if (valido) {
                    LOGGER.info("Login exitoso para tarjeta: " + numeroTarjeta);
                } else {
                    LOGGER.warning("Intento de login fallido para tarjeta: " + numeroTarjeta);
                }
                
                return valido;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al validar login", e);
        }
        
        return false;
    }
    
    /**
     * Obtiene una cuenta por número de tarjeta
     * @param numeroTarjeta Número de tarjeta
     * @return Cuenta encontrada o null
     */
    public Cuenta obtenerCuenta(String numeroTarjeta) {
        String sql = "SELECT s.*, l.pin FROM signupthree s " +
                     "JOIN login l ON s.numero_tarjeta = l.numero_tarjeta " +
                     "WHERE s.numero_tarjeta = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroTarjeta);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setFormNo(rs.getString("formno"));
                cuenta.setTipoCuenta(rs.getString("tipo_cuenta"));
                cuenta.setNumeroTarjeta(rs.getString("numero_tarjeta"));
                cuenta.setPin(rs.getString("pin"));
                cuenta.setServicios(rs.getString("servicios"));
                
                // Obtener saldo actual
                cuenta.setSaldo(obtenerSaldo(rs.getString("pin")));
                
                return cuenta;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener cuenta", e);
        }
        
        return null;
    }
    
    /**
     * Obtiene el saldo actual de una cuenta
     * @param pinHasheado PIN hasheado
     * @return Saldo actual
     */
    public double obtenerSaldo(String pinHasheado) {
        String sql = "SELECT SUM(CASE WHEN tipo = 'Depósito' THEN monto ELSE -monto END) as saldo " +
                     "FROM bank WHERE pin = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, pinHasheado);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener saldo", e);
        }
        
        return 0.0;
    }
    
    /**
     * Registra una transacción
     * @param pinHasheado PIN hasheado
     * @param tipo Tipo de transacción (Depósito/Retiro)
     * @param monto Monto de la transacción
     * @return true si se registró correctamente
     */
    public boolean registrarTransaccion(String pinHasheado, String tipo, double monto) {
        String sql = "INSERT INTO bank (pin, fecha, tipo, monto) VALUES (?, NOW(), ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, pinHasheado);
            pstmt.setString(2, tipo);
            pstmt.setDouble(3, monto);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                LOGGER.info("Transacción registrada: " + tipo + " de " + monto);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al registrar transacción", e);
        }
        
        return false;
    }
    
    /**
     * Verifica si existe una tarjeta
     * @param numeroTarjeta Número de tarjeta
     * @return true si existe
     */
    public boolean existeTarjeta(String numeroTarjeta) {
        String sql = "SELECT COUNT(*) FROM login WHERE numero_tarjeta = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroTarjeta);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar tarjeta", e);
        }
        
        return false;
    }
}
