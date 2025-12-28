import java.util.ArrayList;

public class Transaksi {
    // Hapus atribut idTransaksi manual, karena nanti digenerate database (Auto Increment)
    private User user;
    private String tanggalKembali;
    
    // Tambahkan Keranjang Belanja
    private ArrayList<TransaksiDetail> listDetail;

    public Transaksi(User user) {
        this.user = user;
        this.listDetail = new ArrayList<>();
    }

    public void tambahItem(Barang b, int qty) {
        TransaksiDetail detail = new TransaksiDetail(b, qty);
        listDetail.add(detail);
    }

    public ArrayList<TransaksiDetail> getListDetail() {
        return listDetail;
    }

    public double hitungTotal() {
        double total = 0;
        for (TransaksiDetail d : listDetail) {
            total += d.getSubtotal();
        }
        return total;
    }

    public void setTanggalKembali(String tgl) {
        this.tanggalKembali = tgl;
    }
    
    public String TanggalKembali() {
        return tanggalKembali;
    }
}