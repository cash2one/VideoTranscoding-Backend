package es.urjc.videotranscoding.restController;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.urjc.videotranscoding.entities.User;
import es.urjc.videotranscoding.entities.UserRoles;
import es.urjc.videotranscoding.repository.UserRepository;
import es.urjc.videotranscoding.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserRestController.class, secure = false)
@ContextConfiguration(locations = { "classpath:/xml/ffmpeg-config-test.xml" })
public class UserRestControllerTest {

	private User u1;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserRestController userRestController;

	@MockBean
	private UserService userService;

	@Before
	public void setup() {
		u1 = new User("patio@gmail.com", "admin", "pass", "", UserRoles.ADMIN, UserRoles.USER);
		userService.save(u1);
	}

	@Test
	public void getListUsers() throws Exception {
		List<User> listUsers = new ArrayList<>();
		Mockito.when(userService.findAllUsers()).thenReturn(listUsers);
		listUsers.add(u1);
		String URL_USER = "/user";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_USER).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String outputInJson = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<List<User>> mapType = new TypeReference<List<User>>() {
		};
		List<User> expected = objectMapper.readValue(outputInJson, mapType);
		assertEquals(expected.size(), listUsers.size());
	}

	@Test
	public void getOneUser() throws Exception {
		Mockito.when(userService.findOneUser("admin")).thenReturn(u1);
		String URL_USER_ADMIN = "/user/admin";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_USER_ADMIN).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String outputInJson = result.getResponse().getContentAsString();
		ObjectMapper mapper2 = new ObjectMapper();
		User expected = mapper2.readValue(outputInJson, User.class);
		assertEquals(expected.getNick(), u1.getNick());
		assertEquals(expected.getEmail(), u1.getEmail());
	}

	@Test
	public void registerUser() throws Exception {
		User newUser = new User("email@gmail.com", "newAdmin", "12345678", "", UserRoles.USER);
		Mockito.when(userService.registerUser(newUser)).thenReturn(newUser);
		String URL_REGISTER_USER = "/user/register";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_REGISTER_USER)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("{}");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String outputInJson = result.getResponse().getContentAsString();
		ObjectMapper mapper2 = new ObjectMapper();
		User expected = mapper2.readValue(outputInJson, User.class);
		assertEquals(expected.getNick(), "newAdmin");
		assertEquals(expected.getEmail(), "email@gmail.com");
	}

	@Test
	public void editUser() throws Exception {
		User newUser = new User("email@gmail.com", "newAdmin", "12345678", "", UserRoles.USER);
		Mockito.when(userService.registerUser(newUser)).thenReturn(newUser);
		String URL_REGISTER_USER = "/user/register";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_REGISTER_USER)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("{}");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String outputInJson = result.getResponse().getContentAsString();
		ObjectMapper mapper2 = new ObjectMapper();
		User expected = mapper2.readValue(outputInJson, User.class);
		assertEquals(expected.getNick(), "newAdmin");
		assertEquals(expected.getEmail(), "email@gmail.com");
	}

	@Test
	public void deleteUser() throws Exception {
		User newUser = new User("email@gmail.com", "newAdmin", "12345678", "", UserRoles.USER);
		userService.deleteUser(newUser);
		String URL_DELETE_USER = "/user";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL_DELETE_USER)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("{}");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(result.getResponse().getStatus(), 401);
	}
}
