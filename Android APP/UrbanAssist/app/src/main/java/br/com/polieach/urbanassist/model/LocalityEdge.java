package br.com.polieach.urbanassist.model;

public class LocalityEdge {

    private Locality destination;
    private Locality origin;

    public LocalityEdge(Locality destination, Locality origin) {
        super();
        this.destination = destination;
        this.origin = origin;
    }

    public Locality getDestination() {
        return destination;
    }

    public void setDestination(Locality destination) {
        this.destination = destination;
    }

    public Locality getOrigin() {
        return origin;
    }

    public void setOrigin(Locality origin) {
        this.origin = origin;
    }

}