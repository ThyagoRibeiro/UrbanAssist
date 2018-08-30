package br.com.urbanassist.model;

import java.io.Serializable;

public class User implements Serializable {

	int idUser;
	Attribute name;
	int midia;

	public int getID() {
		return idUser;
	}

	public int getMidia() {
		return midia;
	}

	public Attribute getName() {
		return name;
	}

	public void setID(int idUser) {
		this.idUser = idUser;
	}

	public void setMidia(int midia) {
		this.midia = midia;
	}

	public void setName(Attribute name) {
		this.name = name;
	}

}
