package student;

import snakes.Bot;
import snakes.Coordinate;
import snakes.Direction;
import snakes.Snake;

import java.util.*;


public class MyBot implements Bot {
    private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {

        Node food = new Node(apple.x, apple.y);
        Node snakeHead = new Node(snake.getHead().x, snake.getHead().y);
        Node opponentHead = new Node(opponent.getHead().x, opponent.getHead().y);

        /*LinkedList<Node> list = aStarAlgorithm(snakeHead, food, mazeSize, snake, opponent);

        if(list != null){
            Coordinate li = list.getFirst();
            System.out.println(li);
            Direction dir = getDirectionSnake(snake, li);
            if(validDirection(snake, opponent, mazeSize, dir) ){
                if(snake.body.size() <= opponent.body.size() && manhattanDistance(opponentHead, food) == 1){
                    //System.out.println("snake body smaller and near food");
                    randomDir(snake, opponent, mazeSize);
                }else return dir;//{System.out.println("direction valid");

                //return dir;}
                //if(!getOpponentPos(opponent, apple).equals(snake.getHead().moveTo(dir))){
                //}

            } //else return randomDir(snake, opponent, mazeSize);//{System.out.println("direction not valid");
                    //return randomDir(snake, opponent, mazeSize);}
        }
        //System.out.println("list is null");
        System.out.println("list is null");
        return randomDir(snake, opponent, mazeSize);*/
        if(snake.body.size() <= opponent.body.size() && manhattanDistance(opponentHead, food) == 1){
            return randomDir(snake, opponent, mazeSize);
        }
        return aStarAlgorithm(snakeHead, food, mazeSize, snake, opponent);
    }

    /*public Direction performAlgorithm(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple){
        Node food = new Node(apple.x, apple.y);
        Node snakeHead = new Node(snake.getHead().x, snake.getHead().y);
        Node opponentHead = new Node(opponent.getHead().x, opponent.getHead().y);

        LinkedList<Node> list = aStarAlgorithm(snakeHead, food, mazeSize, snake);

        if(list != null){
            Coordinate li = list.getFirst();
            Direction dir = getDirectionSnake(snake, li);
            if(validDirection(snake, opponent, mazeSize, dir) ){
                if(snake.body.size() <= opponent.body.size() && manhattanDistance(opponentHead, food) == 1){
                    System.out.println("snake body smaller and near food");
                    randomDir(snake, opponent, mazeSize);
                }else {System.out.println("direction valid");
                    return dir;}
                //if(!getOpponentPos(opponent, apple).equals(snake.getHead().moveTo(dir))){
                //}

            } else {System.out.println("direction not valid");
                return randomDir(snake, opponent, mazeSize);}
        }else performAlgorithm(snake, opponent, mazeSize, apple);
        return null;
    }*/

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

    public boolean validDirection(Snake snake, Snake opponent, Coordinate mazeSize, Direction dir){
        Coordinate head = snake.getHead();

        return !opponent.elements.contains(head.moveTo((dir))) && !snake.elements.contains(head.moveTo(dir))
                && head.moveTo(dir).inBounds(mazeSize);

    }


    /**
     * Random production direction
     * @return
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
        /* ^^^ Cannot avoid losing here */
    }

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


    private int manhattanDistance(Node currentNode, Node neighbour) {
        return ((Math.abs(currentNode.x - neighbour.x)) + (Math.abs(currentNode.y - neighbour.y)));
    }

    private Direction aStarAlgorithm(Node start, Node foodDestination, Coordinate mazeSize, Snake snake, Snake opponent) {

        PriorityQueue<Node> openQueue = new PriorityQueue<Node>();

        LinkedList<Node> listClosed = new LinkedList<Node>();

        start.setG(0);

        start.setH(manhattanDistance(start, foodDestination));

        start.setFather(null);
        openQueue.add(start);

        //offene Liste so lange laufen lassen bis sie leer ist
        while (!openQueue.isEmpty()) {

            //PriorityQueue gibt Knoten mit niedrigsten F
            // deshalb Knoten aus Queue entfernen

            Node currentNode = openQueue.remove();

            //System.out.println("Current Node: " + currentNode);
            //if polledNode is destination then just make path and return
            if (currentNode.equals(foodDestination)) {

                LinkedList<Node> path = makePath(currentNode);
                System.out.println(path);
                if(!path.isEmpty()){
                    //System.out.println(path);
                    //System.out.println(foodDestination);

                    Direction firstDir = getDirectionSnake(snake, path.getFirst());
                    if(validDirection(snake, opponent, mazeSize, firstDir)){
                        //System.out.println("direction valid");
                        return firstDir;
                    }
                }
            }

            //find all neighbors of polledNode
            List<Node> neighbours = currentNode.myNeighbours(currentNode);
            //System.out.println("Neighbours:" + neighbours);
            for (int i = 0; i < neighbours.size(); i++) {

                Node neighbour = neighbours.get(i);
                boolean isInOpen = openQueue.contains(neighbour);
                boolean isInClosed = listClosed.contains(neighbour);

                //berechnet distance zwischen polledNode and this Neighbornode
                //add distance into polledNode G

                int neighborDistanceFromStart = currentNode.getG() + manhattanDistance(currentNode, neighbour);;
                //System.out.println("getG: " + currentNode.getG());

                if (!isInOpen && !isInClosed || neighborDistanceFromStart < neighbour.getG()) {

                    //set the parameters of this neighborNode
                    neighbour.setFather(currentNode);
                    neighbour.setG(neighborDistanceFromStart);
                    neighbour.setH(manhattanDistance(neighbour, foodDestination));
                    //System.out.println("setH: " + manhattanDistance(neighbour,foodDestination));

                    if(shouldProcess(neighbour, mazeSize, snake)){
                        //System.out.println("should process true");
                        openQueue.add(neighbour);
                    }
                }
            }
            Node now=null;
            int max=-1;
            for(Node n:openQueue){//We find the F value (the description farthest from the target), if the same we choose behind the list is the latest addition.
                if(n.getF()>=max){
                    max=n.getF();
                    now=n;
                }
            }
            // Remove the current node from the open list and add it to the closed list
            openQueue.remove(now);
            listClosed.add(currentNode);
        }
        System.out.println("list is null");
        return randomDir(snake, opponent, mazeSize);
    }

    /**
     * add father of node to linkedlist
     *
     * @param node
     * @return
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
     * @return should we process this node
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