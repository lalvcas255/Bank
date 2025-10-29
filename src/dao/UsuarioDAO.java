package dao;

import modelo.Usuario;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para operaciones relacionadas con Usuario
 */
public class UsuarioDAO {
    
    private static final Logger LOGGER = Logger.getLogger(UsuarioDAO.class.getName());
    private final DatabaseConnection dbConnection;
    
    public UsuarioDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Inserta un nuevo usuario en la base de datos
     * @param usuario Usuario a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO signup (formno, nombre, apellido, fecha_nacimiento, genero, " +
                     "email, estado_civil, direccion, ciudad, codigo_postal, provincia) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getFormNo());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApellido());
            pstmt.setDate(4, Date.valueOf(usuario.getFechaNacimiento()));
            pstmt.setString(5, usuario.getGenero());
            pstmt.setString(6, usuario.getEmail());
            pstmt.setString(7, usuario.getEstadoCivil());
            pstmt.setString(8, usuario.getDireccion());
            pstmt.setString(9, usuario.getCiudad());
            pstmt.setString(10, usuario.getCodigoPostal());
            pstmt.setString(11, usuario.getProvincia());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                LOGGER.info("Usuario insertado correctamente: " + usuario.getFormNo());
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar usuario", e);
        }
        
        return false;
    }
    
    /**
     * Busca un usuario por su número de formulario
     * @param formNo Número de formulario
     * @return Usuario encontrado o null
     */
    public Usuario buscarPorFormNo(String formNo) {
        String sql = "SELECT * FROM signup WHERE formno = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, formNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setFormNo(rs.getString("formno"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                
                Date fecha = rs.getDate("fecha_nacimiento");
                if (fecha != null) {
                    usuario.setFechaNacimiento(fecha.toLocalDate());
                }
                
                usuario.setGenero(rs.getString("genero"));
                usuario.setEmail(rs.getString("email"));
                usuario.setEstadoCivil(rs.getString("estado_civil"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setCiudad(rs.getString("ciudad"));
                usuario.setCodigoPostal(rs.getString("codigo_postal"));
                usuario.setProvincia(rs.getString("provincia"));
                
                return usuario;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuario", e);
        }
        
        return null;
    }
    
    /**
     * Verifica si existe un email en la base de datos
     * @param email Email a verificar
     * @return true si existe
     */
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM signup WHERE email = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar email", e);
        }
        
        return false;
    }
    
    /**
     * Actualiza los datos de un usuario
     * @param usuario Usuario con datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE signup SET nombre = ?, apellido = ?, email = ?, " +
                     "direccion = ?, ciudad = ?, codigo_postal = ?, provincia = ? " +
                     "WHERE formno = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getDireccion());
            pstmt.setString(5, usuario.getCiudad());
            pstmt.setString(6, usuario.getCodigoPostal());
            pstmt.setString(7, usuario.getProvincia());
            pstmt.setString(8, usuario.getFormNo());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                LOGGER.info("Usuario actualizado correctamente: " + usuario.getFormNo());
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar usuario", e);
        }
        
        return false;
    }
}