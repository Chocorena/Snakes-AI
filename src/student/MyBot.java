package student;

import snakes.*;


import java.util.*;


public class MyBot implements Bot {

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {

        Node food = new Node(apple.x, apple.y);
        Node snakeHead = new Node(snake.getHead().x, snake.getHead().y);
        LinkedList<Node> list = aStarAlgorithm(snakeHead, food, mazeSize, snake);

            if(!list.isEmpty()){

               Coordinate li = (Coordinate) list.getFirst();
               Direction dir = getDirection(snake, li);
               list.remove(0);
               if(dir != null){
                   return dir;
               }else return Direction.DOWN;

            } else return Direction.DOWN;

    }



    private Direction getDirection(Snake snake, Coordinate destination){
        if(snake.getHead().x != destination.x){
            if(snake.getHead().x <= destination.x){
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else{
            if(snake.getHead().y <= destination.y){
                return Direction.UP;
            } else{
                return Direction.DOWN;
            }
        }
    }


    private int manhattanDistance(Node currentNode, Node neighbour) {
        int abstandX = Math.abs(currentNode.x - neighbour.x);
        int abstandY = Math.abs(currentNode.y - neighbour.y);
        return abstandY + abstandX;
    }

    private LinkedList<Node> aStarAlgorithm(Node start, Node foodDestination, Coordinate mazeSize, Snake snake){

        PriorityQueue<Node> openQueue = new PriorityQueue<Node>();

        LinkedList<Node> listClosed = new LinkedList<Node>();

        start.setG(0);

        start.setH(manhattanDistance(start, foodDestination));
        start.setFather(null);
        openQueue.add(start);

        //offene Liste so lange laufen lassen bis sie leer ist
        while(!openQueue.isEmpty()){

            //PriorityQueue gibt Knoten mit niedrigsten F
            // deshalb Knoten aus Queue entfernen
            Node currentNode = (Node) openQueue.remove();

            //if polledNode is destination then just make path and return
            if(currentNode.equals(foodDestination)){
                return makePath(currentNode);
            }

            //find all neighbors of polledNode
            List<Node> neighbours = currentNode.myNeighbours(snake);

            for(int i=0;i<neighbours.size();i++){

                Node neighbour = neighbours.get(i);
                boolean isInOpen= openQueue.contains(neighbour);
                boolean isInClosed = listClosed.contains(neighbour);

                //berechnet distance zwischen polledNode and this Neighbornode
                //add distance into polledNode G

                int neighborDistanceFromStart = currentNode.getG()+manhattanDistance(currentNode,neighbour);


                if((!isInOpen && !isInClosed) /*|| neighborDistanceFromStart < neighborNode.getG() */){

                    //set the parameters of this neighborNode
                    neighbour.setFather(currentNode);
                    neighbour.setG(neighborDistanceFromStart);
                    neighbour.setH(manhattanDistance(neighbour,foodDestination));


                    if(isInClosed){
                        listClosed.remove(neighbour);
                    }
                    if(!isInOpen && shouldProcess(neighbour, mazeSize, snake)){
                        openQueue.add(neighbour);
                    }
                }
            }
            listClosed.add(currentNode);
        }
        return null;
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
    public boolean shouldProcess(Node n, Coordinate mazeSize, Snake snake){
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