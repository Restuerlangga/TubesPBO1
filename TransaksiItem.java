public class TransaksiItem {
    private Barang barang;
    private int jumlah;

    public TransaksiItem(Barang barang, int jumlah) {
        this.barang = barang;
        this.jumlah = jumlah;
    }

    public Barang getBarang() { 
        return barang; 
    }

    public int getJumlah() { 
        return jumlah; 
    }

    public void setJumlah(int jumlah) { 
        this.jumlah = jumlah; 
    }
}
