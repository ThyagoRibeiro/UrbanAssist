package br.com.urbanassist.model;

import java.io.Serializable;

public class User implements Serializable {

	int idUser;
	Attribute name;
	int midia;

	public int getID() {
		return idUser;
	}

	public Attribute getName() {
		return name;
	}

	public int getMidia() {
		return midia;
	}

	public void setID(int idUser) {
		this.idUser = idUser;
	}

	public void setName(Attribute name) {
		this.name = name;
	}

	public void setMidia(int midia) {
		this.midia = midia;
	}

}
