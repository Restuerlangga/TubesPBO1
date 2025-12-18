import java.util.*;


public class Main { 
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        while(true){
            System.out.println("=================================");
            System.out.println("Inventaris Barang Console App");
            System.out.println("1. Login (Jika udah punya akun)");
            System.out.println("2. Register (jika belum ada akun)");
            System.out.println("3. Lihat Barang (Tanpa Login)");
            System.out.println("4. Keluar Aplikasi");
            System.out.println("Pilih Menu :");
            System.out.println("=================================");

            String menu = input.nextLine();

            switch (menu) {
                case "1":
                    handleLogin();
                    break;
                case "2" :
                    handleRegister();
                    break;
                case "3" :
                    handleGustMode();
                    break;
                case "4" :
                    handleOut();
            }
        }
    }
}
