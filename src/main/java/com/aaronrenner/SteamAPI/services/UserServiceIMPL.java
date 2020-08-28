package com.aaronrenner.SteamAPI.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aaronrenner.SteamAPI.exceptions.FriendExists;
import com.aaronrenner.SteamAPI.exceptions.FriendNotFound;
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
		Optional<User> checkUser = userRepository.findBySteamID64(newUser.getSteamID64());
		if(checkUser.isPresent()) {
			throw new UserExists(newUser.getSteamID64());
		} else {
			return userRepository.save(newUser);
		}
	}

	@Override
	public User getUser(String steamID64) {
		Optional<User> userSearch = userRepository.findBySteamID64(steamID64);
		if(userSearch.isPresent()) {
			User user = userSearch.get();
			return user;
		} else {
			throw new UserNotFound(steamID64);
		}
	}

	@Override
	// TODO dont let change steamID to something that exists if it did!
	public User updateUser(String steamID64, User updateUser) {
		System.out.println("fix me UserService-55");
		User storedUserModel = getUser(steamID64);
		if(updateUser.getSteamID64() != null) {
			storedUserModel.setSteamID64(updateUser.getSteamID64());
		} if(updateUser.getUsername() != null) {
			storedUserModel.setUsername(updateUser.getUsername());
		} if(updateUser.getPassword() != null) {
			storedUserModel.setPassword(updateUser.getPassword());
		}
		return this.userRepository.save(storedUserModel);
	}

	@Override
	public void deleteUser(String steamID64) {
		// User is checked for existence by line 
		User userSearch = getUser(steamID64);

		this.userRepository.delete(userSearch);			


	}

	@Override
	public List<FriendID> getFriend(String steamID64) {
		// User is checked for existence by line 
		User userSearch = getUser(steamID64);
		
		return userSearch.getFriendList();

	}
	
	@Override
	// TODO Check that id's are within size
	// TODO dont let add user as self friend
	public FriendID createFriend(String steamID64, String friendSteamID64) {
		// User is checked for existence by line 
		User userSearch = getUser(steamID64);

		FriendID newFriend = new FriendID();
		newFriend.setSteamID64(friendSteamID64);
		
		//Check before continuing
		for(FriendID fID : userSearch.getFriendList()) {
			if(fID.getSteamID64().equals(friendSteamID64)) {
				throw new FriendExists(steamID64, friendSteamID64); // Will not precede if fails
			}
		}

		newFriend = friendRepository.save(newFriend);
		userSearch.getFriendList().add(newFriend);
		userRepository.save(userSearch);
		return newFriend;
	}

	@Override
	public void deleteFriend(String steamID64, String friendSteamID64) {
		// User is checked for existence by line 
		User userSearch = getUser(steamID64);
		boolean friendExist = false;
		FriendID userFriend = null;
		
		for(FriendID fID : userSearch.getFriendList()) {
			if(fID.getSteamID64().equals(friendSteamID64)) {
				friendExist = true;
				userFriend = fID;
				
			}
		}
		if(friendExist) {
			userSearch.getFriendList().remove(userFriend);
			friendRepository.delete(userFriend);
		} else {
			throw new FriendNotFound(steamID64, friendSteamID64);
		}
		
	}

}
