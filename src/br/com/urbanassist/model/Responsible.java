package br.com.urbanassist.model;

import java.io.Serializable;

public class Responsible implements Serializable {

	Attribute email = new Attribute();
	int idResponsible;
	Attribute name = new Attribute();
	Attribute phone = new Attribute();

	public Attribute getEmail() {
		return email;
	}

	public int getID() {
		return idResponsible;
	}

	public Attribute getName() {
		return name;
	}

	public Attribute getPhone() {
		return phone;
	}

	public void setEmail(Attribute email) {
		this.email = email;
	}

	public void setID(int idResponsible) {
		this.idResponsible = idResponsible;
	}

	public void setName(Attribute name) {
		this.name = name;
	}
	
	public void setPhone(Attribute phone) {
		this.phone = phone;
	}

}