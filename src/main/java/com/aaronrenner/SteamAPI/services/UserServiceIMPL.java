package com.aaronrenner.SteamAPI.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aaronrenner.SteamAPI.exceptions.BadRequestError;
import com.aaronrenner.SteamAPI.exceptions.FriendExists;
import com.aaronrenner.SteamAPI.exceptions.FriendNotFound;
import com.aaronrenner.SteamAPI.exceptions.UserNotFound;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.FriendRepository;
import com.aaronrenner.SteamAPI.repositories.UserRepository;
import com.aaronrenner.SteamAPI.security.PasswordEncoder;

@Service
/**
 * 
 * @author aaron renner
 * @version 1.0
 *
 */
public class UserServiceIMPL implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	FriendRepository friendRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	// This value controls how long the password should be in all cases
	private final int PASSWORD_MIN_LENGTH = 4;


	@Override
	public List<User> getUserList() {
		return userRepository.findAll();
	}

	@Override
	public User createUser(User newUser) {
		Optional<User> checkUser = userRepository.findBySteamID64(newUser.getSteamID64());
		Optional<User> checkForUserByUsername = userRepository.findByUsername(newUser.getUsername());
		if(!checkUser.isPresent() && !checkForUserByUsername.isPresent()) {
			// TODO add password minimum length
			if(newUser.getPassword() != null && newUser.getSteamID64() != null && newUser.getUsername() != null) {
				if(checkSteamID(newUser.getSteamID64())) {
					// Set default
					newUser.setRole("user");
					newUser.setId(0);
					// Encode password
					String encodePassword = passwordEncoder.encodePassword(newUser.getPassword());
					newUser.setPassword(encodePassword);

					return userRepository.save(newUser);
				} else {
					throw new BadRequestError("The steamID was not of valid format");
				}
			} else {
				throw new BadRequestError("Missing or incomplete \"username\", \"steamID64\" and \"password\" fields");
			}
		}
		throw new BadRequestError("Missing or incomplete \"username\", \"steamID64\" and \"password\" fields");
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
	public User updateUser(String steamID64, User updateUser) {
		User storedUserModel = getUser(steamID64);
		// If user model is not null
		if(updateUser != null) {
			// If they include an update for user name
			if(updateUser.getUsername() != null) {
				if(!userRepository.existsByUsername(updateUser.getUsername()) || (storedUserModel.getUsername().equals(updateUser.getUsername()))) {
					storedUserModel.setUsername(updateUser.getUsername());	
				} else {
					throw new BadRequestError("That username is in use");
				}
			}
			// If they include update for password
			if(updateUser.getPassword() != null) {
				if(updateUser.getPassword().length() >= PASSWORD_MIN_LENGTH) {
					// Update the encoded password
					String encodedPassword = passwordEncoder.encodePassword(updateUser.getPassword());
					storedUserModel.setPassword(encodedPassword);		
				} else {
					throw new BadRequestError(String.format("Password should be longer than: %d", PASSWORD_MIN_LENGTH));
				}

			}
			// If they include update for steamID64
			if(updateUser.getSteamID64() != null) {
				// Make sure steamID is of proper length
				if(checkSteamID(updateUser.getSteamID64())) {
					// Then make sure not being used
					if(!userRepository.existsBySteamID64(updateUser.getSteamID64()) || (storedUserModel.getSteamID64().equals(updateUser.getSteamID64()))) {
						// Then update
						storedUserModel.setSteamID64(updateUser.getSteamID64());
					} else {
						throw new BadRequestError("That steamID is already in use");
					}	
				} else {
					throw new BadRequestError("The steamID supplied must be min&max 17 digits, check the entry and try again");
				}

			}	
		} else {
			throw new BadRequestError("Update the \"username\", \"password\", or \"steamID64\" fields");
		}

		return userRepository.save(storedUserModel);
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
	// TODO fix
	public FriendID createFriend(String steamID64, String friendSteamID64) {
		// Makes sure user must be lonely and cannot friend self
		if(steamID64.equals(friendSteamID64)) {
			throw new BadRequestError("You want to be friends with yourself? Weirdo.");
		}

		// Check format of new friend entry
		if(!checkSteamID(friendSteamID64)) {
			throw new BadRequestError("Check the format of the friend steamID");
		}
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

	private boolean checkSteamID(String steamID64) {
		int inputlength = steamID64.length();
		boolean isNumbers = steamID64.matches("[0-9]+");
		if(inputlength == 17 && isNumbers) {
			return true;
		}
		return false;
	}

}
