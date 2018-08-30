package br.com.polieach.urbanassist.model;

import java.io.Serializable;

public class Attribute implements Serializable {

	String audio;
	int id;
	String text;
	String video;

	public Attribute() {
		
		this.text = "";
		this.audio = "";
		this.video = "";
	}

	public Attribute(String text, String audio, String video) {

		this.text = text;
		this.audio = audio;
		this.video = video;
	}

	public String getAudio() {
		return audio;
	}

	public int getID() {
		return id;
	}

	public String getText() {
		return text;
	}

	public String getVideo() {
		return video;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setText(String texto) {
		this.text = texto;
	}

	public void setVideo(String video) {
		this.video = video;
	}

}