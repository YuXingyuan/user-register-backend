package com.interview.userregister;

import com.interview.userregister.model.request.UserRequest;
import com.interview.userregister.repository.UserRegisterRepository;
import com.interview.userregister.repository.dao.RegisterUser;
import com.interview.userregister.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserregisterApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRegisterRepository userRegisterRepository;

	@MockBean
	private JavaMailSender javaMailSender;

	private UUID testId;

	@BeforeEach
	void buildUp() {
		RegisterUser registerUser = RegisterUser.builder()
				.name("Test")
				.email("test@gmail.com")
				.age(30)
				.active(Boolean.TRUE)
				.emailSent(Boolean.TRUE)
				.build();

		userRegisterRepository.saveAndFlush(registerUser);

		testId = registerUser.getId();
	}

	@AfterEach
	void tearDown() {
		userRegisterRepository.deleteAll();
	}

	@Test
	void testAddUserWithSuccess() throws Exception {
		doNothing().when(javaMailSender).send(any(MimeMessagePreparator.class));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.stringifyObject(UserRequest.builder()
								.name("Jack")
								.email("jack@gmail.com")
								.age(25)
								.build())))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());

		verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
	}

	@Test
	void testAddUserWhenInvalidEmailWithFailure() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.stringifyObject(UserRequest.builder()
								.name("Jack")
								.email("invalid email")
								.age(25)
								.build())))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User email format is invalid; "));

		verify(javaMailSender, never()).send(any(MimeMessagePreparator.class));
	}

	@Test
	void testEditUserWithSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/edit/" + testId.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.stringifyObject(UserRequest.builder()
								.name("Marry")
								.email("test@gmail.com")
								.age(45)
								.build())))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testId.toString()));
	}

	@Test
	void testEditUserWithFailureNotFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/edit/" + "8a6156fb-300b-48bf-b72e-87adf716dbbf")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.stringifyObject(UserRequest.builder()
								.name("Marry")
								.email("test@gmail.com")
								.age(45)
								.build())))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));
	}

	@Test
	void testDeleteUserWithSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/delete/" + testId.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testId.toString()));
	}

	@Test
	void testDeleteSelectedUserWithSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/delete/selected")
						.param("ids", testId.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.userList[0].id").value(testId.toString()));
	}

	@Test
	void testGetUserWithSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/get/" + testId.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testId.toString()));
	}

	@Test
	void testGetUserWithFailure() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/get/" + "8a6156fb-300b-48bf-b72e-87adf716dbbf")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));
	}

	@Test
	void testGetAllUserWithSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/get/all")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.userList[0].id").value(testId.toString()));
	}

}
