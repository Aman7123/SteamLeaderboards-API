package com.aaronrenner.SteamAPI.models;

import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@Entity
@Table(name = "game")
public class Game {
	
	@Id
	private long id;
	private String title;


}
