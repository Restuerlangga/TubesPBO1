public abstract class Akun {

    protected int id;
    protected String nama;
    protected String username;
    protected String password;

    public Akun(int id, String nama, String username, String password) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
    }

    public abstract void login();

    public void logout() {
        System.out.println(nama + " berhasil logout.");
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }
}
