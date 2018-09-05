package br.com.polieach.urbanassist.model;

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

    public void setDescription(Attribute descricao) {
        this.description = descricao;
    }

    public int getID() {
        return idLocality;
    }

    public void setID(int id) {
        this.idLocality = id;
    }

    public Attribute getName() {
        return name;
    }

    public void setName(Attribute name) {
        this.name = name;
    }

    public Responsible getResponsible() {
        return responsible;
    }

    public void setResponsible(Responsible responsible) {
        this.responsible = responsible;
    }

}
