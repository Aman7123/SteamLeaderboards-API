package com.aaronrenner.SteamAPI.models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Data
@Table(name="user")
@JsonIgnoreProperties({"id", "role"})

/**
 * This class represents the data structure of a user object in code and in the database, this class creates its own data tables
 * 
 * @author aaronrenner
 *
 */

public class User {

	@Id
	@GeneratedValue
	private long id;
	private String username;
	private String steamID64;
	private String password;
	private String role;
	
	@OneToMany()
	private List<FriendID> friendList = new ArrayList<>();
	
}
