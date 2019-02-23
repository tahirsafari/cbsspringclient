package com.safari.pg.cbsint.udr;

import org.junit.Assert;
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
public class GetCashierInfoByUserIdTest {
	@Autowired
	JdbcTemplate jdbcTemplate;
	CbsAgent ca;
	CbsShInterface chInterface;
	
	@Before
	public void setup() throws CbsException {
		this.ca = new CbsAgent(jdbcTemplate);
    	this.chInterface = new  CbsShInterface(this.ca);
	}
	
    @Test
    public void testGetCashierInfoByUserIdUsingValidUserId() throws Exception {
    	Assert.assertNotNull(chInterface.getCashierInfoByUserId(1000006));
    }
    
    @Test //(expected=CbsInterfaceException.class)
    public void testGetCashierInfoByUserIdUsingInValidUserId() throws Exception {
    	Assert.assertNull(chInterface.getCashierInfoByUserId(1).getActivationToken());
    }
    
    @Test (expected=CbsInterfaceException.class)
    public void testGetCashierInfoByUserId_ThrowsException() throws Exception {
    	Assert.assertNull(chInterface.getCashierInfoByUserId(-1).getActivationToken());
    }
}
