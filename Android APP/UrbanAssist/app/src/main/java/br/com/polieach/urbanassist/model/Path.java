package br.com.polieach.urbanassist.model;

import java.util.LinkedList;

public class Path implements Comparable<Path> {
    int fx, cost;
    LinkedList<Edge> path;

    public Path(int cost, int heuristic, LinkedList<Edge> path) {
        this.fx = cost + heuristic;
        this.path = path;
        this.cost = cost;
    }

    @Override
    public int compareTo(Path other) {
        return this.fx - other.fx;
    }

    public int getCost() {
        return cost;
    }

    public LinkedList<Edge> getPath() {
        return path;
    }

    public boolean contains(Thing thing) {

        for (Edge edge : path) {
            if (edge.getDestination().getID() == thing.getID())
                return true;
        }

        return false;
    }

}