package net.coderodde.snake;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author rodde
 */
public final class Snake implements Iterable<SnakeCompartment> {
    
    private final Deque<SnakeCompartment> compartmentQueue = new ArrayDeque<>();
    private MotionDirection motionDirection;
    
    public Snake(List<SnakeCompartment> snakeCompartmentList,
                 MotionDirection motionDirection) {
        checkSnakeCompartment(snakeCompartmentList);
        compartmentQueue.addAll(snakeCompartmentList);
        setDirection(motionDirection);
    }
    
    public void setDirection(MotionDirection direction) {
        this.motionDirection = Objects.requireNonNull(
                direction, 
                "The input direction is null.");
    }
    
    public void makeStep(GridCell[][] grid) {
        if (hitsWall(grid)) {
            throw new WallCollisionException();
        }
        
        if (eatsHimself()) {
            throw new EatHimselfException();
        }
        
        SnakeCompartment headSnakeCompartment = compartmentQueue.getFirst();
        SnakeCompartment tailSnakeCompartment = compartmentQueue.removeLast();
        
        tailSnakeCompartment.x = headSnakeCompartment.x;
        tailSnakeCompartment.y = headSnakeCompartment.y;
        
        switch (motionDirection) {
            case NORTH:
                tailSnakeCompartment.y--;
                break;
                
            case EAST:
                tailSnakeCompartment.x++;
                break;
                
            case SOUTH:
                tailSnakeCompartment.y++;
                break;
                
            case WEST:
                tailSnakeCompartment.x--;
                break;
                
            default:
                throw new EnumConstantNotPresentException(
                        MotionDirection.class,
                        motionDirection.name());
        }
        
        tailSnakeCompartment.motionDirection = this.motionDirection;
        compartmentQueue.addFirst(tailSnakeCompartment);
    }
    
    public void grow() {
        SnakeCompartment lastSnakeCompartment = compartmentQueue.getLast();
        SnakeCompartment newSnakeCompartment = 
                new SnakeCompartment(lastSnakeCompartment);
        
        switch (lastSnakeCompartment.motionDirection) {
            case NORTH:
                newSnakeCompartment.y++;
                break;
                
            case EAST:
                newSnakeCompartment.x--;
                break;
                
            case SOUTH:
                newSnakeCompartment.y--;
                break;
                
            case WEST:
                newSnakeCompartment.x++;
                break;
        }
        
        newSnakeCompartment.motionDirection =
                lastSnakeCompartment.motionDirection;
        compartmentQueue.addLast(newSnakeCompartment);
    }
    
    public Point getSnakeHeadCoordinates() {
        return new Point(compartmentQueue.getFirst().x,
                         compartmentQueue.getFirst().y);
    }
    
    public boolean hitsWall(GridCell[][] grid) {
        switch (motionDirection) {
            case NORTH:
                return hitsWallMovingToNorth(grid);
                
            case EAST:
                return hitsWallMovingToEast(grid);
                
            case SOUTH:
                return hitsWalMovingToSouth(grid);
                
            case WEST:
                return hitsWalMovingToWest(grid);
                
            default:
                throw new IllegalStateException("Should never get here.");
        }
    }
    
    public boolean eatsHimself() {
        switch (motionDirection) {
            case NORTH:
                return eatsHimselfMovingToNorth();
                
            case EAST:
                return eatsHimselfMovingToEast();
                
            case SOUTH:
                return eatsHimselfMovingToSouth();
                
            case WEST:
                return eatsHimselfMovingToWest();
                
            default:
                throw new EnumConstantNotPresentException(GridCell.class, 
                                                          "Unknown constant.");
        }
    }
    
    @Override
    public Iterator<SnakeCompartment> iterator() {
        return compartmentQueue.iterator();
    }
    
    List<SnakeCompartment> getSnakeCompartmentList() {
        return new ArrayList<>(compartmentQueue);
    }
    
