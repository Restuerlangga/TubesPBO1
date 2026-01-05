public class Barang {
    private int id;
    private String kodeBarang;
    private String namaBarang;
    private String kategori;
    private int stok;
    private double hargaSewa;

    public Barang(int id, String kodeBarang, String namaBarang, String kategori, int stok, double hargaSewa) {
        this.id = id;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.kategori = kategori;
        this.stok = stok;
        this.hargaSewa = hargaSewa;
    }

    
    public Barang(String kodeBarang, String namaBarang, String kategori, int stok, double hargaSewa) {
        this.id = 0;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.kategori = kategori;
        this.stok = stok;
        this.hargaSewa = hargaSewa;
    }

    // Method Getter
    public int getid() { return id; }
    public String getKodeBarang() { return kodeBarang; }
    public String getNamaBarang() { return namaBarang; }
    public int getStok() { return stok; }
    public double getHargaSewa() { return hargaSewa; }

    @Override
    public String toString() {
        return String.format("[%s] %-20s | Stok: %d | Rp %,.0f/hari", kodeBarang, namaBarang, stok, hargaSewa);
    }
}