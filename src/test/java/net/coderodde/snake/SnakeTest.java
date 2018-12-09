package net.coderodde.snake;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class SnakeTest {
    
    @Test
    public void test() {
        List<SnakeCompartment> snakeCompartmentList = new ArrayList<>();
        snakeCompartmentList.add(new SnakeCompartment(MotionDirection.WEST, 0, 0));
        snakeCompartmentList.add(new SnakeCompartment(MotionDirection.WEST, 1, 0));
        snakeCompartmentList.add(new SnakeCompartment(MotionDirection.WEST, 2, 0));
        GridCell[][] grid = new GridCell[4][4];
        
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x] = GridCell.TRAVERSABLE;
            }
        }
        
        Snake snake = new Snake(snakeCompartmentList, MotionDirection.WEST);
        snake.makeStep(grid);
        
        List<SnakeCompartment> list = snake.getSnakeCompartmentList();
        assertEquals(new SnakeCompartment(MotionDirection.WEST, -1, 0), list.get(0));
        assertEquals(new SnakeCompartment(MotionDirection.WEST, 0, 0), list.get(1));
        assertEquals(new SnakeCompartment(MotionDirection.WEST, 1, 0), list.get(2));
        
        snake.setDirection(MotionDirection.SOUTH);
        snake.makeStep(grid);
        
        list = snake.getSnakeCompartmentList();
        assertEquals(new SnakeCompartment(MotionDirection.SOUTH, -1, 1), list.get(0));
        assertEquals(new SnakeCompartment(MotionDirection.WEST, -1, 0), list.get(1));
        assertEquals(new SnakeCompartment(MotionDirection.WEST, 0, 0), list.get(2));
        
        snake.makeStep(grid);
        
        list = snake.getSnakeCompartmentList();
        assertEquals(new SnakeCompartment(MotionDirection.SOUTH, -1, 2), list.get(0));
        assertEquals(new SnakeCompartment(MotionDirection.SOUTH, -1, 1), list.get(1));
        assertEquals(new SnakeCompartment(MotionDirection.WEST, -1, 0), list.get(2));
    }
}
