package com.aaronrenner.SteamAPI.services;

import java.util.*;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;

public interface UserService {
	
	List<User> getUserList();
	User createUser(User newUser);
	User getUser(String steamID64);
	User updateUser(String steamID64, User updateUser);
	void deleteUser(String steamID64);
	List<FriendID> getFriend(String steamID64);
	FriendID createFriend(String steamID64, String friendSteamID64);
	void deleteFriend(String steamID64, String friendSteamID64);
	
}
