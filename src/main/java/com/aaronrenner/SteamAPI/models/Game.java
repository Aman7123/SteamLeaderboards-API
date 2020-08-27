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
 * @author aaronrenner
 *
 */
public class Game {
	
	@Id
	private long id;
	private String title;


}
