package GUI;

//UserInterface.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel {
    public void ShowMainPanel() {
        JFrame frame = new JFrame("SAMT Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        // Tworzenie niestandardowego paska tytułowego
        JPanel titleBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Ustawienie efektu cienia
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());  // Czarny prostokąt

                // Cień pod paskiem
                g2d.setColor(new Color(0, 0, 0, 100)); // Półprzezroczysty cień
                g2d.fillRoundRect(0, getHeight() - 5, getWidth(), 5, 5, 5);  // Cień na dole
            }
        };

        titleBar.setPreferredSize(new Dimension(frame.getWidth(), 50));
        titleBar.setBackground(Color.BLACK);

        // Tworzenie napisu w lewym górnym rogu
        JLabel titleLabel = new JLabel("SAMT CONVERTER");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Inter", Font.ITALIC, 18));


        // Dodanie napisu na pasek
        titleBar.add(titleLabel, BorderLayout.WEST);

        // Dodanie paska tytułowego do okna
        frame.add(titleBar, BorderLayout.NORTH);

        // Główny panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Wyświetlenie okna
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
