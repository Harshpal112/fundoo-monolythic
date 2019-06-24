package com.bridgeit.fundoo.note.dTO;

import javax.validation.constraints.NotEmpty;

public class NotesDto {
	@NotEmpty(message = "Enter the title")
	private String title;
	@NotEmpty(message = "Enter the discription")
	private String discription;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	@Override
	public String toString() {
		return "NotesDto [title=" + title + ", discription=" + discription + "]";
	}

}
