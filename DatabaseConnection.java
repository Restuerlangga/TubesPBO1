import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Sesuaikan user/pass jika XAMPP anda berbeda
            String url = "jdbc:mysql://localhost:3307/db_inventaris_pbo";
            String user = "root";
            String password = ""; 
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
        }
        return conn;
    }
}