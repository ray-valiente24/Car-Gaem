package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.util.Scanner;

public class Main {

    private static JFrame frame;
    private static JPanel menuPanel;
    public static GamePanel gamePanel; // now using your GamePanel class

    private static void createWindow() {
        frame = new JFrame("The Car Gaem v. Alpha (Windows)");

        // ===== APPLICATION ICON =====
        frame.setIconImage(
                new ImageIcon(
                        Main.class.getResource("/assets/icon.png")
                ).getImage()
        );

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        createMenuPanel();

        // ===== USE YOUR GAMEPANEL =====
        gamePanel = new GamePanel() {
            @Override
            protected void onBack() {
                switchToMenu();
            }
        };

        frame.setResizable(false);
        frame.setContentPane(menuPanel);
        frame.setVisible(true);
    }

    // ===== MENU PANEL =====
    private static void createMenuPanel() {

        menuPanel = new JPanel(new BorderLayout()) {

            Image bg = new ImageIcon(
                    Main.class.getResource("/assets/homebg.png")
            ).getImage();

            Image leftOverlay = new ImageIcon(
                    Main.class.getResource("/assets/wasd.png")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);

                int overlayWidth = 75;
                int overlayHeight = getHeight();
                g.drawImage(leftOverlay, 0, 0, overlayWidth, overlayHeight, this);
            }
        };

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // LOGO
        ImageIcon original = new ImageIcon(
                Main.class.getResource("/assets/logo.png")
        );

        Image scaled = original.getImage().getScaledInstance(650, -1, Image.SCALE_SMOOTH);

        JLabel logoLabel = new JLabel(new ImageIcon(scaled));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // BUTTONS
        JButton playButton = new JButton("Play");
        JButton quitButton = new JButton("Quit");

        playButton.setFont(new Font("Arial", Font.BOLD, 36));
        quitButton.setFont(new Font("Arial", Font.PLAIN, 36));

        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Make buttons longer horizontally
        Dimension buttonSize = new Dimension(300, 45); // width 400, height 80
        playButton.setPreferredSize(buttonSize);
        playButton.setMaximumSize(buttonSize);   // important
        quitButton.setPreferredSize(buttonSize);
        quitButton.setMaximumSize(buttonSize);   // important

        // ADD BUTTONS TO PANEL
        contentPanel.add(logoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(playButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(quitButton);

        centerPanel.add(contentPanel);
        menuPanel.add(centerPanel, BorderLayout.CENTER);

        // BOTTOM-RIGHT COPYRIGHT
        JLabel copyright = new JLabel(
                "Pizeltray Studios - All Rights Reserved 2026"
        );
        copyright.setFont(new Font("Arial", Font.BOLD, 12));
        copyright.setForeground(Color.LIGHT_GRAY);
        copyright.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        bottomPanel.add(copyright, BorderLayout.EAST);

        menuPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ACTIONS
        playButton.addActionListener(e -> switchToGame());
        quitButton.addActionListener(e -> System.exit(0));

        // HOVER + HAND CURSOR EFFECTS
        addHoverEffect(playButton, Color.LIGHT_GRAY, Color.BLACK);
        addHoverEffect(quitButton, Color.LIGHT_GRAY, Color.BLACK);
    }

    // ===== SCREEN SWITCHING =====
    private static void switchToGame() {
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow()); // ensure keys work
    }

    private static void switchToMenu() {
        frame.setContentPane(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    // ===== HOVER EFFECT HELPER =====
    private static void addHoverEffect(JButton button, Color hoverBackground, Color hoverForeground) {
        Color originalBg = button.getBackground();
        Color originalFg = button.getForeground();

        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBackground);
                button.setForeground(hoverForeground);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
                button.setForeground(originalFg);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createWindow);
        System.out.println("Pizeltray Studios - All Rights Reserved 2026\n");
    }
}