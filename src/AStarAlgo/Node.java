package AStarAlgo;

import snakes.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements nodes of a cell on the game board
 * This class inherits from the Coordinate class
 * Goal of this class was to add own methods and attributes without changed the base class Coordinate
 * because of the tournament
 */
public class Node extends Coordinate{
    public int x, y;
    private int G =0;
    private int H =0;
    private Node father =  null;

    /**
     * Both parameters together create a Node which has the same attributes as a Coordinate
     * @param x the x represents an integer on the x axis
     * @param y the y represents an integer on the y axis
     */
    public Node(int x, int y) {
        super(x, y);
        this.x = x;
        this.y = y;
    }


    /**
     * method to get the x value of a node
     * @return an integer which represents the x value of a cell
     */
    public int getX() {
        return x;
    }

    /**
     * method to set an x value of a node
     * @param x an integer which represents the x value of a cell
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * method to get the y value of a node
     * @return an integer which represents the y value of a cell
     */
    public int getY() {
        return y;
    }

    /**
     * method to set the y value of a node
     * @param y and integer which represents the y value of a cell
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * method to get F which means adding G and H value
     * @return the integer of G+H
     */
    public int getF(){
        return G+H;
    }

    /**
     * method to get the value of G
     * @return the integer G
     */
    public int getG(){
        return G;
    }

    /**
     * method to set the value of G
     * @param g the integer of the value of g
     */
    public void setG(int g){
        G = g;
    }

    /**
     * method to get the value of H
     * @return an integer H
     */
    public int getH(){
        return H;
    }

    /**
     * method to set the value of H
     * @param h the integer of h
     */
    public void setH(int h){
        H =h;
    }

    /**
     * method to get the saved predecessor of a node
     * @return a node which is the predecessor of a node
     */
    public Node getFather(){
        return father;
    }

    /**
     * method to set the predecessor of a node
     * @param father the predecessor of a node
     */
    public void setFather(Node father){
        this.father = father;
    }


    /**
     * Get all 4 neighbours of a current Node
     * @param currentNode the node from which the 4 neighbours should be received
     * @return a list of all 4 neighbours of a given node
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
