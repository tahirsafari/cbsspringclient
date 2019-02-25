package com.safari.pg.cbsint.udr;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
public class GetMerchantTranCancelRequestInfoByTranIdTests {
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
	public void getMerchantTranCancelRequestInfoByTranId() throws Exception {
		int id = 1220018;
		assertNotNull(chInterface.getMerchantTranCancelRequestInfoByTranId(id).getDescription());
	}
	
	@Test
	public void getMerchantTranCancelRequestInfoByTranId_InvalidGroupId() throws Exception {
		int invalidId = 100000400;
		assertNull(chInterface.getMerchantTranCancelRequestInfoByTranId(invalidId).getDescription());
	}
	@Test(expected=CbsInterfaceException.class)
	public void getMerchantTranCancelRequestInfoByTranId_ThrowsException() throws Exception {
		int invalidId = -1001;
		chInterface.getMerchantTranCancelRequestInfoByTranId(invalidId);
	}
}
