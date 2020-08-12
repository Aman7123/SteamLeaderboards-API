package com.aaronrenner.SteamAPI.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;

@Service
public class UserServiceIMPL implements UserService {

	private ArrayList<User> userList;
	
	public UserServiceIMPL() {
		this.userList = new ArrayList<>();
	}
	
	@Override
	public List<User> getUserList() {
		return this.userList;
	}

	@Override
	public User createUser(User newUser) {
		if(userList.contains(newUser)) {
		} else {
			userList.add(newUser);
			return newUser;
		}
		return null;
	}

	@Override
	public User getUser(String steamID64) {
		for(User userSearch: userList) {
			if(userSearch.getSteamID64().equals(steamID64)) {
				return userSearch;
			}
		}
		return null;
	}

	@Override
	public User updateUser(String steamID64, User updateUser) {
		for(User userSearch: userList) {
			if(userSearch.getSteamID64().equals(steamID64)) {
				System.out.println(updateUser);
			}
		}
		return null;
	}

	@Override
	public boolean deleteUser(String steamID64) {
		for(User userSearch: userList) {
			if(userSearch.getSteamID64().equals(steamID64)) {
				return userList.remove(userSearch);
			}
		}
		return false;
	}

	@Override
	public List<FriendID> getFriend(String steamID64) {
		List<FriendID> bufferFriends = null;
		for(User userSearch: userList) {
			if(userSearch.getSteamID64().equals(steamID64)) {
				return userSearch.getFriendList();
			}
		}
		return bufferFriends;
	}

	@Override
	public FriendID createFriend(String steamID64, String friendSteamID64) {
		for(User userSearch: userList) {
			if(userSearch.getSteamID64().equals(steamID64)) {
				FriendID newFriend = new FriendID(userSearch.getId());
				newFriend.setSteamID64(friendSteamID64);
				userSearch.addFriend(newFriend);
				return newFriend;
			}
		}
		return null;
	}

	@Override
	public boolean deleteFriend(String steamID64, String friendSteamID64) {
		for(User userSearch: userList) {
			if(userSearch.getSteamID64().equals(steamID64)) { //end check user
				for(FriendID friendSearch : userSearch.getFriendList()) {
					if(friendSearch.getSteamID64().equals(friendSteamID64)) { //end check friend
						return userSearch.getFriendList().remove(friendSearch);
					}
				}
			}
		}
		return false;
	}

}
