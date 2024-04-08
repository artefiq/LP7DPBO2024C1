import java.awt.*;

// Kelas Player merepresentasikan pemain dalam permainan Flappy Bird
public class Player {
    private int posX; // posisi x pemain
    private int posY; // posisi y pemain
    private int width; // lebar pemain
    private int height; // tinggi pemain
    private Image image; // gambar pemain
    private int velocityY; // kecepatan vertikal pemain

    // Konstruktor untuk membuat objek Pemain
    public Player(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;
        this.velocityY = -0; // kecepatan awal pemain (diam)
    }

    // Metode getter dan setter untuk atribut-atribut Pemain
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

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }
}
