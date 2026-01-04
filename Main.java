import java.sql.*;
import java.util.*;

public class Main {
    private static Scanner input = new Scanner(System.in);
    private static Akun akunAktif = null;

    public static void main(String[] args) {
        // Cek koneksi di awal
        if (DatabaseConnection.getConnection() == null)
            return;

        while (true) {
            System.out.println("\n=================================");
            System.out.println("Inventaris Barang Console App");

            if (akunAktif == null) {
                System.out.println("1. Login");
                System.out.println("2. Register User");
                System.out.println("3. Lihat Barang (Guest)");
                System.out.println("4. Keluar");
                System.out.print("Pilih Menu: ");
                String menu = input.nextLine();

                switch (menu) {
                    case "1":
                        handleLogin();
                        break;
                    case "2":
                        handleRegister();
                        break;
                    case "3":
                        handleGuestMode();
                        break;
                    case "4":
                        System.out.println("Bye!");
                        return;
                }
            } else {
                System.out.println("Halo, " + akunAktif.getNama());
                if (akunAktif instanceof Admin)
                    menuAdmin();
                else
                    menuUser();
            }
        }
    }

    private static void handleLogin() {
        System.out.print("Username: ");
        String u = input.nextLine();
        System.out.print("Password: ");
        String p = input.nextLine();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Cek Admin
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM admin WHERE username=? AND password=?");
            ps.setString(1, u);
            ps.setString(2, p);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                akunAktif = new Admin(rs.getInt("id"), rs.getString("nama"), u, p);
                akunAktif.login();
                return;
            }

