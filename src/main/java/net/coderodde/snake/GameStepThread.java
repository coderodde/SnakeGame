package net.coderodde.snake;

import java.awt.Point;
import java.util.Objects;

/**
 * This class defines a thread that moves the snake in its current direction.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 7, 2018)
 */
public final class GameStepThread extends Thread {
    
    /**
     * The default step duration in milliseconds.
     */
    private static final long DEFAULT_STEP_DURATION = 1000L; // 1 second.
    
    /**
     * The minimum allowed step duration in milliseconds.
     */
    private static final long MINIMUM_STEP_DURATION = 100L;  // 0,1 seconds.
    
    /**
     * The snake being moved. 
     */
    private final Snake snake;
    
    /**
     * The game grid.
     */
    private final GridCell[][] grid;
    
    /**
     * The current step duration.
     */
    private volatile long stepDuration;
    
    /**
     * The flag for requesting the thread to exit.
     */
    private volatile boolean halt;
    
    /**
     * The flag for requesting a pause.
     */
    private volatile boolean pause;
    
    private SnakePanel snakePanel;
    
    /**
     * Constructs this game loop thread.
     * 
     * @param snake the snake to control..
     */
    public GameStepThread(Snake snake, 
                          GridCell[][] grid, 
                          SnakePanel snakePanel) {
        this.snake = Objects.requireNonNull(snake, "The input snake is null.");
        this.grid  = Objects.requireNonNull(grid, 
                                            "The input game grid is null.");
        this.snakePanel = Objects.requireNonNull(
                snakePanel, 
                "The input game panel is null.");
        
        this.stepDuration = DEFAULT_STEP_DURATION;
        // this.halt andn this.pause is set to false by default.
    }
    
    public void setStepDuration(long stepDuration) {
        this.stepDuration = checkStepDuration(stepDuration);
    }
    
    public void requestExit() {
        halt = true;
    }
    
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    
    @Override
    public void run() {
        while (!halt) {
            try {
                Thread.sleep(stepDuration);
            } catch (InterruptedException ex) {
                throw new RuntimeException("Thread.sleep() threw an exception.",
                                           ex);
            }
            
            if (pause) {
                continue;
            }
            
            try {
                Point snakeHeadCoordinates = snake.getSnakeHeadCoordinates();
                Point berryCoordiinates = snakePanel.getCurrentBerryPoint();
                
                if (snakeHeadCoordinates.equals(berryCoordiinates)) {
                    snake.grow();
                    snakePanel.createNewBerry();
                }
                
                snake.makeStep(grid);
                snakePanel.repaint();
            } catch (WallCollisionException ex) {
                return;
            } catch (EatHimselfException ex) {
                return;
            }
        }
    }
    
    public void togglePause() {
        this.pause = !this.pause;
    }
    
    private long checkStepDuration(long stepDuration) {
        if (stepDuration < MINIMUM_STEP_DURATION) {
            throw new IllegalArgumentException(
                    "The input step duration is too small (" + stepDuration +
                    " milliseconds). Must be at least " + 
                    MINIMUM_STEP_DURATION + " milliseconds.");
        }
        
        return stepDuration;
    }
}
