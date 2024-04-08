import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

// Kelas FlappyBird adalah JPanel yang mengimplementasikan ActionListener, KeyListener, dan MouseListener
public class FlappyBird extends JPanel implements ActionListener, KeyListener, MouseListener {

    // Enum untuk menentukan status permainan
    private enum GameState {
        MAIN_MENU,
        PLAYING,
        GAME_OVER
    }

    // Dimensi frame permainan
    private int frameWidth = 360;
    private int frameHeight = 640;

    // Gambar latar belakang, burung, dan pipa
    private Image backgroundImage;
    private Image birdImage;
    private Image lowerPipeImage;
    private Image upperPipeImage;

    // Posisi awal dan ukuran pemain
    private int playerStartPosX = frameWidth / 8;
    private int playerStartPosY = frameHeight / 2;
    private int playerWidth = 34;
    private int playerHeight = 24;
    private Player player;

    // Posisi awal dan ukuran pipa
    private int pipeStartPosX = frameWidth;
    private int pipeStartPosY = 0;
    private int pipeWidth = 64;
    private int pipeHeight = 512;
    private ArrayList<Pipe> pipes;

    private Timer gameloop; // Timer untuk perulangan permainan
    private Timer pipesCooldown; // Timer untuk cooldown penempatan pipa

    private int gravity = 1; // Gravitasi permainan

    private GameState gameState; // Status permainan

    private int score = 0; // Skor permainan

    private boolean gameStarted = false; // Menandakan apakah permainan sudah dimulai

    // Konstruktor FlappyBird
    public FlappyBird() {
        // Mengatur preferensi dimensi JPanel
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this); // Menambahkan mouse listener

        // Menginisialisasi gambar latar belakang, burung, dan pipa
        backgroundImage = new ImageIcon(getClass().getResource("Assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("Assets/bintang.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("Assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("Assets/upperPipe.png")).getImage();

        // Menginisialisasi pemain dan daftar pipa
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<>();

        // Mengatur cooldown penempatan pipa dan timer perulangan permainan
        pipesCooldown = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        pipesCooldown.start();
        gameloop = new Timer(1000 / 60, this);
        gameState = GameState.MAIN_MENU; // Mengatur status awal permainan ke menu utama
    }

    // Metode paintComponent untuk menggambar elemen-elemen permainan
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Metode untuk menggambar elemen permainan
    public void draw(Graphics g) {
        if (gameState == GameState.MAIN_MENU) {
            drawMainMenu(g);
        } else {
            // Menggambar latar belakang
            g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

            // Menggambar pemain
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

            // Menggambar pipa
            for (Pipe pipe : pipes) {
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }

            // Menggambar skor
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score / 2, 10, 30);

            // Jika permainan berakhir, tampilkan teks "Game Over" dan instruksi untuk mengulang
            if (gameState == GameState.GAME_OVER) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                String gameOverText = "Game Over";
                g.drawString(gameOverText, frameWidth / 2 - g.getFontMetrics().stringWidth(gameOverText) / 2, frameHeight / 2 - 20);
                String restartText = "Press 'R' to restart";
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString(restartText, frameWidth / 2 - g.getFontMetrics().stringWidth(restartText) / 2, frameHeight / 2 + 20);
            }
        }
    }

    // Metode untuk menggambar menu utama
    private void drawMainMenu(Graphics g) {
        // Menggambar latar belakang
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String titleText = "Flappy Bird";
        String subText = "By : Taufiq";
        g.drawString(titleText, frameWidth / 2 - g.getFontMetrics().stringWidth(titleText) / 2, frameHeight / 4);
        g.drawString(subText, frameWidth / 2 - g.getFontMetrics().stringWidth(titleText) / 2, frameHeight / 4 + 50);

        // Menggambar tombol mulai
        g.setColor(Color.GREEN);
        g.fillRect(frameWidth / 2 - 50, frameHeight / 2 + 20, 100, 50);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String startText = "Start";
        g.drawString(startText, frameWidth / 2 - g.getFontMetrics().stringWidth(startText) / 2, frameHeight / 2 + 50);
    }


