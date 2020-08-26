package com.aaronrenner.SteamAPI.models;

import java.util.*;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Entity
@Data
@Table(name="user")
@JsonIgnoreProperties({"id", "role"})
public class User {

	@Id
	@GeneratedValue
	private long id;
	private String username;
	private String steamID64;
	// TODO figure out why cannot use @JsonProperty WRITE_ONLY
	private String password;
	private String role;
	
	@OneToMany()
	private List<FriendID> friendList = new ArrayList<>();
	
}