            // Cek User
            ps = conn.prepareStatement("SELECT * FROM user WHERE username=? AND password=?");
            ps.setString(1, u);
            ps.setString(2, p);
            rs = ps.executeQuery();
            if (rs.next()) {
                akunAktif = new User(rs.getInt("id"), rs.getString("nama"), u, p, rs.getString("alamat"));
                akunAktif.login();
                return;
            }
            System.out.println("Gagal Login! Cek username/password.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleRegister() {
        System.out.print("Nama: ");
        String n = input.nextLine();
        System.out.print("Username: ");
        String u = input.nextLine();
        System.out.print("Password: ");
        String p = input.nextLine();
        System.out.print("Alamat: ");
        String a = input.nextLine();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO user (nama, username, password, alamat) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, n);
            ps.setString(2, u);
            ps.setString(3, p);
            ps.setString(4, a);
            ps.executeUpdate();
            System.out.println("Register Berhasil! Silakan Login.");
        } catch (SQLException e) {
            System.out.println("Gagal Register: " + e.getMessage());
        }
    }

    private static void handleGuestMode() {
        System.out.println("\n--- DAFTAR BARANG (GUEST) ---");
        // Kita pakai trik pinjam method static dari User/Admin atau query langsung
        // Disini query langsung biar cepat
        try (Connection conn = DatabaseConnection.getConnection();
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM barang")) {
            while (rs.next()) {
                System.out.printf("[%s] %s | Rp %,.0f\n",
                        rs.getString("kode_barang"), rs.getString("nama_barang"), rs.getDouble("harga_sewa"));
            }
        } catch (Exception e) {
        }
    }

    // --- MENU SETELAH LOGIN ---

    private static void menuAdmin() {
        Admin adm = (Admin) akunAktif;
        System.out.println("\n1. Tambah Barang\n2. Hapus Barang\n3. Update Barang\n4. Lihat Laporan\n0. Logout");
        System.out.print("Pilih: ");
        String pil = input.nextLine();

        if (pil.equals("1")) {
            System.out.print("Kode: ");
            String k = input.nextLine();
            System.out.print("Nama: ");
            String n = input.nextLine();
            System.out.print("Stok: ");
            int s = Integer.parseInt(input.nextLine());
            System.out.print("Harga: ");
            double h = Double.parseDouble(input.nextLine());
            adm.tambahBarang(new Barang(0, k, n, "Umum", s, h));
        } else if (pil.equals("2")) {
            System.out.print("Kode barang dihapus: ");
            adm.hapusBarang(input.nextLine());
        } else if (pil.equals("3")) {
            System.out.print("Kode barang diedit: ");
            String k = input.nextLine();
            System.out.print("Nama Baru: ");
            String n = input.nextLine();
            System.out.print("Stok Baru: ");
            int s = Integer.parseInt(input.nextLine());
            System.out.print("Harga Baru: ");
            double h = Double.parseDouble(input.nextLine());
            adm.updateBarang(new Barang(0, k, n, "Umum", s, h));
        } else if (pil.equals("4")) {
            adm.lihatSemuaLaporan();
        } else if (pil.equals("0")) {
            akunAktif.logout();
            akunAktif = null;
        }
    }

    private static void menuUser() {
        User usr = (User) akunAktif;
        System.out.println("\n1. Belanja / Pinjam");
        System.out.println("2. Kembalikan Barang");
        System.out.println("3. Lihat Riwayat");
        System.out.println("0. Logout");
        System.out.print("Pilih: ");
        String pil = input.nextLine();

        if (pil.equals("1")) {
            Transaksi trx = new Transaksi(usr);

            // ===== LOOP PILIH BARANG =====
            while (true) {
                // Tampilkan barang user
                for (Barang b : usr.lihatBarang()) {
                    System.out.println(b);
                }

                System.out.print("Masukkan Kode Barang (ketik 'bayar' selesai): ");
                String kode = input.nextLine();

                if (kode.equalsIgnoreCase("bayar")) {
                    break; // keluar loop pilih barang
                }

                Barang pilih = null;
                for (Barang b : usr.lihatBarang()) {
                    if (b.getKodeBarang().equalsIgnoreCase(kode)) {
                        pilih = b;
                        break;
                    }
                }

                if (pilih != null) {
                    System.out.print("Jumlah: ");
                    int qty = Integer.parseInt(input.nextLine());

                    if (qty <= pilih.getStok()) {
                        trx.tambahItem(pilih, qty);
                        System.out.println("Masuk keranjang.");
                    } else {
                        System.out.println("Stok kurang.");
                    }
                } else {
                    System.out.println("Barang tidak ada.");
                }
            }

            // ===== CHECKOUT (HANYA SEKALI) =====
            if (!trx.getListDetail().isEmpty()) {

                System.out.println("\n--- DETAIL TRANSAKSI ---");
                for (TransaksiDetail item : trx.getListDetail()) {
                    System.out.println(item); // toString() kepanggil
                }
                System.out.println("-------------------------");
                System.out.printf("TOTAL             Rp %,.0f\n", trx.hitungTotal());

                System.out.print("\nTanggal Kembali (YYYY-MM-DD): ");
                trx.setTanggalKembali(input.nextLine());

                usr.checkout(trx);
                return; // PENTING: stop menuUser setelah checkout
            }

        } else if (pil.equals("2")) {
            kembalikanBarang(usr);
            return;
        } else if (pil.equals("3")) {
            usr.lihatRiwayat();
            return;
        } else if (pil.equals("0")) {
            akunAktif.logout();
            akunAktif = null;
        }
    }

    private static void kembalikanBarang(User usr) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // 1. Tampilkan transaksi yang masih DIPINJAM oleh user
            String sqlTrx = """
                        SELECT id, tanggal_pinjam, tanggal_kembali, total_biaya
                        FROM transaksi
                        WHERE id_user = ? AND status = 'DIPINJAM'
                    """;

            PreparedStatement ps = conn.prepareStatement(sqlTrx);
            ps.setInt(1, usr.getId());
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- TRANSAKSI YANG SEDANG DIPINJAM ---");

            boolean ada = false;
            while (rs.next()) {
                ada = true;
                System.out.printf(
                        "ID: %d | Pinjam: %s | Kembali: %s | Total: Rp %,.0f%n",
                        rs.getInt("id"),
                        rs.getString("tanggal_pinjam"),
                        rs.getString("tanggal_kembali"),
                        rs.getDouble("total_biaya"));
            }

            if (!ada) {
                System.out.println("Tidak ada transaksi yang sedang dipinjam.");
                return;
            }

            // 2. Pilih transaksi yang dikembalikan
            System.out.print("\nMasukkan ID Transaksi yang akan dikembalikan: ");
            int idTrx = Integer.parseInt(input.nextLine());

            conn.setAutoCommit(false); // transaksi DB

            // 3. Ambil detail transaksi
            String sqlDetail = """
                        SELECT id_barang, qty
                        FROM transaksi_detail
                        WHERE id_transaksi = ?
                    """;

            ps = conn.prepareStatement(sqlDetail);
            ps.setInt(1, idTrx);
            rs = ps.executeQuery();

            // 4. Tambah stok barang kembali
            String sqlUpdateStok = "UPDATE barang SET stok = stok + ? WHERE id = ?";
            PreparedStatement psStok = conn.prepareStatement(sqlUpdateStok);

            while (rs.next()) {
                psStok.setInt(1, rs.getInt("qty"));
                psStok.setInt(2, rs.getInt("id_barang"));
                psStok.executeUpdate();
            }

            // 5. Update status transaksi
            String sqlUpdateTrx = "UPDATE transaksi SET status = 'DIKEMBALIKAN' WHERE id = ?";
            ps = conn.prepareStatement(sqlUpdateTrx);
            ps.setInt(1, idTrx);
            ps.executeUpdate();

            conn.commit();

            System.out.println("\nBarang berhasil dikembalikan. Terima kasih 🙏");

        } catch (Exception e) {
            System.out.println("Gagal mengembalikan barang: " + e.getMessage());
        }
    }

}