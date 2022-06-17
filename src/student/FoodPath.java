package student;

import java.util.LinkedList;

public class FoodPath {

    private Node currentFood;

    private LinkedList<Node> pathToCurrentFood;

    public Node getCurrentFood() {
        return currentFood;
    }

    public void setCurrentFood(Node currentFood) {
        this.currentFood = currentFood;
    }

    public LinkedList<Node> getPathToCurrentFood() {
        return pathToCurrentFood;
    }

    public void setPathToCurrentFood(LinkedList<Node> pathToCurrentFood) {
        this.pathToCurrentFood = pathToCurrentFood;
    }

}
