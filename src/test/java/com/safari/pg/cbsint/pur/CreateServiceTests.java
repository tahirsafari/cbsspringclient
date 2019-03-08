package com.safari.pg.cbsint.pur;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.safari.pg.cbs.def.CbsException;
import com.safari.pg.cbs.def.CbsInterfaceException;
import com.safari.pg.cbsint.CbsAgent;
import com.safari.pg.cbsint.CbsShInterface;
import com.todo.TodoApplication;

@RunWith(SpringRunner.class)
@ActiveProfiles(value="test")
@SpringBootTest(classes = TodoApplication.class)
@AutoConfigureWebMvc
public class CreateServiceTests {
	@Autowired
	JdbcTemplate jdbcTemplate;
	CbsAgent ca;
	CbsShInterface chInterface;
	
	@Before
	public void setup() throws CbsInterfaceException, CbsException {
		this.ca = new CbsAgent(jdbcTemplate);
    	this.chInterface = new  CbsShInterface(this.ca);
	}
	
	@Test
	public void createService() throws Exception {
		String serviceCode = "M2A";
		String serviceProfileDesc = "desc";
		int isActive = 1;
		int isTransactional = 10;
		int createdByUserId = 1;
	
		int  result = chInterface.createService(serviceCode, serviceProfileDesc, isActive, isTransactional, createdByUserId);
		System.out.println("result "+result);
		assertTrue(result > 0);
	}
	
//	@Test(expected=CbsInterfaceException.class)
//	public void createService_ThrowsException() throws Exception {
//		String serviceCode = "";
//		String serviceProfileDesc = "desc";
//		int isActive = 1;
//		int isTransactional = 10;
//		int createdByUserId = 1;
//	
//		int  result = chInterface.createService(serviceCode, serviceProfileDesc, isActive, isTransactional, createdByUserId);
//		System.out.println("result "+result);
//		assertTrue(result > 0);
//	}
}
