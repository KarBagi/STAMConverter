package GUI;

import javax.swing.*;
import java.awt.*;

public class ShowStatisticPanel {
    public static void showPanel(JFrame parentFrame) {
        JPanel newPanel = new JPanel(new GridBagLayout());
        JButton backButton = new JButton("Powrót");
        backButton.setPreferredSize(new Dimension(100, 50));

        newPanel.add(backButton);

        backButton.addActionListener(e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.getContentPane().revalidate();
            parentFrame.getContentPane().repaint();
            parentFrame.getContentPane().add(new JPanel());
            parentFrame.pack();
        });

        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(newPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
