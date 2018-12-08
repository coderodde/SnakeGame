package net.coderodde.snake;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author rodde
 */
public final class Snake {
    
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
    
    public void makeStep() {
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
}
