package com.bridgeit.fundoo.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class LoginDTO {
	@NotEmpty(message = "Enter valid email address")
	@Pattern(regexp = "^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$", message = "enetr valid email address..!")
	private String emailid;
	@NotEmpty(message = "Enter password ")
	@Length(min = 6, max = 12, message = "length :6 to 12 characters")
	private String password;

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginDTO [emailid=" + emailid + ", password=" + password + "]";
	}

}
