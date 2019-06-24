package com.bridgeit.fundoo.user.dto;

import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
public class UserDTO {
	@NotEmpty(message = "Enter email address")
	@Pattern(regexp = "^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$", message = "enetr valid email address..!")
	private String emailid;
	@NotEmpty(message = "Enter password ...!")
	@Length(min = 8, max = 32, message = "length :6 to 32 characters")
	private String password;
	@NotEmpty(message = "Enter Name")
	@Length(min = 3, max = 32, message = "length :3 to 32 characters")
	private String name;
	@NotEmpty(message = "Enter mobile number")

// 	@Pattern(regexp = "[7-9] {1}[0-9]{9}", message = "Enter 10-digit mobile number") 

	private String phnumber;

//	public UserDTO(
//			@NotEmpty(message = "Enter email address") @Pattern(regexp = "^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$", message = "enetr valid email address..!") String emailid,
//			@NotEmpty(message = "Enter password ...!") @Length(min = 8, max = 32, message = "length :6 to 32 characters") String password,
//			@NotEmpty(message = "Enter Name") @Length(min = 3, max = 32, message = "length :3 to 32 characters") String name,
//			@NotEmpty(message = "Enter mobile number") String phnumber) {
//		super();
//		this.emailid = emailid;
//		this.password = password;
//		this.name = name;
//		this.phnumber = phnumber;
//	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhnumber() {
		return phnumber;
	}

	public void setPhnumber(String phnumber) {
		this.phnumber = phnumber;
	}
}
