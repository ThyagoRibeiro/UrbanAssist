package br.com.urbanassist.model;

import br.com.urbanassist.model.Thing;

public class Edge {

	private Thing destination;
	private int distance, degree;
	private Thing origin;

	public Edge(Thing origin, Thing destination, int distance, int degree) {
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

	public int getDistance() {
		return distance;
	}

	public Thing getOrigin() {
		return origin;
	}
}