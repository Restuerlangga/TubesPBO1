

public class Transaksi {
    private String idTransaksi;
    private User user;
    private String tanggalPinjam;
    private String tanggalKembali;

    public Transaksi(String idTransaksi, User user, String tanggalPinjam) {
        this.idTransaksi = idTransaksi;
        this.user = user;
        this.tanggalPinjam = tanggalPinjam;
    }

    public String getIdTransaksi() { return idTransaksi; }
    public User getUser() { return user; }
    public String getTanggalPinjam() { return tanggalPinjam; }
    public String TanggalKembali() { return tanggalKembali; }

    public void setTanggalKembali(String tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }
}
