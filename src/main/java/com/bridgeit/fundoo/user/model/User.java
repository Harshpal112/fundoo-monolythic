package com.bridgeit.fundoo.user.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.bridgeit.fundoo.label.model.Labels;
import com.bridgeit.fundoo.note.model.Notes;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Component
@Entity
@Table
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userid;
	private long noteId;

	
	@NotEmpty(message="enter the emailid")
	@NotNull(message="enter the emailid")
	private String emailid;

	private String password;

	private String name;

	private String phnumber;
	private LocalDateTime registeredDate;
	private LocalDateTime modifiedDate;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Notes> notes;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Labels> labels;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Notes> notecollaborater;
	private boolean isverify;

//	public User(Long userid, long noteId,
//			@NotEmpty(message = "enter the emailid") @NotNull(message = "enter the emailid") String emailid,
//			String password, String name, String phnumber, LocalDateTime registeredDate, LocalDateTime modifiedDate,
//			List<Notes> notes, List<Labels> labels, List<Notes> notecollaborater, boolean isverify) {
//		super();
//		this.userid = userid;
//		this.noteId = noteId;
//		this.emailid = emailid;
//		this.password = password;
//		this.name = name;
//		this.phnumber = phnumber;
//		this.registeredDate = registeredDate;
//		this.modifiedDate = modifiedDate;
//		this.notes = notes;
//		this.labels = labels;
//		this.notecollaborater = notecollaborater;
//		this.isverify = isverify;
//	}

	public List<Notes> getNotecollaborater() {
		return notecollaborater;
	}

	public void setNotecollaborater(List<Notes> notecollaborater) {
		this.notecollaborater = notecollaborater;
	}

	public long getNoteId() {
		return noteId;
	}

	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}

	public List<Labels> getLabels() {
		return labels;
	}

	public void setLabels(List<Labels> labels) {
		this.labels = labels;
	}

	public long getId() {
		return noteId;
	}

	public void setId(long id) {
		this.noteId = id;
	}

	public List<Notes> getNotes() {
		return notes;
	}

	public void setNotes(List<Notes> notes) {
		this.notes = notes;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

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

	public LocalDateTime getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(LocalDateTime registeredDate) {
		this.registeredDate = registeredDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public boolean isIsverify() {
		return isverify;
	}

	public void setIsverify(boolean isverify) {
		this.isverify = isverify;
	}
	

	@Override
	public String toString() {
		return "User [userid=" + userid + ", noteId=" + noteId + ", emailid=" + emailid + ", password=" + password
				+ ", name=" + name + ", phnumber=" + phnumber + ", registeredDate=" + registeredDate + ", modifiedDate="
				+ modifiedDate + ", notes=" + notes + ", labels=" + labels + ", isverify=" + isverify + ", getNoteId()="
				+ getNoteId() + ", getLabels()=" + getLabels() + ", getId()=" + getId() + ", getNotes()=" + getNotes()
				+ ", getUserid()=" + getUserid() + ", getEmailid()=" + getEmailid() + ", getPassword()=" + getPassword()
				+ ", getName()=" + getName() + ", getPhnumber()=" + getPhnumber() + ", getRegisteredDate()="
				+ getRegisteredDate() + ", getModifiedDate()=" + getModifiedDate() + ", isIsverify()=" + isIsverify()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

}
