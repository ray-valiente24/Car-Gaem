package main;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.io.*;

public class GamePanel extends JPanel {

    private Image background;
    private Image player;

    private int frames = 0;
    private long lastTime = System.nanoTime();
    private double fps = 0.0;

    private int playerX = 100;
    private int playerY = 100;
    private int speed = 5; // default speed
    private boolean boost = false;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private boolean showSplash = true;
    private String splashText = "Welcome!";

    // ===== BACKGROUND MUSIC =====
    private Clip backgroundMusic;

    public GamePanel() {
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        this.setDoubleBuffered(true);

        // ===== LOAD IMAGES =====
        this.player = new ImageIcon(this.getClass().getResource("/assets/player.png")).getImage();
        this.background = new ImageIcon(this.getClass().getResource("/assets/map.png")).getImage();

        // ===== SPLASH TEXTS =====
        InputStream splashStream = getClass().getResourceAsStream("/assets/splash_texts.txt");
        if (splashStream != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(splashStream))) {
                java.util.List<String> lines = new java.util.ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) lines.add(line.trim());
                }

                if (!lines.isEmpty()) {
                    Random rand = new Random();
                    splashText = lines.get(rand.nextInt(lines.size()));
                } else {
                    splashText = "Welcome!"; // fallback if file empty
                }

            } catch (IOException e) {
                e.printStackTrace();
                splashText = "Welcome!"; // fallback if read fails
            }
        } else {
            System.err.println("Warning: splash_texts.txt not found in resources!");
            splashText = "Welcome!"; // fallback if file not found
        }

        // ===== PLAY BACKGROUND MUSIC =====
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/assets/music1.wav");
            if (audioSrc == null) {
                throw new RuntimeException("Audio file not found!");
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // loops forever
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== TOP BAR BACK BUTTON =====
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setOpaque(false);
        JButton backButton = new JButton("< Retum");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setFocusPainted(false);
        topBar.add(backButton);
        this.add(topBar, BorderLayout.NORTH);

        backButton.addActionListener((e) -> this.onBack());

        // ===== KEY LISTENER =====
        this.addKeyListener(new KeyAdapter() {
            {
                Objects.requireNonNull(GamePanel.this);
            }

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 65 -> GamePanel.this.left = true;   // A
                    case 68 -> GamePanel.this.right = true;  // D
                    case 83 -> GamePanel.this.down = true;   // S
                    case 87 -> GamePanel.this.up = true;     // W
                    case 74 -> GamePanel.this.boost = true; // J
                }
            }

            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 65 -> GamePanel.this.left = false;
                    case 68 -> GamePanel.this.right = false;
                    case 83 -> GamePanel.this.down = false;
                    case 87 -> GamePanel.this.up = false;
                    case 74 -> GamePanel.this.boost = false; // J
                }
            }
        });

        // ===== GAME LOOP (60 FPS) =====
        new Timer(16, (e) -> this.update()).start();

        // SPLASH TIMER: Hides after 3 seconds
        new Timer(3000, e -> showSplash = false).start();
    }

    private void update() {

        int moveSpeed = boost ? speed * 2 : speed;

        if (this.up) this.playerY -= moveSpeed;
        if (this.down) this.playerY += moveSpeed;
        if (this.left) this.playerX -= moveSpeed;
        if (this.right) this.playerX += moveSpeed;

        // prevent moving out of window
        if (this.playerX < 0) this.playerX = 0;
        if (this.playerY < 0) this.playerY = 0;
        if (this.playerX > this.getWidth() - 64) this.playerX = this.getWidth() - 64;
        if (this.playerY > this.getHeight() - 64 - 20) this.playerY = this.getHeight() - 64 - 20;

        this.repaint();
        ++this.frames;
        long currentTime = System.nanoTime();
        if (currentTime - this.lastTime >= 1_000_000_000L) {
            this.fps = this.frames;
            this.frames = 0;
            this.lastTime = currentTime;
            System.out.println("> " + (int)this.fps + " FPS");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ===== DRAW BACKGROUND =====
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        // ===== DRAW SPLASH TEXT (ONLY AT START) =====
        if (showSplash) {
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.setColor(Color.magenta); // splash text color
            g.drawString(splashText, getWidth() / 2 - g.getFontMetrics().stringWidth(splashText) / 2, 100);
        }

        // ===== DRAW PLAYER =====
        g.drawImage(player, playerX, playerY, 144, 48, this);
    }

    protected void onBack() {
        // stop music when back is pressed
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
}