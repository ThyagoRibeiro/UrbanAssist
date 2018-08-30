package br.com.urbanassist.model;

import java.io.Serializable;

public class Attribute implements Serializable {
 
	String text;
	String audio;
	String video;

	public Attribute() {

		this.text = "";
		this.audio = "";
		this.video = "";
	}

	public Attribute(String text, String audio, String video) {

		if (text != null)
			this.text = text;
		else
			this.text = "";

		if (audio != null)
			this.audio = audio;
		else
			this.audio = "";

		if (video != null)
			this.video = video;
		else
			this.video = "";
	}

	public String getAudio() {
		return audio;
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
	
	public void setText(String texto) {
		this.text = texto;
	}

	public void setVideo(String video) {
		this.video = video;
	}

}