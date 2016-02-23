package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="friends")
public class UserFriendsResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<UserFriend> friends = new ArrayList<UserFriend>();
	
	private Integer count;

	public List<UserFriend> getFriends() {
		return friends;
	}

	public void setFriends(List<UserFriend> friends) {
		this.friends = friends;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
