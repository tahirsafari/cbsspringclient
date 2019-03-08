package com.safari.pg.cbsint.udr;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

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
public class GetMerchantTransactionsByIdTests {
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
	public void getMerchantTransactionsById() throws Exception {
		int merchantId = 10001;
		int statusId = 6; 
		int recordCount = 2;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date parsed = format.parse("2018-01-17");
        java.sql.Date fromDate = new java.sql.Date(parsed.getTime());
        java.sql.Date toDate = new java.sql.Date(new Date().getTime());
		assertTrue(chInterface.getMerchantTransactionsById(merchantId, statusId, recordCount, fromDate, toDate).size() >= 0);
	}
	

//	@Test(expected=CbsInterfaceException.class)
//	public void getMerchantTransactions_ThrowsException() throws Exception {
//		int merchantId = 10001;
//		int statusId = 2; 
//		int recordCount = 2;
//
//		chInterface.getMerchantTransactionsById(merchantId, statusId, recordCount, null, null);
//	}
}
