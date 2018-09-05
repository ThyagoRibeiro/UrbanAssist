package br.com.polieach.urbanassist.model;

import java.io.Serializable;

public class Comment implements Serializable {

    Attribute comment;
    User user;

    public Attribute getComment() {
        return comment;
    }

    public void setComment(Attribute comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
