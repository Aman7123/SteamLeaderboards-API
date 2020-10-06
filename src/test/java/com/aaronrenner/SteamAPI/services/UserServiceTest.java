package com.aaronrenner.SteamAPI.services;

import static org.junit.Assert.assertNotNull;

import java.util.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.FriendRepository;
import com.aaronrenner.SteamAPI.repositories.UserRepository;

@SpringBootTest
public class UserServiceTest {
	
	@InjectMocks
	private UserServiceIMPL userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private FriendRepository friendRepository;
	
	private List<User> fakeUserList;
	private User fakeUser;
	private FriendID fakeFriend;
	
	@BeforeEach
	public void setUp() {
		
	}
	
	@AfterEach
	public void tearDown() {
		
	}
	
	
	@Test
	public void getUserList_Success() {

	}
	
	@Test
	public void getUserBySteamID_Success() {

	}
	
	@Test
	public void createUser_Success() {

	}
	
	@Test
	public void updateUser_Success() {


	}
	
	@Test
	public void getFriends() {
		List<FriendID> bufferFriends= new ArrayList<>();
		
		Mockito.when(friendRepository.findAll()).thenReturn(bufferFriends);
		
		List<FriendID> realFriendList = userService.getFriend("007");
		assertNotNull(realFriendList);
	}
	
	@Test
	public void addFriend() {

	}

	
	private List<User> fakeUserList() {
		List<User> userList = new ArrayList<>();
		userList.add(fakeUser(1));
		userList.add(fakeUser(2));
		return userList;
	}
	
	private User fakeUser(long number) {
		User newUser = new User();
		newUser.setId(number);
		newUser.setSteamID64("00" + number);
		newUser.setPassword("1234");
		newUser.setUsername("test_user");
		return newUser;
	}
	
	private FriendID fakeFriend() {
		
	}
}
