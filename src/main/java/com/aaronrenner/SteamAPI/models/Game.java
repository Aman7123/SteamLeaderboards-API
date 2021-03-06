package com.aaronrenner.SteamAPI.models;

import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@Entity
@Table(name = "game")

/**
 * This model represents the database for the game data I store
 * 
 * @author aaron renner
 * @version 1.0
 *
 */
public class Game {

	@Id
	private long id;
	private String title;

	public Game() {

	}

	public Game(long appid, String name) {
		this.id = appid;
		this.title=name;
	}
}
