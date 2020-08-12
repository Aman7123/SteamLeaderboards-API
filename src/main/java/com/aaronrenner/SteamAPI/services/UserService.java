package com.aaronrenner.SteamAPI.services;

import java.util.List;

import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;

public interface UserService {
	
	List<User> getUserList();
	User createUser(User newUser);
	User getUser(String steamID64);
	User updateUser(String steamID64, User updateUser);
	boolean deleteUser(String steamID64);
	List<FriendID> getFriend(String steamID64);
	FriendID createFriend(String steamID64, String friendSteamID64);
	boolean deleteFriend(String steamID64, String friendSteamID64);
	
}
