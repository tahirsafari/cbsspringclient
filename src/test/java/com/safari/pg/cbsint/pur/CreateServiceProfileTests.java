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
public class CreateServiceProfileTests {
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
	public void createServiceProfile() throws Exception {
		int createdByUserId = 1;
		int serviceId = 2;
		String serviceProfileDesc = "desc";
		int serviceChargeModeId = 1;
		int maxOutTxAmount = 10;
		int minOutTxAmount = 2;
		int maxInTxAmount = 10;
		int minInTxAmount = 5;
		int dailyInTxLimit = 12;
		int dailyOutTxLimit = 13;
		int monthlyInTxLimit = 5;
		int monthlyOutTxLimit = 6;
		
		int  result = chInterface.createServiceProfile(createdByUserId, serviceId, serviceProfileDesc, serviceChargeModeId, maxOutTxAmount, minOutTxAmount, maxInTxAmount, minInTxAmount, dailyInTxLimit, dailyOutTxLimit, monthlyInTxLimit, monthlyOutTxLimit);
		System.out.println("result "+result);
		assertTrue(result > 0);
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void createServiceProfile_ThrowsException() throws Exception {
		int createdByUserId = 1;
		int serviceId = 2;
		String serviceProfileDesc = "";
		int serviceChargeModeId = 1;
		int maxOutTxAmount = 10;
		int minOutTxAmount = 2;
		int maxInTxAmount = 10;
		int minInTxAmount = 5;
		int dailyInTxLimit = 12;
		int dailyOutTxLimit = 13;
		int monthlyInTxLimit = 5;
		int monthlyOutTxLimit = 6;
		
		int  result = chInterface.createServiceProfile(createdByUserId, serviceId, serviceProfileDesc, serviceChargeModeId, maxOutTxAmount, minOutTxAmount, maxInTxAmount, minInTxAmount, dailyInTxLimit, dailyOutTxLimit, monthlyInTxLimit, monthlyOutTxLimit);
		System.out.println("result "+result);
	}
}
