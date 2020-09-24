package com.aaronrenner.SteamAPI.repositories;

import java.util.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.aaronrenner.SteamAPI.models.User;

@DataJpaTest
public class UserRepositoryTest {
	
	private String steamID = "007";
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void findAll() {
		List<User> allUsers = userRepository.findAll();
	}
	
	@Test
	public void findOne() {
		Optional<User> oneUser = userRepository.findBySteamID64(steamID);
	}

	@Test
	public void createOne() {
		List<User> allUsers = userRepository.findAll();
		int allUsersSize = allUsers.size();
		
		User createNewUser = fakeUserModel();
		User saveNewUser = userRepository.save(createNewUser);
		allUsers = userRepository.findAll();
		
		assert(allUsersSize+1 == allUsers.size());
		
		userRepository.delete(saveNewUser);
	}

	private User fakeUserModel() {
		User newUser = new User();
		newUser.setUsername("mock_user");
		newUser.setSteamID64("000000");
		newUser.setPassword("1234");
		return newUser;
	}
}
