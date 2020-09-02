package com.aaronrenner.SteamAPI.models;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data

/**
 * This model represents the authorization token sent when logging in
 * 
 * @author aaronrenner
 *
 */
public class Token {
	
	private String token;
	
	public Token(String newToken) {
		this.token = newToken;
	}
}
