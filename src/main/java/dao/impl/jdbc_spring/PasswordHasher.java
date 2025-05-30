package dao.impl.jdbc_spring;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class PasswordHasher {

    public static void main(String[] args) {
        // Cambia estos datos según tu conexión
        String url = "jdbc:mysql://localhost:3306/proyectohospital";
        String user = "root";
        String password = "1234";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // 1. Leer usuarios y contraseñas en texto plano
            String selectSQL = "SELECT customer_id, password FROM credentials";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
                 ResultSet rs = selectStmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("customer_id");
                    String plainPassword = rs.getString("password");

                    // 2. Hashear la contraseña
                    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

                    // 3. Actualizar la contraseña en la BBDD
                    String updateSQL = "UPDATE credentials SET password = ? WHERE customer_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                        updateStmt.setString(1, hashedPassword);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                        System.out.println("Contraseña hasheada para ID: " + id);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
