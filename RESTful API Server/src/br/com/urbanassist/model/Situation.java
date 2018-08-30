package br.com.urbanassist.model;

public class Situation {

	int idSituation;
	Attribute name = new Attribute();

	public int getIdSituation() {
		return idSituation;
	}

	public Attribute getName() {
		return name;
	}

	public void setIdSituation(int idSituation) {
		this.idSituation = idSituation;
	}

	public void setName(Attribute name) {
		this.name = name;
	}

}
