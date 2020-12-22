package com.app.sample.insta.model;

import java.io.Serializable;

public class Notif implements Serializable{
	private long id;
	private String date;
	private Friend friend;
	private String content;

	public Notif(long id, String date, Friend friend, String content) {
		this.id = id;
		this.date = date;
		this.friend = friend;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public Friend getFriend() {
		return friend;
	}

	public String getContent() {
		return "<b>"+friend.getName()+"</b> "+content;
	}
}