package com.bridgeit.fundoo.note.model;

import java.io.Serializable;
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

import com.bridgeit.fundoo.label.model.Labels;
import com.bridgeit.fundoo.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Entity
@Table(name = "Notes")
public class Notes implements Serializable{
	
	private static final long serialVersionUID = 7156526077883281623L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long userid;
	private String title;
	private String discription;
	private LocalDateTime registeredDate;
	private LocalDateTime modifiedDate;
	private boolean isPin;
	private boolean isTrash;
	private boolean isArchive;
	private long labelid;

	private LocalDateTime reminder;

	// @ManyToOne(cascade = CascadeType.ALL)
//	private User user;
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Labels> labels;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> usercollaborater;

	public List<User> getUsercollaborater() {
		return usercollaborater;
	}

	public void setUsercollaborater(List<User> usercollaborater) {
		this.usercollaborater = usercollaborater;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	private String color;

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public boolean isPin() {
		return isPin;
	}

	public void setPin(boolean isPin) {
		this.isPin = isPin;
	}

	public boolean isTrash() {
		return isTrash;
	}

	public void setTrash(boolean isTrash) {
		this.isTrash = isTrash;
	}

	public boolean isArchive() {
		return isArchive;
	}

	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}

	public long getLabelid() {
		return labelid;
	}

	public void setLabelid(long labelid) {
		this.labelid = labelid;
	}

	public List<Labels> getLabels() {
		return labels;
	}

	public void setLabels(List<Labels> labels) {
		this.labels = labels;
	}

	public LocalDateTime getReminder() {
		return reminder;
	}

	public void setReminder(LocalDateTime reminder) {
		this.reminder = reminder;
	}

	@Override
	public String toString() {
		return "Notes [id=" + id + ", userid=" + userid + ", title=" + title + ", discription=" + discription
				+ ", registeredDate=" + registeredDate + ", modifiedDate=" + modifiedDate + ", isPin=" + isPin
				+ ", isTrash=" + isTrash + ", isArchive=" + isArchive + ", labelid=" + labelid + ", reminder="
				+ reminder + ", labels=" + labels + ", usercollaborater=" + usercollaborater + ", color=" + color
				+ ", getUsercollaborater()=" + getUsercollaborater() + ", getColor()=" + getColor() + ", getUserid()="
				+ getUserid() + ", getId()=" + getId() + ", getTitle()=" + getTitle() + ", getDiscription()="
				+ getDiscription() + ", getRegisteredDate()=" + getRegisteredDate() + ", getModifiedDate()="
				+ getModifiedDate() + ", isPin()=" + isPin() + ", isTrash()=" + isTrash() + ", isArchive()="
				+ isArchive() + ", getLabelid()=" + getLabelid() + ", getLabels()=" + getLabels() + ", getReminder()="
				+ getReminder() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
