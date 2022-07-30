package AStarAlgo;

import snakes.Bot;
import snakes.Coordinate;
import snakes.Direction;
import snakes.Snake;

import java.util.*;


/**
 * The class MyBot implements the interface called Bot
 * which includes functions that should be implemented
 * to create smart snake bot for the game
 */
public class MyBot implements Bot {
    private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

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

        Node food = new Node(apple.x, apple.y);
        Node snakeHead = new Node(snake.getHead().x, snake.getHead().y);
        Node opponentHead = new Node(opponent.getHead().x, opponent.getHead().y);


        if(snake.body.size() <= opponent.body.size() && manhattanDistance(opponentHead, food) == 1){
            return randomDir(snake, opponent, mazeSize);
        }
        return aStarAlgorithm(snakeHead, food, mazeSize, snake, opponent);
    }


    /**
     * This method is unused, it will be helpful for future developement to improve our snakes
     * ability to survive
     * @param opponent Opponent snake's body with coordinates for each segment
     * @param apple Coordinate of an apple
     * @return Coordinate a coordinate with the position of the opponent
     */
    public Coordinate getOpponentPos(Snake opponent, Coordinate apple){
        Node opponentHead = new Node(opponent.getHead().x, opponent.getHead().y);

        if(getDirectionSnake(opponent, apple) == Direction.DOWN){
            return new Coordinate(opponentHead.x, opponentHead.y-1);
        }else if(getDirectionSnake(opponent, apple) == Direction.UP){
            return new Coordinate(opponentHead.x, opponentHead.y+1);
        }else if(getDirectionSnake(opponent, apple) == Direction.RIGHT){
            return new Coordinate(opponentHead.x+1, opponentHead.y);
        }else return new Coordinate(opponentHead.x-1, opponentHead.y);
    }

    /**
     * Method to check if the next direction is in the mazeSize, the snake doesnt walk into its own body
     * and doesnt walk into the opponent
     * @param snake Your snake's body with coordinates for each segment
     * @param opponent Opponent snake's body with coordinates for each segment
     * @param mazeSize Size of the board
     * @param dir the direction in which the snake should go next
     * @return boolean true or false, depending on if the direction is valid or not
     */
    public boolean validDirection(Snake snake, Snake opponent, Coordinate mazeSize, Direction dir){
        Coordinate head = snake.getHead();

        return !opponent.elements.contains(head.moveTo((dir))) && !snake.elements.contains(head.moveTo(dir))
                && head.moveTo(dir).inBounds(mazeSize);

    }


    /**
     * Method to get a random direction in which the snake won't die and hopefully won't lose
     * It's more important to not lose though
     * @param snake Your snake's body with coordinates for each segment
     * @param opponent Opponent snake's body with coordinates for each segment
     * @param mazeSize Size of the board
     * @return Direction a direction in which the snake won't die
     */
    public Direction randomDir(Snake snake, Snake opponent, Coordinate mazeSize){
        Coordinate head = snake.getHead();

        /* Get the coordinate of the second element of the snake's body
         * to prevent going backwards */
        Coordinate afterHeadNotFinal = null;
        if (snake.body.size() >= 2) {
            Iterator<Coordinate> it = snake.body.iterator();
            it.next();
            afterHeadNotFinal = it.next();
        }

        final Coordinate afterHead = afterHeadNotFinal;

        /* The only illegal move is going backwards. Here we are checking for not doing it */
        Direction[] validMoves = Arrays.stream(DIRECTIONS)
                .filter(d -> !head.moveTo(d).equals(afterHead)) // Filter out the backwards move
                .sorted()
                .toArray(Direction[]::new);


        /* Just naïve greedy algorithm that tries not to die at each moment in time */
        Direction[] notLosing = Arrays.stream(validMoves)

                .filter(d -> head.moveTo(d).inBounds(mazeSize))             // Don't leave maze
                .filter(d -> !opponent.elements.contains(head.moveTo(d)))  // Don't collide with opponent...
                .filter(d -> !snake.elements.contains(head.moveTo(d)))      // and yourself
                .sorted()
                .toArray(Direction[]::new);

        if (notLosing.length > 0) return notLosing[0];
        else return validMoves[0];
        /* Cannot avoid losing here */
    }

    /**
     * Method which calculates a direction based on the given snake and a destination coordinate
     * @param snake Your snake's body with coordinates for each segment
     * @param destination the destination coordinate
     * @return Direction calculated direction based on 2 coordinates
     */
    private Direction getDirectionSnake(Snake snake, Coordinate destination) {
        if (snake.getHead().x != destination.x) {
            if (snake.getHead().x <= destination.x) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (snake.getHead().y <= destination.y) {
                return Direction.UP;
            } else {
                return Direction.DOWN;
            }
        }
    }


    /**
     * Method which calculates the distance between a starting and destination node
     * @param currentNode the starting node
     * @param neighbour the destination node
     * @return int the distance between two nodes as an integer
     */
    private int manhattanDistance(Node currentNode, Node neighbour) {
        return ((Math.abs(currentNode.x - neighbour.x)) + (Math.abs(currentNode.y - neighbour.y)));
    }

    /**
     * This method includes the implemented A* algorithm and returns the calculated direction the snake will go to next
     * @param start start Node
     * @param foodDestination Node of the current apple
     * @param mazeSize Size of the board
     * @param snake Your snake's body with coordinates for each segment
     * @param opponent Opponent snake's body with coordinates for each segment
     * @return Direction the Direction the snake will go next based on the algorithm
     */
    private Direction aStarAlgorithm(Node start, Node foodDestination, Coordinate mazeSize, Snake snake, Snake opponent) {

        PriorityQueue<Node> openQueue = new PriorityQueue<Node>();

        LinkedList<Node> listClosed = new LinkedList<Node>();

        start.setG(0);

        start.setH(manhattanDistance(start, foodDestination));

        start.setFather(null);
        openQueue.add(start);

        //Run open list until it is empty
        while (!openQueue.isEmpty()) {
            //PriorityQueue gives nodes with lowest F
            //therefore remove nodes from queue
            Node currentNode = openQueue.remove();
            //if currentNode equals apple, then reconstruct the path and return it

            if (currentNode.equals(foodDestination)) {
                LinkedList<Node> path = makePath(currentNode);

                if(!path.isEmpty()){
                    Direction firstDir = getDirectionSnake(snake, path.getFirst());
                    if(validDirection(snake, opponent, mazeSize, firstDir)){
                        return firstDir;
                    }
                }
            }
            //find all neighbours of currentNode
            List<Node> neighbours = currentNode.myNeighbours(currentNode);

            for (int i = 0; i < neighbours.size(); i++) {

                Node neighbour = neighbours.get(i);
                boolean isInOpen = openQueue.contains(neighbour);
                boolean isInClosed = listClosed.contains(neighbour);

                //calculates distance between currentNoe and this Neighbour
                //Add distance to the neighbouring node G
                int neighborDistanceFromStart = currentNode.getG() + manhattanDistance(currentNode, neighbour);

                if (!isInOpen && !isInClosed) {
                    //set parameter of neighbourNode
                    neighbour.setFather(currentNode);
                    neighbour.setG(neighborDistanceFromStart);
                    neighbour.setH(manhattanDistance(neighbour, foodDestination));

                    if(shouldProcess(neighbour, mazeSize, snake)){
                        openQueue.add(neighbour);
                    }
                }
            }

            Node now=null;
            int max=-1;
            for(Node n:openQueue){
                //We find the F value (the description farthest from the target),
                // if the same we choose behind the list is the latest addition.
                if(n.getF()>=max){
                    max=n.getF();
                    now=n;
                }
            }
            //Remove currentNode from the openQueue and add it to the closedList.
            openQueue.remove(now);
            listClosed.add(currentNode);
        }
        return randomDir(snake, opponent, mazeSize);
    }


    /**
     * This methods recreates the shortest path starting from destination(food) node back to the starting node (current snake's head position)
     * @param node food node (destination)
     * @return LinkedList<Node> a list of nodes which represent all nodes of the shortest path to the apple
     */
    public LinkedList<Node> makePath(Node node) {

        LinkedList<Node> path = new LinkedList<Node>();

        while (node.getFather() != null) {
            path.addFirst(node);
            node = node.getFather();
        }
        return path;
    }


    /**
     * Method to check if a node is in the mazeSize
     * @param n the node that should be checke
     * @param mazeSize Size of the board
     * @param snake Your snake's body with coordinates for each segment
     * @return boolean , true or false depending on if the node is inside the size of the board
     */
    public boolean shouldProcess(Node n, Coordinate mazeSize, Snake snake) {

        //if node is out of screen MAX
        if (n.getX() > (mazeSize.x - 1) ||
                n.getY() > (mazeSize.y - 1)) {
            return false;
        }
        //if node is out of screen MIN
        if (n.getX() < 0 ||
                n.getY() < 0) {
            return false;
        }
        Coordinate neighbour = new Coordinate(n.x, n.y);
        return !snake.elements.contains(neighbour);
    }
}