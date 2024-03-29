package DeepLearning;

import snakes.Coordinate;
import snakes.Direction;

import java.util.Arrays;

/**
 * NOT EXECUTABLE
 * a class the calculate a suitable reward for all actions and position of our snake
 */
public class Reward{
    private Reward() {}

    /**
     * Used to calculate reward for taken action
     *
     * @param action Action that was taken
     * @param snakePosition Current snake position
     * @param foodPosition Current food position
     * @param mazeSize Size of board
     * @return Returns calculated reward value
     */
    public static double calculateRewardForAction(final Action action,
                                                  final Coordinate snakePosition,
                                                  final Coordinate foodPosition, Coordinate mazeSize) {
        Direction nextDirection = Direction.UP;
        switch (action) {
            case MOVE_UP -> {}
            case MOVE_RIGHT -> nextDirection = Direction.RIGHT;
            case MOVE_DOWN -> nextDirection = Direction.DOWN;
            case MOVE_LEFT -> nextDirection = Direction.LEFT;
        }

        final Coordinate coordinate = getnextCoordinate(snakePosition,nextDirection);

        return getRewardForPosition(nextDirection, coordinate, snakePosition, foodPosition, mazeSize);
    }

    /**
     * method to calculate a coordinate based on the snake position and a direction
     * @param cor current coordinate of the snake
     * @param nextDirection the calculated next direction
     * @return the coordinate the snake will go to next
     */
    public static Coordinate getnextCoordinate(Coordinate cor, Direction nextDirection){
        if(nextDirection == Direction.UP){
            return new Coordinate(cor.x, cor.y+1);
        }else if (nextDirection == Direction.DOWN){
            return new Coordinate(cor.x, cor.y-1);
        }else if (nextDirection == Direction.RIGHT){
            return new Coordinate(cor.x+1, cor.y);
        }else{
            return new Coordinate(cor.x-1, cor.y);
        }
    }

    /**
     * Used to verify if forwarded position is closer to the food
     *
     * @param nextCoordinate the coordinate the snake will go to next
     * @param foodPosition coordinate of the apple
     * @param nextDirection direction the snake will go to next
     * @return boolean, true or false whether the snake's current position is closer to the food or not
     */
    public static boolean isPositionCloserToFoodPosition(final Coordinate nextCoordinate,
                                                         final Coordinate foodPosition,
                                                         final Direction nextDirection) {
        if (nextDirection == Direction.UP) {
            return foodPosition.y < nextCoordinate.y;
        }

        if (nextDirection == Direction.RIGHT) {
            return foodPosition.x > nextCoordinate.x;
        }

        if (nextDirection == Direction.DOWN) {
            return foodPosition.y > nextCoordinate.y;
        }

        if (nextDirection == Direction.LEFT) {
            return foodPosition.x < nextCoordinate.x;
        }

        return false;
    }

    /**
     * method to give the snake a reward based on its position so the snake will learn which actions are good
     * @param nextDirection direction the snake will go to
     * @param nextCoordinate the next Coordinate of the snakes head based on the direction
     * @param snakePosition current position of the snake
     * @param foodPosition coordinate of the apple
     * @param mazeSize Size of board
     * @return a double which represents the reward for the snake
     */
    private static double getRewardForPosition(final Direction nextDirection,
                                               final Coordinate nextCoordinate,
                                               final Coordinate snakePosition,
                                               final Coordinate foodPosition, Coordinate mazeSize) {
        if (!nextCoordinate.inBounds(mazeSize) || Arrays.asList(snakePosition).contains(nextCoordinate)) {
            return -100.0;
        }

        if (nextCoordinate.equals(foodPosition)) {
            return 100.0;
        }

        if (isPositionCloserToFoodPosition(nextCoordinate, foodPosition, nextDirection)) {
            return 1.0;
        }

        return -1.0;
    }
}
