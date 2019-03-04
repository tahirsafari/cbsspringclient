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
public class UpdateServiceTests {
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
	public void updateService() throws Exception {
		String serviceCode = "M2A";
		int serviceId = 1;
		String serviceProfileDesc = "desc";
		int isActive = 1;
		int isTransactional = 10;
	
		boolean  result = chInterface.updateService(serviceId, serviceCode, serviceProfileDesc, isActive, isTransactional);
		System.out.println("result "+result);
		assertTrue(result);
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void updateService_ThrowsException() throws Exception {
		String serviceCode = "";
		int serviceId = 1;
		String serviceProfileDesc = "desc";
		int isActive = 1;
		int isTransactional = 10;
	
		chInterface.updateService(serviceId, serviceCode, serviceProfileDesc, isActive, isTransactional);
	}
}
