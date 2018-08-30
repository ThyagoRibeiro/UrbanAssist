package br.com.urbanassist.model;

public class AttrClass {

	int idClass;
	Attribute name = new Attribute();

	public int getIdClass() {
		return idClass;
	}

	public Attribute getName() {
		return name;
	}

	public void setIdClass(int idClass) {
		this.idClass = idClass;
	}

	public void setName(Attribute name) {
		this.name = name;
	}

}
