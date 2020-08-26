package com.aaronrenner.SteamAPI.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@Entity
@Table(name="friend_list")
@JsonIgnoreProperties({"id"})
public class FriendID {
	@Id
	@GeneratedValue
	private long id;
	private String steamID64;
}

