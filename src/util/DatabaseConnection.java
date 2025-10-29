package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para gestionar conexiones a la base de datos
 * Implementa el patrón Singleton
 */
public class DatabaseConnection {
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    
    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/banksystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // Cambiar por tu contraseña
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    /**
     * Constructor privado para Singleton
     */
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            LOGGER.info("Conexión a la base de datos establecida correctamente");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver de MySQL no encontrado", e);
            throw new RuntimeException("Error al cargar el driver de MySQL", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al conectar con la base de datos", e);
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }
    
    /**
     * Obtiene la instancia única de DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Obtiene la conexión a la base de datos
     */
    public Connection getConnection() {
        try {
            // Verifica si la conexión está cerrada y la reabre
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                LOGGER.info("Conexión reestablecida");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar conexión", e);
        }
        return connection;
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Conexión cerrada correctamente");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error al cerrar conexión", e);
            }
        }
    }
    
    /**
     * Verifica si la conexión está activa
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}