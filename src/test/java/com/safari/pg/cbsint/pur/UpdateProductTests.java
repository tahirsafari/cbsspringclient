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
public class UpdateProductTests {
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
	public void updateProduct() throws CbsInterfaceException, CbsException {
		String productName = "productName";
		String productDesc = "productDesc";
		int productOwner = 1;
		int productType = 1;
		int productId = 1;
		
		boolean  result = chInterface.updateProduct(productId, productName, productDesc, productOwner, productType);
		System.out.println("result "+result);
		assertTrue(result);
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void updateProduct_ThrowsException() throws CbsInterfaceException, CbsException {
		String productName = "";
		String productDesc = "productDesc";
		int productOwner = 1;
		int productType = 1;
		int productId = 1;
		
		boolean  result = chInterface.updateProduct(productId, productName, productDesc, productOwner, productType);
		System.out.println("result "+result);
	}
}
