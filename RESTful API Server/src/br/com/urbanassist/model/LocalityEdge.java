package br.com.urbanassist.model;

import br.com.urbanassist.model.Locality;

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

	public Locality getOrigin() {
		return origin;
	}

	public void setDestination(Locality destination) {
		this.destination = destination;
	}

	public void setOrigin(Locality origin) {
		this.origin = origin;
	}

}