package br.com.urbanassist.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Locality implements Serializable {

	ArrayList<Comment> commentList;
	Attribute description = new Attribute();
	int idLocality;
	Attribute name = new Attribute();
	Responsible responsible = new Responsible();
	Locality isContainedIn;

	public void addComentarios(Comment comment) {
		this.commentList.add(comment);
	}

	public ArrayList<Comment> getComment() {
		return commentList;
	}

	public Attribute getDescription() {
		return description;
	}

	public int getID() {
		return idLocality;
	}

	public Locality getIsContainedIn() {
		return isContainedIn;
	}

	public Attribute getName() {
		return name;
	}

	public Responsible getResponsible() {
		return responsible;
	}

	public void setDescription(Attribute descricao) {
		this.description = descricao;
	}

	public void setID(int id) {
		this.idLocality = id;
	}

	public void setIsContainedIn(Locality isContainedIn) {
		this.isContainedIn = isContainedIn;
	}

	public void setName(Attribute name) {
		this.name = name;
	}

	public void setResponsible(Responsible responsible) {
		this.responsible = responsible;
	}

}
