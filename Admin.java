import java.sql.*;

public class Admin extends Akun implements PengelolaanBarang {

    public Admin(int id, String nama, String username, String password) {
        super(id, nama, username, password);
    }

    @Override
    public void login() {
        System.out.println("Admin " + nama + " (Database Connected) Ready.");
    }

    @Override
    public void tambahBarang(Barang b) {
        String sql = "INSERT INTO barang (kode_barang, nama_barang, kategori, stok, harga_sewa) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getKodeBarang());
            ps.setString(2, b.getNamaBarang());
            ps.setString(3, "Umum"); // Default kategori (bisa disesuaikan)
            ps.setInt(4, b.getStok());
            ps.setDouble(5, b.getHargaSewa());
            ps.executeUpdate();
            System.out.println("Sukses tambah barang ke Database.");
        } catch (SQLException e) {
            System.out.println("Gagal tambah: " + e.getMessage());
        }
    }

    @Override
    public void hapusBarang(String kodeBarang) {
        String sql = "DELETE FROM barang WHERE kode_barang=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kodeBarang);
            ps.executeUpdate();
            System.out.println("Barang dihapus dari Database.");
        } catch (SQLException e) {
            System.out.println("Gagal hapus: " + e.getMessage());
        }
    }

    @Override
    public void updateBarang(Barang b) {
        // Update stok dan harga berdasarkan kode
        String sql = "UPDATE barang SET nama_barang=?, stok=?, harga_sewa=? WHERE kode_barang=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getNamaBarang());
            ps.setInt(2, b.getStok());
            ps.setDouble(3, b.getHargaSewa());
            ps.setString(4, b.getKodeBarang());
            int row = ps.executeUpdate();
            if (row > 0) System.out.println("Barang berhasil diupdate.");
            else System.out.println("Kode barang tidak ditemukan.");
        } catch (SQLException e) {
            System.out.println("Gagal update: " + e.getMessage());
        }
    }

    @Override
    public void lihatSemuaBarang() {
        // Fitur ini sama dengan user, select * from barang
        try (Connection conn = DatabaseConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM barang")) {
            while(rs.next()) {
                System.out.printf("[%s] %s | Stok: %d\n", 
                    rs.getString("kode_barang"), rs.getString("nama_barang"), rs.getInt("stok"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}