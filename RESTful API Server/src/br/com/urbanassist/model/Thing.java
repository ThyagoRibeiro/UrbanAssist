package br.com.urbanassist.model;

import java.io.Serializable;
import java.util.ArrayList;

import br.com.urbanassist.model.Comment;
import br.com.urbanassist.model.Locality;

public class Thing implements Serializable {

	Attribute alert = new Attribute();
	ArrayList<Comment> commentList = new ArrayList<>();
	Attribute description = new Attribute();
	Attribute display = new Attribute();
	int idThing;
	Locality locality = new Locality();
	Attribute message = new Attribute();
	Attribute name = new Attribute();
	Responsible responsible = new Responsible();
	Situation situation = new Situation();
	AttrClass attrClass = new AttrClass();

	public void addComentarios(Comment comment) {
		this.commentList.add(comment);
	}

	public Attribute getAlert() {
		return alert;
	}

	public AttrClass getAttrClass() {
		return attrClass;
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

	public Situation getSituation() {
		return situation;
	}

	public void setAlert(Attribute alert) {
		this.alert = alert;
	}

	public void setAttrClass(AttrClass attrClass) {
		this.attrClass = attrClass;
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

	public void setResponsible(Responsible responsible) {
		this.responsible = responsible;
	}

	public void setSituation(Situation situation) {
		this.situation = situation;
	}

}
