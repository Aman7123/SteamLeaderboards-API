package com.aaronrenner.SteamAPI.services;

import com.aaronrenner.SteamAPI.models.Token;
import com.aaronrenner.SteamAPI.models.User;

public interface LoginService {
	
	Token createToken(User user);
	User validateToken(String token);
	
}
