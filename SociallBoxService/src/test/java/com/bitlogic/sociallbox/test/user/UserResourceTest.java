package com.bitlogic.sociallbox.test.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bitlogic.sociallbox.service.test.config.MockAppContext;
import com.bitlogic.sociallbox.service.test.config.MockServletContextConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MockServletContextConfig.class,
		MockAppContext.class})
@WebAppConfiguration
public class UserResourceTest {

	private MockMvc mockMvc;

	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
	}
	
	
	@Test
	public void testAddNewUserSuccess() throws Exception{
		StringBuffer sb = new StringBuffer();
		InputStream stream = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream("requests/signupMobileUser.json");
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		for (int c = br.read(); c != -1; c = br.read()) sb.append((char)c);

		System.out.println(sb.toString());
		
		 mockMvc.perform(post("/api/secured/users")
	                .contentType(MediaType.APPLICATION_JSON)
	                .header("type", "M")
	                .content(sb.toString())
	        ).andExpect(status().isCreated());
		
	}
}
