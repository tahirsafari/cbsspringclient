package com.safari.pg.cbsint.pur;

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
import com.todo.TodoApplication;
import com.todo.dtos.WebPermissionDto;

import junit.framework.Assert;

import com.safari.pg.cbs.def.CbsInterfaceException;
import com.safari.pg.cbsint.CbsAgent;
import com.safari.pg.cbsint.CbsShInterface;

@RunWith(SpringRunner.class)
@ActiveProfiles(value="test")
@SpringBootTest(classes = TodoApplication.class)
@AutoConfigureWebMvc
public class CreateWebPermissionTests {
	@Autowired
	JdbcTemplate jdbcTemplate;
	CbsAgent ca;
	CbsShInterface chInterface;
	
	@Before
	public void setup() throws CbsException {
		this.ca = new CbsAgent(jdbcTemplate);
    	this.chInterface = new  CbsShInterface(this.ca);
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void createWebPermissionThrowsException_InvalidUserId() throws CbsInterfaceException {
		WebPermissionDto permissionDto = new WebPermissionDto(-1, "Test", "Desc");
		chInterface.createWebPermission(permissionDto.getUserId(), permissionDto.getName(), permissionDto.getDesc());
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void createWebPermissionThrowsException_InvalidName() throws CbsInterfaceException {
		WebPermissionDto permissionDto = new WebPermissionDto(100, "", "Desc");
		chInterface.createWebPermission(permissionDto.getUserId(), permissionDto.getName(), permissionDto.getDesc());
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void createWebPermissionThrowsException_InvalidDesc() throws CbsInterfaceException {
		WebPermissionDto permissionDto = new WebPermissionDto(100, "Name", "");
		chInterface.createWebPermission(permissionDto.getUserId(), permissionDto.getName(), permissionDto.getDesc());
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void createWebPermissionThrowsException_NullValues() throws CbsInterfaceException {
		WebPermissionDto permissionDto = new WebPermissionDto(0, null, null);
		chInterface.createWebPermission(permissionDto.getUserId(), permissionDto.getName(), permissionDto.getDesc());
	}		
	@Test
	public void createWebPermission_success() throws CbsInterfaceException {
		WebPermissionDto permissionDto = new WebPermissionDto(100, "Test", "Desc");
		int result = chInterface.createWebPermission(permissionDto.getUserId(), permissionDto.getName(), permissionDto.getDesc());
		Assert.assertTrue(result > 0);
	}
}
