package com.safari.pg.cbsint.pur;

import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;

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
public class UpdateProductServiceRateTests {
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
	public void updateProductServiceRate() throws CbsInterfaceException, CbsException {
		int rateId = 10;
		double senderRate = 100;
		int senderRateTypeId = 1;
		double rcvRate = 12;
		int rcvRateTypeId = 1;
		double minTxAmt = 1;
		double maxTxAmt = 2;
		Date effdate = new Date(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		java.util.Date tomorrow = calendar.getTime();
		Date expdate = new Date(tomorrow.getTime());
		boolean  result = chInterface.updateProductServiceRate(rateId, senderRate, senderRateTypeId, rcvRate, rcvRateTypeId, minTxAmt, maxTxAmt, effdate, expdate);
		System.out.println("result "+result);
		assertTrue(result);
	}
	
//	@Test(expected=CbsInterfaceException.class)
//	public void updateProductServiceRate_ThrowsException() throws CbsInterfaceException, CbsException {
//		int rateId = 10;
//		double senderRate = -100;
//		int senderRateTypeId = -1;
//		double rcvRate = 12;
//		int rcvRateTypeId = 1;
//		double minTxAmt = 1;
//		double maxTxAmt = 2;
//		Date effdate = new Date(Instant.now().toEpochMilli());
//		Date expdate = new Date(Instant.now().toEpochMilli());
//		boolean  result = chInterface.updateProductServiceRate(rateId, senderRate, senderRateTypeId, rcvRate, rcvRateTypeId, minTxAmt, maxTxAmt, effdate, expdate);
//		System.out.println("result "+result);
//	}
}
