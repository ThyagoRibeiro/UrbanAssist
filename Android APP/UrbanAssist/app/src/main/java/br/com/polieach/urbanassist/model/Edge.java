package br.com.polieach.urbanassist.model;

import java.io.Serializable;

public class Edge implements Serializable {

    private Thing destination;
    private double distance;
    private int degree;
    private Thing origin;

    public Edge(Thing origin, Thing destination, int distance, int degree) {
        super();
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.degree = degree;
    }

    public Edge() {
        // TODO Auto-generated constructor stub
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public Thing getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Thing getOrigin() {
        return origin;
    }

    public void setDestinationID(int destinationID) {
        destination = new Thing();
        destination.setID(destinationID);
    }

    public void setOriginID(int originID) {
        origin = new Thing();
        origin.setID(originID);
    }

}