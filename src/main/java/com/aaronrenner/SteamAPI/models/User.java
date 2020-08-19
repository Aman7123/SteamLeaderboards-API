package com.aaronrenner.SteamAPI.models;

import java.util.*;
import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@Entity
@Table(name="user")
public class User {

	@Id
	private long id;
	private String username;
	private String steamID64;
	private String password;
	private String role;
	
	@OneToMany(mappedBy="user")
	private List<FriendID> friendList = new ArrayList<>(0);
	
}
