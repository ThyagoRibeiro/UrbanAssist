package br.com.urbanassist.model;

import java.io.Serializable;

public class Comment implements Serializable {

	Attribute comment;
	User user;
	
	public Attribute getComment() {
		return comment;
	}

	public User getUser() {
		return user;
	}

	public void setComment(Attribute comment) {
		this.comment = comment;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
