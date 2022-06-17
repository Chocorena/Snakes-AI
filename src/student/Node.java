package student;

import snakes.Coordinate;
import snakes.Snake;

import java.util.ArrayList;
import java.util.List;

public class Node{
    public int x, y;
    private int G =0;
    private int H =0;
    private Node father =  null;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getF(){
        return G+H;
    }
    public int getG(){
        return G;
    }
    public void setG(int g){
        G = g;
    }
    public int getH(){
        return H;
    }
    public void setH(int h){
        H =h;
    }
    public Node getFather(){
        return father;
    }
    public void setFather(Node father){
        this.father = father;
    }

    /**
     * Get neighbours of current snake position
     * @param snake get information about head and body, length of body
     * @return array of neighbour coordinates
     * @author Morena
     */
    public List<Node> myNeighbours(Snake snake){

        Node up = new Node(snake.getHead().x - snake.elements.size(), snake.getHead().y);
        Node down = new Node(snake.getHead().x + snake.elements.size(), snake.getHead().y);
        Node left = new Node(snake.getHead().x, snake.getHead().y - snake.elements.size());
        Node right = new Node(snake.getHead().x, snake.getHead().y + snake.elements.size());

        Node northEast = new Node(snake.getHead().x + snake.elements.size(), snake.getHead().y - snake.elements.size());
        Node northWest = new Node(snake.getHead().x - snake.elements.size(), snake.getHead().y - snake.elements.size());
        Node southEast = new Node(snake.getHead().x + snake.elements.size(), snake.getHead().y + snake.elements.size());
        Node southWest = new Node(snake.getHead().x - snake.elements.size(), snake.getHead().y + snake.elements.size());

        List<Node> neighbours = new ArrayList<>();
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
