 package com.bridgeit.fundoo.label.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.bridgeit.fundoo.note.model.Notes;

@Component
@Entity
@Table
public class Labels {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long labelid;
	private long userid;
	private String labelname;
	private LocalDateTime registereddate;
	private LocalDateTime modifieddate;
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Notes> notes;
	private long noteid;
	public long getNoteid() {
		return noteid;
	}
	public void setNoteid(long noteid) {
		this.noteid = noteid;
	}
	public long getLabelid() {
		return labelid;
	}
	public void setLabelid(long labelid) {
		this.labelid = labelid;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getLabelname() {
		return labelname;
	}
	public void setLabelname(String labelname) {
		this.labelname = labelname;
	}
	public LocalDateTime getRegistereddate() {
		return registereddate;
	}
	public void setRegistereddate(LocalDateTime registereddate) {
		this.registereddate = registereddate;
	}
	public LocalDateTime getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(LocalDateTime modifieddate) {
		this.modifieddate = modifieddate;
	}
	public List<Notes> getNotes() {
		return notes;
	}
	public void setNotes(List<Notes> notes) {
		this.notes = notes;
	}
	@Override
	public String toString() {
		return "Labels [labelid=" + labelid + ", userid=" + userid + ", labelname=" + labelname + ", registereddate="
				+ registereddate + ", modifieddate=" + modifieddate + ", notes=" + notes + "]";
	}
	
}
