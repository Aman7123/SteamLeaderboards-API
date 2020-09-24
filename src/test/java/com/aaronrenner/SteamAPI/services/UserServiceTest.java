package com.aaronrenner.SteamAPI.services;

import static org.junit.Assert.assertNotNull;

import java.util.*;
import org.junit.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.UserRepository;

@SpringBootTest
public class UserServiceTest {
	
	@InjectMocks
	private UserServiceIMPL userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Test
	public void getAllUsersTest() {
		List<User> allUserList = new ArrayList<>();
		allUserList.add(fakeUserModel());
		allUserList.add(fakeAdminModel());
		Mockito.when(userRepository.findAll()).thenReturn(allUserList);
		
		List<User> realUsersList = userService.getUserList();
		assertNotNull(realUsersList);
		
	}
	
	@Test
	public void getUserBySteamID() {
		Mockito.when(userRepository.findBySteamID64("007")).thenReturn(Optional.of(fakeUserModel()));
		
		User newUser = userService.getUser("007");
		assertNotNull(newUser);
	}
	
	@Test
	public void addUser() {
		userService.createUser(fake_realUser());
	}
	
	@Test
	public void updateUser() {
		Optional<User> fakeUser = findTestUser();
		if(fakeUser.isPresent()) {
			Mockito.when(userRepository.findBySteamID64(fakeUser.get().getSteamID64())).thenReturn(fakeUser);			
		}

	}
	
	@Test
	public void deleteUser() {
		Optional<User> pullUser = findTestUser();
		if(pullUser.isPresent()) {
			userService.deleteUser(pullUser.get().getSteamID64());
		}
	}
	
	// TODO code addFriend, deleteFriend and updateFriend
	
	private User fakeUserModel() {
		User newUser = new User();
		newUser.setUsername("mock_user");
		newUser.setSteamID64("000000");
		newUser.setPassword("1234");
		return newUser;
	}
	
	private User fakeAdminModel() {
		User newUser = new User();
		newUser.setUsername("mock_admin");
		newUser.setSteamID64("000000");
		newUser.setPassword("1234");
		return newUser;
	}
	
	private User fake_realUser() {
		User newUser = new User();
		newUser.setUsername("fake_user");
		newUser.setSteamID64("12345678901234567");
		newUser.setPassword("1234");
		return newUser;
	}
	
	private Optional<User> findTestUser() {
		return userRepository.findByUsername("fake_user");
	}
}
