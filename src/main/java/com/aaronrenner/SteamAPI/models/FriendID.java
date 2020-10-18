package com.aaronrenner.SteamAPI.models;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@Entity
@Table(name="friend_list")
@JsonIgnoreProperties({"id"})

/**
 * This model represents the database and common referenced data model throughout this project. It is simply the two data variables below.
 * 
 * @author aaron renner
 * @version 1.0
 *
 */

public class FriendID {
	@Id
	@GeneratedValue
	private long id;
	private String steamID64;
}

