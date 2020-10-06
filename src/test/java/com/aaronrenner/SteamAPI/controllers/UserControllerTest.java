package com.aaronrenner.SteamAPI.controllers;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.TokenTest;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	
	private String adminToken = "Bearer " + new TokenTest().getToken();
	ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private UserService userService;
	
	@Before
	public void setUp() throws Exception {
		userService.deleteFriend("007", "12345678901234567");
		userService.deleteUser("12345678901234567");
	}
	
	@Test
	public void lookupAllUsers() throws Exception {
		List<User> allUserList = new ArrayList<>();
		allUserList.add(fakeUserModel());
		allUserList.add(fakeAdminModel());
		Mockito.when(userService.getUserList()).thenReturn(allUserList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Authorization", adminToken)).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
		
		User[] users = om.readValue(mvcResult.getResponse().getContentAsString(), User[].class);
		assert(users.length > 0);
	}
	
	@Test 
	public void lookupSelf() throws Exception {
		User testUser = new User();
		testUser.setSteamID64("007");
		
		Mockito.when(userService.getUser("007")).thenReturn(testUser);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users" + "/" + testUser.getSteamID64()).header("Authorization", adminToken)).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
		
		User responseUser = om.readValue(mvcResult.getResponse().getContentAsString(), User.class);
		assert(responseUser.getUsername().equals("test_user"));
	}
	
	@Test
	public void createUser() throws Exception {
		//TODO finish this post
		User bufferUser = fake_realUser();
		String bufferUser_JSON = om.writeValueAsString(bufferUser);
		
		Mockito.when(userService.createUser(bufferUser)).thenReturn(bufferUser);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(bufferUser_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
		
		User bufferUser_1 = fake_realUser();
		MvcResult mvcResult_1 = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + bufferUser_1.getSteamID64()).header("Authorization", adminToken).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

	}	
	
	@Test
	public void updateUser() throws Exception {
		User bufferUser = new User();
		bufferUser.setUsername("test_user");
		bufferUser.setPassword("1234");
		
		String bufferUser_JSON = om.writeValueAsString(bufferUser);
		
		User jBond = userService.getUser("007");
		
		Mockito.when(userService.updateUser("007", bufferUser)).thenReturn(jBond);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/007").header("Authorization", adminToken).contentType(MediaType.APPLICATION_JSON).content(bufferUser_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isAccepted()).andReturn();
	}
	
	
	@Test
	public void getFriends() throws Exception {
		List<FriendID> bufferFriends = new ArrayList<>();
		bufferFriends.add(fakeFriendModel());
		
		Mockito.when(userService.getFriend("007")).thenReturn(bufferFriends);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/007/friends-list").header("Authorization", adminToken).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		
	}
	
	@Test
	public void addFriendandDelete() throws Exception {
		FriendID bufferFriend = fakeFriendModel();
		
		Mockito.when(userService.createFriend("007", bufferFriend.getSteamID64())).thenReturn(bufferFriend);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/007/friends-list/12345678901234567").header("Authorization", adminToken).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

		
		MvcResult mvcResult_2 = mockMvc.perform(MockMvcRequestBuilders.delete("/users/007/friends-list/12345678901234567").header("Authorization", adminToken).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

	}

}
