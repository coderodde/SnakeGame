package net.coderodde.snake;

import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author rodde
 */
public class SnakeFrame extends JFrame {

    private final SnakePanel snakePanel = new SnakePanel(10, 10, 3);
    
    public SnakeFrame() {
        getContentPane().add(snakePanel);
        getContentPane().setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        pack();
        setResizable(true);
        setVisible(true);
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeFrame();
        });
    }
}
