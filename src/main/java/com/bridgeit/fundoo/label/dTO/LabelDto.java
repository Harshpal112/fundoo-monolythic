package com.bridgeit.fundoo.label.dTO;

import javax.validation.constraints.NotEmpty;

public class LabelDto {
	@NotEmpty(message = "Enter the labelname")
	private String labelname;

	public String getLabelname() {
		return labelname;
	}

	public void setLabelname(String labelname) {
		this.labelname = labelname;
	}

	@Override
	public String toString() {
		return "LabelDto [labelname=" + labelname + "]";
	}

}
