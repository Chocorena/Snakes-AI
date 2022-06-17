package student;

import snakes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MyBot implements Bot {

    private static final Direction[] DIRECTIONS = new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Coordinate head = snake.getHead();

        Coordinate afterHeadNotFinal = null;
        if (snake.body.size() >= 2) {
            Iterator<Coordinate> it = snake.body.iterator();
            it.next();
            afterHeadNotFinal = it.next();
        }

        final Coordinate afterHead = afterHeadNotFinal;

        Direction[] validMoves = Arrays.stream(DIRECTIONS)
                .filter(d -> !head.moveTo(d).equals(afterHead))
                .sorted()
                .toArray(Direction[]::new);

        Direction[] notLosing = Arrays.stream(validMoves)
                .filter(d -> head.moveTo(d).inBounds(mazeSize))
                .filter(d -> !opponent.elements.contains(head.moveTo(d)))
                .filter(d -> !snake.elements.contains(head.moveTo(d)))
                .sorted()
                .toArray(Direction[]::new);
        return notLosing[0];
    }

    /**
     * Get neighbours of current snake position
     * @param snake get information about head and body, length of body
     * @return array of neighbour coordinates
     * @author Morena
     */
    public List<Coordinate> myNeighbours(Snake snake){

        Coordinate up = new Coordinate(snake.getHead().x - snake.elements.size(), snake.getHead().y);
        Coordinate down = new Coordinate(snake.getHead().x + snake.elements.size(), snake.getHead().y);
        Coordinate left = new Coordinate(snake.getHead().x, snake.getHead().y - snake.elements.size());
        Coordinate right = new Coordinate(snake.getHead().x, snake.getHead().y + snake.elements.size());

        Coordinate northEast = new Coordinate(snake.getHead().x + snake.elements.size(), snake.getHead().y - snake.elements.size());
        Coordinate northWest = new Coordinate(snake.getHead().x - snake.elements.size(), snake.getHead().y - snake.elements.size());
        Coordinate southEast = new Coordinate(snake.getHead().x + snake.elements.size(), snake.getHead().y + snake.elements.size());
        Coordinate southWest = new Coordinate(snake.getHead().x - snake.elements.size(), snake.getHead().y + snake.elements.size());

        List<Coordinate> neighbours = new ArrayList<>();
        neighbours.add(up);
        neighbours.add(down);
        neighbours.add(left);
        neighbours.add(right);
        neighbours.add(northWest);
        neighbours.add(northEast);
        neighbours.add(southEast);
        neighbours.add(southWest);

        return neighbours;
    }

}