    // Metode untuk menempatkan pipa pada posisi acak
    public void placePipes() {
        if (gameState == GameState.PLAYING) {
            int randompipeStartPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
            int openingspace = frameHeight / 4;
            Pipe upperPipe = new Pipe(pipeStartPosX, randompipeStartPosY, pipeWidth, pipeHeight, upperPipeImage);
            pipes.add(upperPipe);

            Pipe lowerPipe = new Pipe(pipeStartPosX, randompipeStartPosY + pipeHeight + openingspace, pipeWidth, pipeHeight, lowerPipeImage);
            pipes.add(lowerPipe);
        }
    }

    // Metode untuk menggerakkan elemen permainan
    public void move() {
        if (gameState == GameState.PLAYING) {
            // Menerapkan gravitasi pada pemain
            player.setVelocityY(player.getVelocityY() + gravity);
            player.setPosY(player.getPosY() + player.getVelocityY());
            player.setPosY(Math.max(player.getPosY(), 0)); // Memastikan pemain tidak melewati batas atas

            // Memeriksa tabrakan dengan pipa
            for (Pipe pipe : pipes) {
                if (player.getPosX() + player.getWidth() > pipe.getPosX() && player.getPosX() < pipe.getPosX() + pipe.getWidth()) {

                    // Pipa bawah
                    if (player.getPosY() + player.getHeight() > pipe.getPosY() && player.getPosY() + player.getHeight() < pipe.getPosY() + pipe.getHeight() && pipe.getImage() == lowerPipeImage) {
                        gameOver();
                        return;
                    }

                    // Pipa atas
                    if (player.getPosY() < pipe.getPosY() + pipe.getHeight() && pipe.getImage() == upperPipeImage) {
                        gameOver();
                        return;
                    }
                }
            }

            // Memeriksa jika pemain mencapai batas bawah
            if (player.getPosY() + player.getHeight() >= frameHeight) {
                // Pemain mencapai batas bawah, game over
                gameOver();
                return;
            }

            // Memindahkan pipa
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());
                if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                    pipe.setPassed(true);
                    score++;
                }
            }
        }
    }

    // Metode actionPerformed untuk menangani peristiwa saat Timer dipicu
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    // Metode yang diimplementasikan dari antarmuka KeyListener
    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Metode yang diimplementasikan dari antarmuka KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameState == GameState.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                player.setVelocityY(-10);
            }
        } else if (gameState == GameState.GAME_OVER && e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
    }

    // Metode yang diimplementasikan dari antarmuka KeyListener
    @Override
    public void keyReleased(KeyEvent e) {
    }

    // Metode yang diimplementasikan dari antarmuka MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (gameState == GameState.MAIN_MENU && mouseX >= frameWidth / 2 - 50 && mouseX <= frameWidth / 2 + 50
                && mouseY >= frameHeight / 2 + 20 && mouseY <= frameHeight / 2 + 70) {
            startGame(); // Memulai permainan jika tombol mulai diklik
        }
    }

    // Metode yang diimplementasikan dari antarmuka MouseListener
    @Override
    public void mousePressed(MouseEvent e) {
    }

    // Metode yang diimplementasikan dari antarmuka MouseListener
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // Metode yang diimplementasikan dari antarmuka MouseListener
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    // Metode yang diimplementasikan dari antarmuka MouseListener
    @Override
    public void mouseExited(MouseEvent e) {
    }

    // Metode untuk menampilkan layar game over
    private void gameOver() {
        gameState = GameState.GAME_OVER;
        gameloop.stop();
    }

    // Metode untuk mengulang permainan
    private void restartGame() {
        gameState = GameState.PLAYING;
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        pipes.clear();
        score = 0;
        gameloop.start();
    }

    // Metode untuk memulai permainan
    private void startGame() {
        gameState = GameState.PLAYING;
        gameStarted = true;
        pipes.clear();
        score = 0;
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        gameloop.start();
    }
}