package net.coderodde.snake;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javax.swing.JPanel;
import static net.coderodde.snake.MotionDirection.EAST;
import static net.coderodde.snake.MotionDirection.WEST;

/**
 *
 * @author rodde
 */
public class SnakePanel extends JPanel {
    
    /**
     * The color of the traversable grid cell.
     */
    private static final Color DEFAULT_WINDOW_BACKGROUND_COLOR = Color.BLACK;
    
    /**
     * The color of cell border.
     */
    private static final Color DEFAULT_BORDER_COLOR = Color.DARK_GRAY;
    
    /**
     * The color of the walls.
     */
    private static final Color DEFAULT_WALL_COLOR = Color.GRAY;
    
    /**
     * The color of the snake.
     */
    private static final Color DEFAULT_SNAKE_COLOR = Color.WHITE;
    
    /**
     * The color of berries.
     */
    private static final Color DEFAULT_BERRY_COLOR = Color.RED;
    
    /**
     * The thickness of the grid lines in pixels.
     */
    private static final int DEFAULT_GRID_THICKNESS = 1;
    
    /**
     * The minimum width of the grid in cells.
     */
    private static final int MINIMUM_GRID_WIDTH = 10;
    
    /**
     * The minimum height of the grid in cells.
     */
    private static final int MINIMUM_GRID_HEIGHT = 10;
    
    /**
     * The resolution of the user's screen.
     */
    private final Dimension screenResolution;
    
    private Dimension resolution;
    private int gridWidth;
    private int gridHeight;
    private int gridThickness;
    private GameStepThread gameStepThread;
    private GridCell[][] grid;
    private Snake snake;
    
    /**
     * The current window background color. Or in another words, the color of
     * traversable grid cells not occupied by the snake or a berry.
     */
    private Color windowBackgroundColor = DEFAULT_WINDOW_BACKGROUND_COLOR;
    
    /**
     * The current color of the border separating the grid cells.
     */
    private Color borderColor = DEFAULT_BORDER_COLOR;
    
    /**
     * The current color of non-traversable grid cells.
     */
    private Color wallColor = DEFAULT_WALL_COLOR;
    
    /**
     * The current color of the snake.
     */
    private Color snakeColor = DEFAULT_SNAKE_COLOR;
    
    /**
     * The current color of the grid cell occupied by a berry.
     */
    private Color berryColor = DEFAULT_BERRY_COLOR;
    
    /**
     * Specifies the thickness of the grid lines in pixels.
     */
    private int gridLineThickness;
    
    /**
     * The coordinates of a berry.
     */
    private Point currentBerryPoint;
    
    private final Random random = new Random();
    
    /**
     * Specifies the current game state.
     */
    private enum GameState {
        GAMING,
        PAUSE,
        STOP,
    }
    
    private GameState gameState = GameState.STOP;
    
    /**
     * Constructs a new panel for displaying the game grid.
     * 
     * @param gridWidth         the width of the grid in cells.
     * @param gridHeight        the height of the grid in cells.
     * @param gridLineThickness the thickness of the grid lines.
     */
    public SnakePanel(int gridWidth, int gridHeight, int gridLineThickness) {
        this.gridWidth  = checkGridWidth(gridWidth);
        this.gridHeight = checkGridHeight(gridHeight);
        this.gridLineThickness = checkGridLineThickness(gridLineThickness);
        this.grid = getGrid(gridWidth, gridHeight);
        this.screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBackground(DEFAULT_WINDOW_BACKGROUND_COLOR);
        
        List<SnakeCompartment> snakeCompartmentList = 
                Arrays.asList(new SnakeCompartment(EAST, 3, 0),
                              new SnakeCompartment(EAST, 2, 0),
                              new SnakeCompartment(EAST, 1, 0),
                              new SnakeCompartment(EAST, 0, 0));
        
        this.snake = new Snake(snakeCompartmentList, EAST);
        GameStepThread gameStepThread = new GameStepThread(snake,
                                                           this.grid, 
                                                           this);
        gameStepThread.setStepDuration(1000L);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                Component component = componentEvent.getComponent();
                screenResolution.width = component.getWidth();
                screenResolution.height = component.getHeight();
                component.repaint();
            }
        });
        
        addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        snake.setDirection(MotionDirection.NORTH);
                        break;

                    case KeyEvent.VK_RIGHT:
                        snake.setDirection(MotionDirection.EAST);
                        break;

                    case KeyEvent.VK_DOWN:
                        snake.setDirection(MotionDirection.SOUTH);
                        break;

                    case KeyEvent.VK_LEFT:
                        snake.setDirection(MotionDirection.WEST);
                        break;

                    case KeyEvent.VK_SPACE:
                        gameStepThread.togglePause();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
