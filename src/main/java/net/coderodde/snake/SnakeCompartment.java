package net.coderodde.snake;

public final class SnakeCompartment {
    protected MotionDirection motionDirection;
    protected int x;
    protected int y;

    public SnakeCompartment(MotionDirection direction, int x, int y) {
        this.motionDirection = direction;
        this.x = x;
        this.y = y;
    }

    public SnakeCompartment(SnakeCompartment snakeCompartment) {
        this.motionDirection = snakeCompartment.motionDirection;
        this.x = snakeCompartment.x;
        this.y = snakeCompartment.y;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        
        if (o == null) {
            return false;
        }
        
        if (!getClass().equals(o.getClass())) {
            return false;
        }
        
        SnakeCompartment other = (SnakeCompartment) o;
        return motionDirection.equals(other.motionDirection) &&
               x == other.x &&
               y == other.y;
    }
}