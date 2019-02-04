package com.todo.todo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
//@ActiveProfiles(value="test")
@SpringBootTest
public class CbsControllerTests {

	@Autowired
	WebApplicationContext wac;
	@Autowired
	private ObjectMapper objectMapper;
//	@Autowired
//	private CustomerService customerService;
	private MockMvc mockMvc;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
        		.webAppContextSetup(wac)
                .build();
	}
	
	
	@Before
	public void addData(){
		
//		Customer customer = new Customer();
//		customer.setAddress("lorem ipsum");
//		customer.setContactNumber("+92-300-13344899");
//		customer.setFirstName("David");
//		customer.setLastName("Lorem");
//		
//		customerService.saveCustomer(customer);
	}
	
	@Test
	public void list_get_customers_when_code_null() throws Exception{
		mockMvc.perform(get("/cbs/code/null"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				//.andExpect(jsonPath("$", hasSize(1)))
//				.andExpect(jsonPath("$[0].id").value(1L))
//				.andExpect(jsonPath("$[0].firstName").value("David"))
				.andDo(print());
	}

	@Test
	public void list_get_customers_when_code_empty() throws Exception{
		mockMvc.perform(get("/cbs/code/empty"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				//.andExpect(jsonPath("$", hasSize(1)))
//				.andExpect(jsonPath("$[0].id").value(1L))
//				.andExpect(jsonPath("$[0].firstName").value("David"))
				.andDo(print());
	}
	
	@Test
	public void list_get_customers_when_group_small() throws Exception{
		mockMvc.perform(get("/cbs/group/small"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				//.andExpect(jsonPath("$", hasSize(1)))
//				.andExpect(jsonPath("$[0].id").value(1L))
//				.andExpect(jsonPath("$[0].firstName").value("David"))
				.andDo(print());
	}
}
