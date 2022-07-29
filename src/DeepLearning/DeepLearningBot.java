package DeepLearning;

import AStarAlgo.Node;
import snakes.Bot;
import snakes.Coordinate;
import snakes.Direction;
import snakes.Snake;

public class DeepLearningBot implements Bot{
    private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    private Action action;

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Reward.calculateRewardForAction(action, snake.getHead(), apple, mazeSize);
        return null;
    }



}
