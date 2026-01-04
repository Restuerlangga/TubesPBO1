import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // Sesuaikan konfigurasi database Anda
    private static final String URL = "jdbc:mysql://localhost:3307/db_inventaris_pbo";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Register Driver (Penting untuk beberapa versi NetBeans/Java)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Buat Koneksi
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println("Koneksi ke Database BERHASIL!"); // (Opsional: nyalakan jika ingin debug)
            
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Koneksi GAGAL: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        System.out.println("Mencoba menghubungkan ke database...");
        Connection cek = getConnection();
        
        if (cek != null) {
          System.out.println("Koneksi Sukses");
        } else {
           System.out.println("Koneksi Gagal");
        }
    }
}