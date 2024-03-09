package com.testtasks.CRONapp.entities;

import java.time.Instant;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

//Entity if news. Using this we can create repo to get crud methods for free.
//And also we will write some hql queries using this Entity.
public class News implements Persistable<String> {
	//here defined fields whitch are connected with attributes of our created table 'news'.
	@Id 
	private String headline;
	private String description;
	private Instant publicationTime;
//	constructors
    public News() {}
	public News(String headline, String description, Instant publicationTime) {
		this.headline = headline;
		this.description = description;
		this.publicationTime = publicationTime;
	}
//	getters setters
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public Instant getPublicationTime() {
		return publicationTime;
	}
	public void setPublicationTime(Instant date) {
		this.publicationTime = date;
	}
//	some methods to inform r2dbc is object new to save 
//	or it is already present in db to update
	@Transient
    private boolean isNew;

    @Override
    public String getId() {
        return headline;
    }

    @Override
    public boolean isNew() {
        return this.isNew || headline == null;
    }
    public News setAsNew() {
        this.isNew = true;
        return this;
    }
	@Override
	public String toString() {
		return "News [headline=" + headline + ", description=" + description + ", publicationTime=" + publicationTime
				+ ", isNew=" + isNew + "]";
	}


}
