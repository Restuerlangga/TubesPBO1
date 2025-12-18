import java.util.ArrayList;

public class Admin extends Akun implements PengelolaanBarang {

    private ArrayList<Barang> daftarBarang;

    public Admin(int id, String nama, String username, String password) {
        super(id, nama, username, password);
        this.daftarBarang = new ArrayList<>();
    }

    @Override
    public void login() {
        System.out.println("Admin " + nama + " berhasil login.");
    }

    @Override
    public void tambahBarang(Barang barang) {
        daftarBarang.add(barang);
        System.out.println("Barang berhasil ditambahkan.");
    }

    @Override
    public void hapusBarang(String kodeBarang) {
        daftarBarang.removeIf(b -> b.getKodeBarang().equals(kodeBarang));
        System.out.println("Barang berhasil dihapus.");
    }

    @Override
    public void updateBarang(Barang barang) {
        for (int i = 0; i < daftarBarang.size(); i++) {
            if (daftarBarang.get(i).getKodeBarang().equals(barang.getKodeBarang())) {
                daftarBarang.set(i, barang);
                System.out.println("Barang berhasil diupdate.");
                return;
            }
        }
    }

    @Override
    public void lihatSemuaBarang() {
        for (Barang b : daftarBarang) {
            System.out.println(b);
        }
    }
}
