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
public class UpdatePaymentProcessorSystemTests {
	@Autowired
	JdbcTemplate jdbcTemplate;
	CbsAgent ca;
	CbsShInterface chInterface;
	
	@Before
	public void setup() throws CbsException, CbsInterfaceException {
		this.ca = new CbsAgent(jdbcTemplate);
    	this.chInterface = new  CbsShInterface(this.ca);
	}
	
	@Test
	public void updateMerchantOrderVendorResponse() throws CbsInterfaceException {
		int systemId = 100;
		String systemName = "systemName";
		String desc = "desc";
		String systemUrl = "systemUrl";
		String systemSecret = "systemSecret";
		int protocolId = 1;
		String stystemUsername = "stystemUsername";
		String systemPassword = "systemPassword";
		int isActive = 1;
		String myIp = "192.168.10.80";
		String myPartnerId = "123";
        
		boolean result = this.chInterface.updatePaymentProcessorSystem(systemId, systemName, desc, systemUrl, systemSecret, protocolId, stystemUsername, systemPassword, isActive, myIp, myPartnerId);
		System.out.println("result "+result);
		assertTrue(result);
	}
}
