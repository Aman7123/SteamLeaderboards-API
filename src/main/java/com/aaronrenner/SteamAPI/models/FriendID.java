package com.aaronrenner.SteamAPI.models;

import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@Entity
@Table(name="friend_list")
public class FriendID {
	
	@Id
	private long id;
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	private String steamID64;

	public FriendID(User user) {
		this.user = user;

	}
	
}