//        SnakeGameKeyListener keyListener =
//                new SnakeGameKeyListener(snake, gameStepThread);
//        
//        this.addKeyListener(keyListener);
        this.currentBerryPoint = createBerry();
        gameStepThread.setPause(false);
        gameStepThread.start();
    }
    
    public Snake getSnake() {
        return this.snake;
    }
    
    public Point getCurrentBerryPoint() {
       return new Point(currentBerryPoint);
    }
    
    public void setSnake(Snake snake) {
        this.snake = Objects.requireNonNull(snake, "The input snake is null.");
    }
    
    /**
     * Creates an empty game grid, which is simply a two-dimensional array of
     * cells.
     * 
     * @param gridWidth  the width of the grid in cells.
     * @param gridHeight the height of the grid in cells.
     * @return the game grid.
     */
    private GridCell[][] getGrid(int gridWidth, int gridHeight) {
        GridCell[][] grid = new GridCell[gridHeight][gridWidth];
        
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                grid[y][x] = GridCell.TRAVERSABLE;
            }
        }
        
        return grid;
    }
    
    @Override
    public void paint(Graphics g) {
        update(g);
    }
    
    private int getCellLength() {
        int auxCellWidth  = screenResolution.width;
        int auxCellHeight = screenResolution.height;
        
        auxCellWidth -= (gridWidth + 1) * gridLineThickness;
        auxCellWidth /= gridWidth;
        
        auxCellHeight -= (gridHeight + 1) * gridLineThickness;
        auxCellHeight /= gridHeight;
        
        return Math.min(auxCellWidth, auxCellHeight);
    }
    
    @Override
    public void update(Graphics g) {
        // Clear the grid.
        g.setColor(windowBackgroundColor);
        g.fillRect(0, 
                   0,
                   screenResolution.width, 
                   screenResolution.height);
        int cellLength = getCellLength();
        
        // Draw the grid lines.
        int horizontalLeftoverPixels = 
                screenResolution.width - (gridWidth + 1) * gridLineThickness
                                       -  gridWidth * cellLength;
        
        int verticalLeftoverPixels = 
                screenResolution.height - (gridHeight + 1) * gridLineThickness
                                        -  gridHeight * cellLength;
        
        int skipHorizontal = horizontalLeftoverPixels / 2;
        int skipVertical   = verticalLeftoverPixels  /  2;
        
        g.setColor(borderColor);
        
        // Draw vertical grid lines.
        for (int i = 0; i != gridWidth + 1; i++) {
            g.fillRect(skipHorizontal + i * (cellLength + gridLineThickness), 
                       skipVertical, 
                       gridLineThickness,
                       screenResolution.height - verticalLeftoverPixels);
        }
        
        // Draw horizontal grid lines.
        for (int i = 0; i != gridHeight + 1; i++) {
            g.fillRect(skipHorizontal,
                       skipVertical + i * (cellLength + gridLineThickness),
                       screenResolution.width - horizontalLeftoverPixels,
                       gridLineThickness);
        }
        
        // Draw the berry.
        g.setColor(berryColor);
        int x = currentBerryPoint.x;
        int y = currentBerryPoint.y;
        g.fillRect(skipHorizontal + gridLineThickness 
                                  + x * (gridLineThickness + cellLength),
                   skipVertical + gridLineThickness
                                + y * (gridLineThickness + cellLength),
                   cellLength,
                   cellLength);
        
        // Draw the snake.
        g.setColor(snakeColor);
        
        for (SnakeCompartment snakeCompartment : snake) {
            x = snakeCompartment.x;
            y = snakeCompartment.y;
            g.fillRect(
                    skipHorizontal + gridLineThickness 
                                   + x * (cellLength + gridLineThickness),
                    skipVertical + gridLineThickness
                                 + y * (cellLength + gridLineThickness),
                    cellLength, 
                    cellLength);
        }
    }
    
    public void createNewBerry() {
        this.currentBerryPoint = createBerry();
    }
    
    private int checkGridWidth(int gridWidth) {
        if (gridWidth < MINIMUM_GRID_WIDTH) {
            throw new IllegalArgumentException(
                    "The requested grid width is too small (" + gridWidth + 
                    "). Must be at least " + MINIMUM_GRID_WIDTH + ".");
        }
        
        return gridWidth;
    }
    
    private int checkGridHeight(int gridHeight) {
        if (gridHeight < MINIMUM_GRID_HEIGHT) {
            throw new IllegalArgumentException(
                    "The requested grid height is too small (" + gridHeight +
                    "). Must be at least " + MINIMUM_GRID_HEIGHT + ".");
        }
        
        return gridHeight;
    }

    private int checkGridLineThickness(int gridLineThickness) {
        if (gridLineThickness < 0) {
            throw new IllegalArgumentException(
                    "The input grid line thickness (" + gridLineThickness + 
                    ") may not be negative.");
        }
        
        return gridLineThickness;
    }

    private Point createBerry() {
        Point berryPoint = new Point();
        
        while (true) {
            int x = random.nextInt(gridWidth);
            int y = random.nextInt(gridHeight);
            
            if (this.grid[y][x].equals(GridCell.WALL)) {
                continue;
            }
            
            if (snake.occupiesPoint(x, y)) {
                continue;
            }
            
            berryPoint.x = x;
            berryPoint.y = y;
            return berryPoint;
        }
    }
    
    private static final class SnakeKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyReleased(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private static final class SnakeMouseListener 
            implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            
        }
        
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

        @Override
        public void mouseDragged(MouseEvent e) {
            
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        
        }
    }
}
