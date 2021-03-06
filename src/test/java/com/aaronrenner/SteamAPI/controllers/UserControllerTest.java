package com.aaronrenner.SteamAPI.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.TokenTest;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.services.UserServiceIMPL;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
	
	private String adminToken = "Bearer " + new TokenTest().getToken();
	private String userToken = "Bearer " + new TokenTest().getToken_user();
	private String fakeToken = "PoopFace " + new TokenTest().getToken_user();
	ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserServiceIMPL userService;

	
	private List<User> fakeUserList;
	private User fakeUser;
	private List<FriendID> fakeFriendList;
	private FriendID fakeFriend;
	
	@BeforeEach
	public void SetUp() {
		fakeUserList = fakeUserList();
		fakeUser = fakeUser(3);
		fakeFriendList = fakeFriendList();
		fakeFriend = fakeFriend("12345678901234567");
		
		Mockito.when(userService.getUserList()).thenReturn(fakeUserList);
		Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(fakeUser);
		Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(fakeUser);
		Mockito.when(userService.updateUser(Mockito.anyString(), Mockito.any(User.class))).thenReturn(fakeUser);
		Mockito.when(userService.getFriend(Mockito.anyString())).thenReturn(fakeFriendList);
		Mockito.when(userService.createFriend(Mockito.anyString(), Mockito.anyString())).thenReturn(fakeFriend);
	}
	
	@AfterEach
	public void tearDown() {
		fakeUserList = null;
		fakeUser = null;
		fakeFriendList = null;
		fakeFriend = null;
	}
	
	@Test
	private void injectedComponentsAreNotNull() {
		assertNotNull(mockMvc);
		assertNotNull(userService);
	}
	
	
	@Test
	public void lookupAllUsers() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Authorization", adminToken)).andReturn();
		User[] realUserList = om.readValue(mvcResult.getResponse().getContentAsString(), User[].class);
		
		assert(200 == mvcResult.getResponse().getStatus());
		assert(realUserList.length > 0);
		
		// For Error
		MvcResult mvcResult_Error = mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Authorization", userToken)).andReturn();
		assert(401 == mvcResult_Error.getResponse().getStatus());
		
		// For Error Bad Token
		MvcResult mvcResult_Error_Token = mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Authorization", fakeToken)).andReturn();
		assert(401 == mvcResult_Error_Token.getResponse().getStatus());
	}
	
	@Test
	public void lookupUserBySteamID() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/007").header("Authorization", adminToken)).andReturn();	
		User realUser = om.readValue(mvcResult.getResponse().getContentAsString(), User.class);
		
		assert(200 == mvcResult.getResponse().getStatus());
		assert(Integer.valueOf(realUser.getSteamID64()) == Integer.valueOf(fakeUser.getSteamID64()));
		
		// For Error
		MvcResult mvcResult_Error = mockMvc.perform(MockMvcRequestBuilders.get("/users/007").header("Authorization", userToken)).andReturn();	
		assert(401 == mvcResult_Error.getResponse().getStatus());
	}
	
	@Test
	public void updateUser() throws Exception {
		String updatedUser_AsJSON = om.writeValueAsString(fakeUser);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/003").header("Authorization", adminToken).contentType(MediaType.APPLICATION_JSON).content(updatedUser_AsJSON).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		assert(202 == mvcResult.getResponse().getStatus());
		
		// For Error
		MvcResult mvcResult_Error = mockMvc.perform(MockMvcRequestBuilders.patch("/users/007").header("Authorization", userToken).contentType(MediaType.APPLICATION_JSON).content(updatedUser_AsJSON)).andReturn();	
		assert(401 == mvcResult_Error.getResponse().getStatus());
	}
	
	@Test
	public void addUser() throws Exception {
		String user_AsJSON = om.writeValueAsString(fakeUser);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(user_AsJSON).accept(MediaType.APPLICATION_JSON)).andReturn();
		assert(201 == mvcResult.getResponse().getStatus());
	}
	
	@Test
	public void lookupFriendsList() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/007/friends-list").header("Authorization", adminToken)).andReturn();
		FriendID[] realFriendList = om.readValue(mvcResult.getResponse().getContentAsString(), FriendID[].class);
		
		assert(200 == mvcResult.getResponse().getStatus());
		assert(realFriendList.length > 0);
		
		//For Error
		MvcResult mvcResult_Error = mockMvc.perform(MockMvcRequestBuilders.get("/users/007/friends-list").header("Authorization", userToken)).andReturn();
		assert(401 == mvcResult_Error.getResponse().getStatus());
	}
	
	@Test
	public void addFriend() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/007/friends-list/007").header("Authorization", adminToken)).andReturn();
		assert(201 == mvcResult.getResponse().getStatus());
		
		// For Error
		MvcResult mvcResult_Error = mockMvc.perform(MockMvcRequestBuilders.post("/users/007/friends-list/007").header("Authorization", userToken)).andReturn();
		assert(401 == mvcResult_Error.getResponse().getStatus());
	}
	
	// TODO more in-depth delete
	@Test
	public void deleteUser() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/12345678901234567").header("Authorization", adminToken)).andDo(MockMvcResultHandlers.print()).andReturn();
		
		// For Error
		MvcResult mvcResult_Error = mockMvc.perform(MockMvcRequestBuilders.delete("/users/12345678901234567").header("Authorization", userToken)).andReturn();

	}
	
	// TODO more in-depth delete
	@Test
	public void deleteFriend() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/12345678901234567/friends-list/12345678901234567").header("Authorization", adminToken)).andReturn();
		
		// For Error
		MvcResult mvcResult_Error = mockMvc.perform(MockMvcRequestBuilders.delete("/users/12345678901234567/friends-list/12345678901234567").header("Authorization", userToken)).andReturn();
		MvcResult mvcResult_Error2 = mockMvc.perform(MockMvcRequestBuilders.delete("/users/12345678901234567/friends-list/12345678901234567")).andReturn();
	}
	
	@Test
	public void noLoginToken() throws Exception {
		MvcResult mvcResult_UserList = mockMvc.perform(MockMvcRequestBuilders.get("/users")).andReturn();
		assert(401 == mvcResult_UserList.getResponse().getStatus());
		MvcResult mvcResult_User = mockMvc.perform(MockMvcRequestBuilders.get("/users/76561198089525491")).andReturn();
		assert(401 == mvcResult_User.getResponse().getStatus());
		MvcResult mvcResult_UpdateUser = mockMvc.perform(MockMvcRequestBuilders.patch("/users/76561198089525491").contentType(MediaType.APPLICATION_JSON).content("{}")).andReturn();
		assert(401 == mvcResult_UpdateUser.getResponse().getStatus());
		MvcResult mvcResult_DeleteUser = mockMvc.perform(MockMvcRequestBuilders.delete("/users/76561198089525491")).andReturn();
		assert(401 == mvcResult_DeleteUser.getResponse().getStatus());
		MvcResult mvcResult_GetFriends = mockMvc.perform(MockMvcRequestBuilders.get("/users/76561198089525491/friends-list")).andReturn();
		assert(401 == mvcResult_GetFriends.getResponse().getStatus());
		MvcResult mvcResult_AddFriends = mockMvc.perform(MockMvcRequestBuilders.post("/users/76561198089525491/friends-list/12345678901234567")).andReturn();
		assert(401 == mvcResult_AddFriends.getResponse().getStatus());
		
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
		newUser.getFriendList().add(fakeFriend("12345678901234567"));
		return newUser;
	}
	
	private List<FriendID> fakeFriendList() {
		List<FriendID> friendList = new ArrayList<>();
		friendList.add(fakeFriend("12345678901234567"));
		return friendList;
	}
	
	private FriendID fakeFriend(String IDToHave) {
		FriendID newFriend = new FriendID();
		newFriend.setSteamID64(IDToHave);
		return newFriend;
	}
}
