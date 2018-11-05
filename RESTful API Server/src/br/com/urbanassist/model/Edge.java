package br.com.urbanassist.model;

import br.com.urbanassist.model.Thing;

public class Edge {

	private Thing destination;
	private double distance;
	private int degree;
	private Thing origin;

	public Edge() {
		// TODO Auto-generated constructor stub
	}

	public Edge(Thing origin, Thing destination, double distance, int degree) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
		this.degree = degree;
	}

	public int getDegree() {
		return degree;
	}

	public Thing getDestination() {
		return destination;
	}

	public double getDistance() {
		return distance;
	}

	public Thing getOrigin() {
		return origin;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public void setDestination(Thing destination) {
		this.destination = destination;
	}

	public void setDestinationID(int destinationID) {
		destination = new Thing();
		destination.setID(destinationID);
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setOrigin(Thing origin) {
		this.origin = origin;
	}

	public void setOriginID(int originID) {
		origin = new Thing();
		origin.setID(originID);
	}

}