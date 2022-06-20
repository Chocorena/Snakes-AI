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

        LinkedList<Node> list = aStarAlgorithm(snakeHead, food, mazeSize, snake);


        if(list != null){
            Coordinate li = list.getFirst();
            Direction dir = getDirection(snake, li);
            if(validDirection(snake, opponent, mazeSize, dir)){
                return  dir;
            } else randomDir(snake, opponent, mazeSize);
        }
        return randomDir(snake, opponent, mazeSize);

    }

    public boolean validDirection(Snake snake, Snake opponent, Coordinate mazeSize, Direction dir){
        Coordinate head = snake.getHead();

        if(!opponent.elements.contains(head.moveTo((dir))) && !snake.elements.contains(head.moveTo(dir)) && head.moveTo(dir).inBounds(mazeSize)) {
            return true;
        } else return false;

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

    private Direction getDirection(Snake snake, Coordinate destination) {
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

    private LinkedList<Node> aStarAlgorithm(Node start, Node foodDestination, Coordinate mazeSize, Snake snake) {

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
                if(!path.isEmpty()){
                    return path;
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

                int neighborDistanceFromStart = currentNode.getG() + 1;
                //System.out.println("getG: " + currentNode.getG());

                if (!isInOpen && !isInClosed) {

                    //set the parameters of this neighborNode
                    neighbour.setFather(currentNode);
                    neighbour.setG(neighborDistanceFromStart);
                    neighbour.setH(manhattanDistance(neighbour, foodDestination));
                    //System.out.println("setH: " + manhattanDistance(neighbour,foodDestination));


                    if (shouldProcess(neighbour, mazeSize, snake)) {


                        openQueue.add(neighbour);

                        //System.out.println("openqueue: " + openQueue);
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
        return null;
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
        //System.out.println("Path: "+ path);
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
        //System.out.println(!snake.elements.contains(neighbour));
        return !snake.elements.contains(neighbour);

    }
}
    /*public Direction aSearch(Snake snake, Node food){

        ArrayList<Node> openList = new ArrayList<Node>();
        ArrayList<Node> closeList = new ArrayList<Node>();
        Stack<Node> stack = new Stack<Node>();//Snake to eat the path

        Node snakeHead = new Node(snake.getHead().x, snake.getHead().y);
        openList.add(snakeHead);// Place the start node in the open list;

        snakeHead.setH(manhattanDistance(snakeHead,food));

        while(!openList.isEmpty()){
            Node now=null;
            int max=-1;
            for(Node n:openList){//We find the F value (the description farthest from the target), if the same we choose behind the list is the latest addition.
                if(n.getF()>=max){
                    max=n.getF();
                    now=n;
                }
            }
            // Remove the current node from the open list and add it to the closed list
            openList.remove(now);
            closeList.add(now);
            //Neighbor in four directions
            Node up = new Node(now.getX(), now.getY() - snake.elements.size());
            Node right = new Node(now.getX() + snake.elements.size(), now.getY());
            Node down = new Node(now.getX(), now.getY() + snake.elements.size());
            Node left = new Node(now.getX() - snake.elements.size(), now.getY());
            ArrayList<Node> temp = new ArrayList<Node>(4);
            temp.add(up);
            temp.add(right);
            temp.add(down);
            temp.add(left);
            for (Node n : temp){
                // If the neighboring node is not accessible or the neighboring node is already in the closed list, then no action is taken and the next node continues to be examined;
                if (closeList.contains(n)
                        || n.getX()>(snake.mazeSize.x-1) || n.getX() < 0
                        || n.getY()>(snake.mazeSize.y-1) || n.getY() < 0)
                    continue;

                // If the neighbor is not in the open list, add the node to the open list,
                //  and the adjacent node's parent node as the current node, while saving the adjacent node G and H value, F value calculation I wrote directly in the Node class
                if(!openList.contains(n)){
//					System.out.println("ok");
                    n.setFather(now);
                    n.setG(now.getG()+10);
                    n.setH(manhattanDistance(n,food));
                    openList.add(n);
                    // When the destination node is added to the open list as the node to be checked, the path is found, and the loop is terminated and the direction is returned.
                    if (n.equals(food)) {

                        //Go forward from the target node, .... lying groove there is a pit, node can not use f, because f and find the same node coordinates but f did not record father
                        Node node = openList.get(openList.size() - 1);
                        while(node!=null&&!node.equals(snakeHead)){
                            stack.push(node);
                            node=node.getFather();
                        }
                        int x = stack.peek().getX();
                        int y = stack.peek().getY();
                        if (x > snakeHead.x && y == snakeHead.y) {
                            return Direction.RIGHT;
                        }
                        if (x < snakeHead.x && y == snakeHead.y) {
                            return Direction.LEFT;
                        }
                        if (x == snakeHead.x && y > snakeHead.y) {
                            return Direction.UP;
                        }
                        if (x == snakeHead.x && y < snakeHead.y) {
                            return Direction.DOWN;
                        }
                    }
                }
                // If the neighbor is in the open list,
                // // judge whether the value of G that reaches the neighboring node via the current node is greater than or less than the value of G that is stored earlier than the current node (if the value of G is greater than or smaller than the value of G), set the parent node of the adjacent node as Current node, and reset the G and F values ​​of the adjacent node.
                if (openList.contains(n)) {
                    if (n.getG() > (now.getG() + 1)) {
                        n.setFather(now);
                        n.setG(now.getG() + 1);
                    }
                }
            }
        }
        // When the open list is empty, indicating that there is no new node to add, and there is no end node in the tested node, the path can not be found. At this moment, the loop returns -1 too.
        return Direction.UP;
    }
}*/