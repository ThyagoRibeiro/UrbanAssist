package br.com.polieach.urbanassist.model;

import java.io.Serializable;

public class User implements Serializable {

	int idUser;
	Attribute name;
	int midia;
	String googleID;
	String googleEmail;

	public Attribute getName() {
		return name;
	}

	public int getMidia() {
		return midia;
	}

	public void setName(Attribute name) {
		this.name = name;
	}

	public void setMidia(int midia) {
		this.midia = midia;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getGoogleID() {
		return googleID;
	}

	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}

	public String getGoogleEmail() {
		return googleEmail;
	}

	public void setGoogleEmail(String googleEmail) {
		this.googleEmail = googleEmail;
	}
}
