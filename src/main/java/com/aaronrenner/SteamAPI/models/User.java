package com.aaronrenner.SteamAPI.models;

import java.util.*;
import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Entity
@Data
@Table(name="user")
public class User {

	@Id
	@GeneratedValue
	private long id;
	private String username;
	private String steamID64;
	// TODO figure out why cannot use @JsonProperty WRITE_ONLY
	private String password;
	// TODO default value for role
	private String role;
	
	@OneToMany()
	private List<FriendID> friendList = new ArrayList<>();
	
}
