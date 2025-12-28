import java.sql.*;
import java.util.ArrayList;

public class User extends Akun implements Pembayaran {
    private String alamat;

    public User(int id, String nama, String username, String password, String alamat) {
        super(id, nama, username, password);
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    @Override
    public void login() {
        System.out.println("User " + nama + " berhasil login!");
    }

    // Method untuk mengambil daftar barang dari Database
    public ArrayList<Barang> lihatBarang() {
        ArrayList<Barang> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM barang");
            while (rs.next()) {
                list.add(new Barang(
                    rs.getInt("id"),
                    rs.getString("kode_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("kategori"),
                    rs.getInt("stok"),
                    rs.getDouble("harga_sewa")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Gagal load barang: " + e.getMessage());
        }
        return list;
    }

    // LOGIC CHECKOUT KE DATABASE
    public void checkout(Transaksi trx) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Transaksi Database (Safety)

            // 1. Simpan Header Transaksi
            String sqlHead = "INSERT INTO transaksi (id_user, tanggal_pinjam, tanggal_kembali, total_biaya, status) VALUES (?, NOW(), ?, ?, ?)";
            PreparedStatement psHead = conn.prepareStatement(sqlHead, Statement.RETURN_GENERATED_KEYS);
            psHead.setInt(1, this.id);
            // Tanggal kembali hitung manual sederhana atau input user (disini pakai string dulu sesuai database anda)
            psHead.setString(2, trx.TanggalKembali()); 
            psHead.setDouble(3, trx.hitungTotal());
            psHead.setString(4, "DIPINJAM");
            psHead.executeUpdate();

            // Ambil ID Transaksi baru
            ResultSet rs = psHead.getGeneratedKeys();
            int idTrx = 0;
            if (rs.next()) idTrx = rs.getInt(1);

            // 2. Simpan Detail & Kurangi Stok
            String sqlDet = "INSERT INTO transaksi_detail (id_transaksi, id_barang, qty, subtotal) VALUES (?,?,?,?)";
            String sqlStok = "UPDATE barang SET stok = stok - ? WHERE id = ?";
            PreparedStatement psDet = conn.prepareStatement(sqlDet);
            PreparedStatement psStok = conn.prepareStatement(sqlStok);

            for (TransaksiDetail item : trx.getListDetail()) {
                psDet.setInt(1, idTrx);
                psDet.setInt(2, item.getBarang().getid());
                psDet.setInt(3, item.getJumlah());
                psDet.setDouble(4, item.getSubtotal());
                psDet.executeUpdate();

                psStok.setInt(1, item.getJumlah());
                psStok.setInt(2, item.getBarang().getid());
                psStok.executeUpdate();
            }

            conn.commit(); // Simpan permanen
            System.out.println("Transaksi Berhasil! Total: " + trx.hitungTotal());
            prosesPembayaran(trx.hitungTotal());

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Transaksi Gagal: " + e.getMessage());
        }

        
    }

    @Override
    public void prosesPembayaran(double total) {
        System.out.println("Pembayaran sebesar Rp " + total + " telah dicatat sistem.");
    }

    
}

