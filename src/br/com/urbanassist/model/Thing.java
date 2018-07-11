package br.com.urbanassist.model;

import java.io.Serializable;
import java.util.ArrayList;

import br.com.urbanassist.model.Comment;
import br.com.urbanassist.model.Locality;

public class Thing implements Serializable {

	Attribute alert = new Attribute();
	Attribute concept = new Attribute();
	ArrayList<Comment> commentList = new ArrayList<>();
	Attribute description = new Attribute();
	Attribute display = new Attribute();
	int idThing;
	Locality locality = new Locality();
	Attribute message = new Attribute();
	Attribute name = new Attribute();
	Responsible responsible = new Responsible();
	Attribute situation = new Attribute();

	public void addComentarios(Comment comment) {
		this.commentList.add(comment);
	}

	public Attribute getAlert() {
		return alert;
	}

	public Attribute getConcept() {
		return concept;
	}

	public ArrayList<Comment> getCommentList() {
		return commentList;
	}

	public Attribute getDescription() {
		return description;
	}

	public Attribute getDisplay() {
		return display;
	}

	public int getID() {
		return idThing;
	}

	public Locality getLocality() {
		return locality;
	}

	public Attribute getMessage() {
		return message;
	}

	public Attribute getName() {
		return name;
	}

	public Responsible getResponsible() {
		return responsible;
	}

	public Attribute getSituation() {
		return situation;
	}

	public void setAlert(Attribute alert) {
		this.alert = alert;
	}

	public void setConcept(Attribute concept) {
		this.concept = concept;
	}

	public void setDescription(Attribute descricao) {
		this.description = descricao;
	}

	public void setDisplay(Attribute display) {
		this.display = display;
	}

	public void setID(int idThing) {
		this.idThing = idThing;
	}

	public void setLocality(Locality locality) {
		this.locality = locality;
	}

	public void setMessage(Attribute message) {
		this.message = message;
	}

	public void setName(Attribute name) {
		this.name = name;
	}

	public void setResponsavel(Responsible responsavel) {
		this.responsible = responsavel;
	}

	public void setSituation(Attribute situation) {
		this.situation = situation;
	}
	
}
