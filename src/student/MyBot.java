package student;

import snakes.*;

import java.util.*;


public class MyBot implements Bot {

    private static final Direction[] DIRECTIONS = new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    SnakeCanvas canvas;

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


    private double manhattenDistance(Coordinate polledNode, Coordinate neighbour) {
        return Math.sqrt(Math.abs(polledNode.x - neighbour.x) + Math.abs(polledNode.y - neighbour.y));
    }

    /**
     * add father of node to linkedlist
     * @param node
     * @return
     */
    public LinkedList<Node> makePath(Node node){

        LinkedList<Node> path = new LinkedList<Node>();

        while(node.getFather() !=null){
            path.addFirst(node);
            node = node.getFather();
        }

        return path;
    }

    /**
     * @return should we process this node
     */
    public boolean shouldProcess(Node n, Snake snake, Coordinate mazeSize){
        //if node is out of screen MAX
        if(n.getX()>(mazeSize.x-1) ||
                n.getY()>(mazeSize.y-1)) {
            return false;
        }
        //if node is out of screen MIN
        if(n.getX()<0 ||
                n.getY()<0) {
            return false;
        }

        boolean  shouldProceed=!snake.elements.contains(n);
        return shouldProceed;

    }
}