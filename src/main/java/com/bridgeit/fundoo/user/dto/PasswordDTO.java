package com.bridgeit.fundoo.user.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class PasswordDTO {
	@NotEmpty(message = "Enter new password  ")
	@Length(min = 6, max = 12, message = "length :6 to 12 characters")
	private String newpassword;
	@NotEmpty(message = "Enter confirm password ")
	@Length(min = 6, max = 12, message = "length :6 to 12 characters")
	private String confirmpassword;

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getConfirmpassword() {
		return confirmpassword;
	}

	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}

	@Override
	public String toString() {
		return "PasswordDTO [newpassword=" + newpassword + ", confirmpassword=" + confirmpassword + "]";
	}

}