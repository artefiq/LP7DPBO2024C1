import javax.swing.*;

// Kelas App adalah kelas utama yang berfungsi sebagai entry point program
public class App {
    public static void main(String[] args) {
        // Membuat objek JFrame untuk menampung permainan Flappy Bird
        JFrame frame = new JFrame("Flappy Bird");

        // Mengatur operasi default saat menutup frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Mengatur dimensi frame permainan
        frame.setSize(360, 640);

        // Menempatkan frame ke tengah layar
        frame.setLocationRelativeTo(null);

        // Menonaktifkan kemampuan perubahan ukuran frame
        frame.setResizable(false);

        // Membuat objek FlappyBird yang merupakan JPanel yang berisi permainan
        FlappyBird flappyBird = new FlappyBird();

        // Menambahkan objek FlappyBird ke dalam frame
        frame.add(flappyBird);

        // Mengatur ukuran frame agar sesuai dengan isi FlappyBird
        frame.pack();

        // Memfokuskan keyboard ke FlappyBird untuk menerima input
        flappyBird.requestFocus();

        // Menampilkan frame ke layar
        frame.setVisible(true);
    }
}
