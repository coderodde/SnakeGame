package net.coderodde.snake;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
        snakePanel.setFocusable(true);
        getContentPane().setPreferredSize(
                Toolkit.getDefaultToolkit().getScreenSize());
        pack();
        setResizable(true);
        setVisible(true);
        repaint();
    }
    
    public Snake getSnake() {
        return snakePanel.getSnake();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeFrame snakeFrame = new SnakeFrame();
            snakeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