    private void checkSnakeCompartment(
            List<SnakeCompartment> snakeCompartmentList) {
        Objects.requireNonNull(snakeCompartmentList, 
                               "The snake compartment list is null.");
        
        if (snakeCompartmentList.isEmpty()) {
            throw new IllegalArgumentException(
                    "The input snake compartment list is empty.");
        }
        
        for (int i = 0, j = 1; j < snakeCompartmentList.size(); i++, j++) {
            SnakeCompartment snakeCompartment1 = snakeCompartmentList.get(i);
            SnakeCompartment snakeCompartment2 = snakeCompartmentList.get(j);
            int horizontalDifference = Math.abs(snakeCompartment1.x -
                                                snakeCompartment2.x);
            int verticalDifference = Math.abs(snakeCompartment1.y - 
                                              snakeCompartment2.y);
            
            if (horizontalDifference + verticalDifference != 1) {
                throw new IllegalArgumentException(
                        "The snake is disconnected.");
            }
        }
    }
    
    private boolean hitsWallMovingToNorth(GridCell[][] grid) {
        SnakeCompartment headSnakeCompartment = compartmentQueue.getFirst();
        int x = headSnakeCompartment.x;
        int y = headSnakeCompartment.y;
        
        if (y == 0) {
            return true;
        }
        
        if (grid[y - 1][x].equals(GridCell.WALL)) {
            return true;
        }

        return false;
    }
    
    private boolean hitsWallMovingToEast(GridCell[][] grid) {
        SnakeCompartment headSnakeCompartment = compartmentQueue.getFirst();
        int x = headSnakeCompartment.x;
        int y = headSnakeCompartment.y;
 
        if (x == grid[0].length - 1) {
            return true;
        }
        
        if (grid[y][x + 1] == GridCell.WALL) {
            return true;
        }
        
        return false;
    }
    
    private boolean hitsWalMovingToSouth(GridCell[][] grid) {
        SnakeCompartment headSnakeCompartment = compartmentQueue.getFirst();
        int x = headSnakeCompartment.x;
        int y = headSnakeCompartment.y;
        
        if (y == grid.length - 1) {
            return true;
        }
        
        if (grid[y + 1][x] == GridCell.WALL) {
            return true;
        }
        
        return false;
    }
    
    private boolean hitsWalMovingToWest(GridCell[][] grid) {
        SnakeCompartment headSnakeCompartment = compartmentQueue.getFirst();
        int x = headSnakeCompartment.x;
        int y = headSnakeCompartment.y;
        
        if (x == 0) {
            return true;
        }
        
        if (grid[y][x - 1] == GridCell.WALL) {
            return true;
        }
        
        return false;
    }
    
    private boolean eatsHimselfMovingToNorth() {
        SnakeCompartment headSnakeCompartment = compartmentQueue.getFirst();
        int x = headSnakeCompartment.x;
        int y = headSnakeCompartment.y;
        return occupiesPoint(x, y - 1);
    }
    
    private boolean eatsHimselfMovingToEast() {
        SnakeCompartment headSnakeCompartment = compartmentQueue.getFirst();
        int x = headSnakeCompartment.x;
        int y = headSnakeCompartment.y;
        return occupiesPoint(x + 1, y);
    }
    
    private boolean eatsHimselfMovingToSouth() {
        SnakeCompartment headSnakeCompartment = compartmentQueue.getFirst();
        int x = headSnakeCompartment.x;
        int y = headSnakeCompartment.y;
        return occupiesPoint(x, y + 1);
    }
    
    private boolean eatsHimselfMovingToWest() {
        SnakeCompartment headSnakeCompartment = compartmentQueue.getFirst();
        int x = headSnakeCompartment.x;
        int y = headSnakeCompartment.y;
        return occupiesPoint(x - 1, y);
    }
    
    boolean occupiesPoint(int x, int y) {
        for (SnakeCompartment snakeCompartment : this) {
            if (snakeCompartment.x == x && snakeCompartment.y == y) {
                return true;
            }
        }
        
        return false;
    }
}
