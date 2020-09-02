package com.aaronrenner.SteamAPI.services;

import java.util.List;
import java.util.Optional;

import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.models.Token;
import com.aaronrenner.SteamAPI.models.User;

public interface LoginService {
	
	Token createToken(User user);
	User validateToken(String token);
	
}
