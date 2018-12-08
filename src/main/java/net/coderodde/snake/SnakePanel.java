package net.coderodde.snake;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;
import javax.swing.JPanel;

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
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        int aux1 = resolution.width - (gridWidth + 1) * gridThickness;
        int aux2 = resolution.height - (gridHeight + 1) * gridThickness;
        int aux1a = aux1 / gridWidth;
        int aux2a = aux2 / gridHeight;
        int cellWidthHeight = Math.min(aux1a, aux2a);
        this.grid = new GridCell[gridHeight][gridWidth];
        this.setBackground(DEFAULT_WINDOW_BACKGROUND_COLOR);
        
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                Component component = componentEvent.getComponent();
                screenResolution.width = component.getWidth();
                screenResolution.height = component.getHeight();
                component.repaint();
            }
        });
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
        
        g.setColor(Color.ORANGE);
        g.fillRect(10, 800, 100, 100);
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
