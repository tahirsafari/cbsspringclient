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
public class UpdateTranStatusTests {
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
	public void updateTranStatus() throws CbsInterfaceException, CbsException {
		int statusId = 1;
		int transactionId = 12344;

		boolean  result = chInterface.updateTranStatus(transactionId, statusId);
		System.out.println("result "+result);
		assertTrue(result );
	}
	
//	@Test(expected=CbsInterfaceException.class)
//	public void updateTranStatus_ThrowsException() throws CbsInterfaceException, CbsException {
//		int statusId = 1;
//		int transactionId = -12344;
//
//		boolean  result = chInterface.updateTranStatus(transactionId, statusId);
//		System.out.println("result "+result);
//	}
}
