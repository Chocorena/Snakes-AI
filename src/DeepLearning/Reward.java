package DeepLearning;

import snakes.Coordinate;
import snakes.Direction;

import java.util.Arrays;

public class Reward{
    private Reward() {}

    /**
     * Used to calculate reward for taken action.
     *
     * @param action Action that was taken.
     * @param snakePosition Current snake position.
     * @param foodPosition Current food position.
     * @return Returns calculated reward value.
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
     * Used to verify if forwarded position is closer to the food.
     *
     * @param nextCoordinate .
     * @param foodPosition .
     * @param nextDirection .
     * @return .
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

    private static double getRewardForPosition(final Direction nextDirection,
                                               final Coordinate nextCoordinate,
                                               final Coordinate snakePosition,
                                               final Coordinate foodPosition, Coordinate mazeSize) {
        if (nextCoordinate.inBounds(mazeSize) || Arrays.asList(snakePosition).contains(nextCoordinate)) {
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
