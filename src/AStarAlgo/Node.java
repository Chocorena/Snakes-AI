package AStarAlgo;

import snakes.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements nodes of a cell on the game boar
 */
public class Node extends Coordinate{
    public int x, y;
    private int G =0;
    private int H =0;
    private Node father =  null;

    public Node(int x, int y) {
        super(x, y);
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
     *
     * @return array of neighbour coordinates
     * @author Morena
     */
    public List<Node> myNeighbours(Node currentNode){

        Node up = new Node(currentNode.x, currentNode.y+1);
        Node down = new Node(currentNode.x, currentNode.y-1);
        Node left = new Node(currentNode.x-1, currentNode.y);
        Node right = new Node(currentNode.x+1, currentNode.y);

        List<Node> neighbours = new ArrayList<>();
        neighbours.add(up);
        neighbours.add(down);
        neighbours.add(left);
        neighbours.add(right);

        return neighbours;
    }
    /**
     * Check whether objects(here nodes) are equal
     *
     * @param o an object that should be compared
     * @return True - if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x &&
                y == node.y;
    }

}
