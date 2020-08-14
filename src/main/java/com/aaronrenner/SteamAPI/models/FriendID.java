package com.aaronrenner.SteamAPI.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@Entity
@Table(name="friend_list")
public class FriendID {
	
	@Id
	private long id;
	private String steamID64;
	
	@ManyToOne
	@JoinColumn(name="userId")
	@JsonIgnore
	private User user;

	public FriendID(User user) {
		this.user=user;
	}
	
}

