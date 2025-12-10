package gruasheredianas.conectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author franco
 */
public class ConexionDB {
    
    private static final String URL = "jdbc:mysql://localhost:3306/gruas_heredianas";
    private static final String USER = "root";
    private static final String PASSWORD = "Yarikori12";

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a la base de datos exitosa");
        } catch (SQLException e) {
            System.out.println("Error en conexión: " + e.getMessage());
        }
        return con;
    }
}
