import java.awt.*;

// Kelas Pipe merepresentasikan pipa dalam permainan Flappy Bird
public class Pipe {

    private int posX; // posisi x pipa
    private int posY; // posisi y pipa
    private int width; // lebar pipa
    private int height; // tinggi pipa
    private Image image; // gambar pipa
    private int velocityX; // kecepatan horizontal pipa
    boolean passed = false; // status apakah pipa sudah dilewati oleh pemain

    // Konstruktor untuk membuat objek Pipe
    public Pipe(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;
        this.velocityX = -1; // kecepatan default pergerakan pipa ke kiri
        this.passed = false; // pipa belum dilewati saat pertama kali dibuat
    }

    // Metode getter dan setter untuk atribut-atribut Pipa
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
