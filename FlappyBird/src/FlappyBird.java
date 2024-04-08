import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener, MouseListener {

    private enum GameState {
        MAIN_MENU,
        PLAYING,
        GAME_OVER
    }

    private int frameWidth = 360;
    private int frameHeight = 640;

    private Image backgroundImage;
    private Image birdImage;
    private Image lowerPipeImage;
    private Image upperPipeImage;

    // Player
    private int playerStartPosX = frameWidth / 8;
    private int playerStartPosY = frameHeight / 2;
    private int playerWidth = 34;
    private int playerHeight = 24;
    private Player player;

    // Pipe
    private int pipeStartPosX = frameWidth;
    private int pipeStartPosY = 0;
    private int pipeWidth = 64;
    private int pipeHeight = 512;
    private ArrayList<Pipe> pipes;

    private Timer gameloop;
    private Timer pipesCooldown;

    private int gravity = 1;

    private GameState gameState;

    private int score = 0;

    private boolean gameStarted = false; // Indicates whether the game has started

    public FlappyBird() {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this); // Add mouse listener

        backgroundImage = new ImageIcon(getClass().getResource("Assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("Assets/bintang.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("Assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("Assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<>();

        pipesCooldown = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        pipesCooldown.start();
        gameloop = new Timer(1000 / 60, this);
        gameState = GameState.MAIN_MENU; // Set initial game state to main menu
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (gameState == GameState.MAIN_MENU) {
            drawMainMenu(g);
        } else {
            g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

            for (Pipe pipe : pipes) {
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score / 2, 10, 30);

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

    private void drawMainMenu(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String titleText = "Flappy Bird";
        String subText = "By : Taufiq";
        g.drawString(titleText, frameWidth / 2 - g.getFontMetrics().stringWidth(titleText) / 2, frameHeight / 4);
        g.drawString(subText, frameWidth / 2 - g.getFontMetrics().stringWidth(titleText) / 2, frameHeight / 4 + 50);

        // Draw start button
        g.setColor(Color.GREEN);
        g.fillRect(frameWidth / 2 - 50, frameHeight / 2 + 20, 100, 50);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String startText = "Start";
        g.drawString(startText, frameWidth / 2 - g.getFontMetrics().stringWidth(startText) / 2, frameHeight / 2 + 50);
    }


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

    public void move() {
        if (gameState == GameState.PLAYING) {
            // Apply gravity to the player
            player.setVelocityY(player.getVelocityY() + gravity);
            player.setPosY(player.getPosY() + player.getVelocityY());
            player.setPosY(Math.max(player.getPosY(), 0)); // Ensure the player does not go above the top border

            // Check collision with pipes
            for (Pipe pipe : pipes) {
                if (player.getPosX() + player.getWidth() > pipe.getPosX() && player.getPosX() < pipe.getPosX() + pipe.getWidth()) {

                    // lower pipe
                    if (player.getPosY() + player.getHeight() > pipe.getPosY() && player.getPosY() + player.getHeight() < pipe.getPosY() + pipe.getHeight() && pipe.getImage() == lowerPipeImage) {
                        gameOver();
                        return;
                    }

                    // upper pipe
                    if (player.getPosY() < pipe.getPosY() + pipe.getHeight() && pipe.getImage() == upperPipeImage) {
                        gameOver();
                        return;
                    }
                }
            }

            // Check if the player hits the bottom border
            if (player.getPosY() + player.getHeight() >= frameHeight) {
                // Player hit the bottom border, game over
                gameOver();
                return;
            }

            // Move pipes
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

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

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

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (gameState == GameState.MAIN_MENU && mouseX >= frameWidth / 2 - 50 && mouseX <= frameWidth / 2 + 50
                && mouseY >= frameHeight / 2 + 20 && mouseY <= frameHeight / 2 + 70) {
            startGame(); // Start the game if the start button is clicked
        }
    }

    // Other mouse listener methods (not used, can be left empty)

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void gameOver() {
        gameState = GameState.GAME_OVER;
        gameloop.stop();
    }

    private void restartGame() {
        gameState = GameState.PLAYING;
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        pipes.clear();
        score = 0;
        gameloop.start();
    }

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