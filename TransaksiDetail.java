public class TransaksiDetail{
    private Barang barang;
    private int jumlah; //jumlah barang  
    private double subtotal;

    public TransaksiDetail(Barang barang, int jumlah) {
        this.barang = barang;
        this.jumlah = jumlah;
        this.subtotal = barang.getHargaSewa() * jumlah;
    }

    public Barang getBarang() {
        return barang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        // Contoh output: "Laptop ROG (x2) : Rp 300.000"
        return String.format("%-20s (x%d) : Rp %,.0f", 
                barang.getNamaBarang(), 
                jumlah, 
                subtotal);
    }
}