import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class User extends Akun {

    private ArrayList<Transaksi> riwayatTransaksi;

    public User(int id, String nama, String username, String password) {
        super(id, nama, username, password);
        this.riwayatTransaksi = new ArrayList<>();
    }

    @Override
    public void login() {
        System.out.println("User " + nama + " berhasil login.");
    }

    // OVERLOADING
    public void pinjamBarang(Barang barang) {
        pinjamBarang(barang, 1);
    }

    public void pinjamBarang(Barang barang, int jumlah) {
        if (barang.kurangiStok(jumlah)) {
            System.out.println("Berhasil meminjam " + jumlah + " " + barang.getNamaBarang());
        } else {
            System.out.println("Stok tidak mencukupi.");
        }
    }

    public Transaksi buatTransaksi(String idTransaksi) {
        String tanggalPinjam = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Transaksi transaksi = new Transaksi(idTransaksi, this, tanggalPinjam);
        riwayatTransaksi.add(transaksi);
        return transaksi;
    }

    public ArrayList<Transaksi> getRiwayatTransaksi() {
        return riwayatTransaksi;
    }
}
