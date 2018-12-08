package net.coderodde.snake;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getScreenDevices()[0];
        System.out.println(gd.getDefaultConfiguration().getBounds());
//        System.exit(0);
        int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
//        System.exit(0);
        System.out.println("yeah; " + dpi + " " + Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        System.out.println(Toolkit.getDefaultToolkit().getScreenSize());
        getContentPane().add(snakePanel);
        getContentPane().setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        pack();
        setResizable(true);
        setVisible(true);
        repaint();
    }
    
    public static void main(String[] args) {
        new SnakeFrame();
        SwingUtilities.invokeLater(() -> {
        });
    }
}
