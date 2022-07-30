package DeepLearning;

import AStarAlgo.Node;
import snakes.Bot;
import snakes.Coordinate;
import snakes.Direction;
import snakes.Snake;

/**
 * NOT EXECUTABLE
 * The class DeepLearningBot implements the interface called Bot
 * which includes functions that should be implemented
 * to create smart snake bot for the game
 */
public class DeepLearningBot implements Bot{
    private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    private Action action;

    /**
     * Smart snake bot (brain of your snake) should choose step (direction where to go)
     * on each game step until the end of game
     * @param snake    Your snake's body with coordinates for each segment
     * @param opponent Opponent snake's body with coordinates for each segment
     * @param mazeSize Size of the board
     * @param apple    Coordinate of an apple
     * @return Direction the direction in which the snake will go next
     */
    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Reward.calculateRewardForAction(action, snake.getHead(), apple, mazeSize);
        return null;
    }



}
