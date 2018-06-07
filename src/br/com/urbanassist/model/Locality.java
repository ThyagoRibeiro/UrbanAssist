package br.com.urbanassist.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Locality implements Serializable {

	ArrayList<Comment> commentList;
	Attribute description = new Attribute();
	int idLocality;
	Attribute name = new Attribute();
	Responsible responsible = new Responsible();

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

	public Attribute getNome() {
		return name;
	}

	public Responsible getResponsavel() {
		return responsible;
	}

	public void setDescricao(Attribute descricao) {
		this.description = descricao;
	}

	public void setID(int id) {
		this.idLocality = id;
	}

	public void setNome(Attribute nome) {
		this.name = nome;
	}

	public void setResponsavel(Responsible responsavel) {
		this.responsible = responsavel;
	}

}