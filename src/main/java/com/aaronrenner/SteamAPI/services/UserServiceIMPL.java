package com.aaronrenner.SteamAPI.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aaronrenner.SteamAPI.exceptions.UserExists;
import com.aaronrenner.SteamAPI.exceptions.UserNotFound;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.FriendRepository;
import com.aaronrenner.SteamAPI.repositories.UserRepository;

@Service
public class UserServiceIMPL implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	FriendRepository friendRepository;
	
	
	@Override
	public List<User> getUserList() {
		return userRepository.findAll();
	}

	@Override
	public User createUser(User newUser) {
		User checkUser = userRepository.findBySteamID64(newUser.getSteamID64());
		if(checkUser != null) {
			throw new UserExists(newUser.getSteamID64());
		} else {
			userRepository.save(newUser);
			return userRepository.findBySteamID64(newUser.getSteamID64());
		}
	}

	@Override
	public User getUser(String steamID64) {
		User userSearch = userRepository.findBySteamID64(steamID64);
		if( userSearch != null) {
			return userSearch;
		} else {
			throw new UserNotFound(steamID64);
		}
	}

	@Override
	public User updateUser(String steamID64, User updateUser) {
		// TODO learn patch
		return null;
	}

	@Override
	public void deleteUser(String steamID64) {
		User userSearch = getUser(steamID64);
		
		userRepository.delete(userSearch);
	}

	@Override
	public List<FriendID> getFriend(String steamID64) {
		User userSearch = getUser(steamID64);
			
		return userSearch.getFriendList();
	}

	@Override
	public FriendID createFriend(String steamID64, String friendSteamID64) {
		/** TODO
		User userSearch = getUser(steamID64);
		
		FriendID newFriend = new FriendID(userSearch);
		newFriend.setSteamID64(friendSteamID64);
		friendRepository.save(newFriend);
		return newFriend;
		*/
		return null;
	}

	@Override
	public void deleteFriend(String steamID64, String friendSteamID64) {
		/** TODO
		User userSearch = getUser(steamID64);
		
		friendRepository.deleteBySteamID64(friendSteamID64);
		*/
	}

}
