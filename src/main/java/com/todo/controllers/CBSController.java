package com.todo.controllers;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safari.pg.util._UserAuthInfo;
import com.todo.services.Service1;
import com.todo.services.ServiceBase;

@RestController
@RequestMapping("/cbs")
public class CBSController {

//    @Autowired
//    DataSource dataSource;
//    @Autowired
//    JdbcTemplate template;

    private static final Logger logger = LogManager.getLogger(CBSController.class);
    
  
    @GetMapping(value = "/someendpoint")
	public _UserAuthInfo serviceMethod()  {
        
        //depending on some condition, ServiceX will be invoked
    	//Service1 obj = new Service1();
        //String result = obj.process();
            
        ServiceBase srvcObj = null;
        if(1 == 1){
            srvcObj =new Service1();
        } 
        
        _UserAuthInfo result = srvcObj.process("1000008");
        return result;
            
       
	}
	
	
}
