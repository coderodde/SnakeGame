package net.coderodde.snake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author rodde
 */
public class SnakeGameKeyListener {

    private final Snake snake;
    private final GameStepThread gameStepThread;
    
    public SnakeGameKeyListener(Snake snake, GameStepThread gameStepThread) {
        this.snake = snake;
        this.gameStepThread = gameStepThread;
    }

    
}
