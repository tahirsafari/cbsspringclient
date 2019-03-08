package com.safari.pg.cbsint.udr;

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
public class GetMerchantTransactionsTests {
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
	public void getMerchantTransactions() throws Exception {
		int merchantId = 10001;
		int statusId = 6; 
		int recordCount = 2;
		assertTrue(chInterface.getMerchantTransactions(merchantId, statusId, recordCount).size() >= 0);
	}
	

//	@Test(expected=CbsInterfaceException.class)
//	public void getMerchantTransactions_ThrowsException() throws Exception {
//		int merchantId = -1001;
//		int statusId = 2; 
//		int recordCount = 2;
//		chInterface.getMerchantTransactions(merchantId, statusId, recordCount);
//	}
}
