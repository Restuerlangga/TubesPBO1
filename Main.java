import java.sql.*;
import java.util.*;

public class Main { 
    private static Scanner input = new Scanner(System.in);
    private static Akun akunAktif = null;

    public static void main(String[] args) {
        // Cek koneksi di awal
        if (DatabaseConnection.getConnection() == null) return;

        while(true){
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
                    case "1": handleLogin(); break;
                    case "2": handleRegister(); break;
                    case "3": handleGuestMode(); break;
                    case "4": 
                        System.out.println("Bye!"); 
                        return;
                }
            } else {
                System.out.println("Halo, " + akunAktif.getNama());
                if (akunAktif instanceof Admin) menuAdmin();
                else menuUser();
            }
        }
    }

    private static void handleLogin() {
        System.out.print("Username: "); String u = input.nextLine();
        System.out.print("Password: "); String p = input.nextLine();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Cek Admin
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM admin WHERE username=? AND password=?");
            ps.setString(1, u); ps.setString(2, p);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                akunAktif = new Admin(rs.getInt("id"), rs.getString("nama"), u, p);
                akunAktif.login();
                return;
            }

            // Cek User
            ps = conn.prepareStatement("SELECT * FROM user WHERE username=? AND password=?");
            ps.setString(1, u); ps.setString(2, p);
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
        System.out.print("Nama: "); String n = input.nextLine();
        System.out.print("Username: "); String u = input.nextLine();
        System.out.print("Password: "); String p = input.nextLine();
        System.out.print("Alamat: "); String a = input.nextLine();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO user (nama, username, password, alamat) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, n); ps.setString(2, u); ps.setString(3, p); ps.setString(4, a);
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
            while(rs.next()) {
                System.out.printf("[%s] %s | Rp %,.0f\n", 
                    rs.getString("kode_barang"), rs.getString("nama_barang"), rs.getDouble("harga_sewa"));
            }
        } catch (Exception e) {}
    }

    // --- MENU SETELAH LOGIN ---

    private static void menuAdmin() {
        Admin adm = (Admin) akunAktif;
        System.out.println("\n1. Tambah Barang\n2. Hapus Barang\n3. Update Barang\n0. Logout");
        System.out.print("Pilih: ");
        String pil = input.nextLine();

        if (pil.equals("1")) {
            System.out.print("Kode: "); String k = input.nextLine();
            System.out.print("Nama: "); String n = input.nextLine();
            System.out.print("Stok: "); int s = Integer.parseInt(input.nextLine());
            System.out.print("Harga: "); double h = Double.parseDouble(input.nextLine());
            adm.tambahBarang(new Barang(0, k, n, "Umum", s, h));
        } else if (pil.equals("2")) {
            System.out.print("Kode barang dihapus: ");
            adm.hapusBarang(input.nextLine());
        } else if (pil.equals("3")) {
            System.out.print("Kode barang diedit: "); String k = input.nextLine();
            System.out.print("Nama Baru: "); String n = input.nextLine();
            System.out.print("Stok Baru: "); int s = Integer.parseInt(input.nextLine());
            System.out.print("Harga Baru: "); double h = Double.parseDouble(input.nextLine());
            adm.updateBarang(new Barang(0, k, n, "Umum", s, h));
        } else if (pil.equals("0")) {
            akunAktif.logout();
            akunAktif = null;
        }
    }

    private static void menuUser() {
        User usr = (User) akunAktif;
        System.out.println("\n1. Belanja / Pinjam\n0. Logout");
        System.out.print("Pilih: ");
        String pil = input.nextLine();

        if (pil.equals("1")) {
            Transaksi trx = new Transaksi(usr);
            while(true) {
                // Tampilkan barang user
                for(Barang b : usr.lihatBarang()) System.out.println(b);
                
                System.out.print("Masukkan Kode Barang (ketik 'bayar' selesai): ");
                String kode = input.nextLine();
                if(kode.equalsIgnoreCase("bayar")) break;

                Barang pilih = null;
                for(Barang b : usr.lihatBarang()) if(b.getKodeBarang().equalsIgnoreCase(kode)) pilih = b;

                if(pilih != null) {
                    System.out.print("Jumlah: ");
                    int qty = Integer.parseInt(input.nextLine());
                    if (qty <= pilih.getStok()) {
                        trx.tambahItem(pilih, qty);
                        System.out.println("Masuk keranjang.");
                    } else System.out.println("Stok kurang.");
                } else System.out.println("Barang tidak ada.");
            }
            
            if(!trx.getListDetail().isEmpty()) {
                System.out.print("Tanggal Kembali (YYYY-MM-DD): ");
                trx.setTanggalKembali(input.nextLine());
                usr.checkout(trx);
            }
        } else if (pil.equals("0")) {
            akunAktif.logout();
            akunAktif = null;
        }
    }
}