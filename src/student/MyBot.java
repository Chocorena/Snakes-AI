package student;

import snakes.Bot;
import snakes.Coordinate;
import snakes.Direction;
import snakes.Snake;

import java.util.Arrays;
import java.util.Iterator;

public class MyBot implements Bot {

        private static final Direction[] DIRECTIONS = new Direction[] { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        @Override
        public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple){
            Coordinate head = snake.getHead();

            Coordinate afterHeadNotFinal=null;
            if(snake.body.size()>=2){
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



            if(notLosing.length>0) return notLosing[0];
            else return validMoves[0];
        }
    }


