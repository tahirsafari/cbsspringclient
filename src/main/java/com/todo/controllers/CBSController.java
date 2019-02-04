package com.todo.controllers;

import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safari.pg.cbs.def.CbsException;
import com.safari.pg.cbs.def._MerchantFavCustomer;
import com.safari.pg.cbs.def._MerchantSetting;
import com.safari.pg.cbs.def._UserAuthInfo;
import com.safari.pg.cbs.services.sh.ServiceUdr;
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
    
  
//    @GetMapping(value = "/someendpoint")
//	public _UserAuthInfo serviceMethod() throws CbsException  {
//        
//        //depending on some condition, ServiceX will be invoked
//    	//Service1 obj = new Service1();
//        //String result = obj.process();
//            
//        ServiceBase srvcObj = null;
//        if(1 == 1){
//            srvcObj =new Service1();
//        } 
//        
//        _UserAuthInfo result = srvcObj.process("1000008");
//        return result;
//            
//       
//	}
	
    @GetMapping(value = "/code/null")
	public ResponseEntity<_MerchantFavCustomer> testService() throws Exception  {
    
        
		ServiceBase srvcObj = null;
		if(1 == 1){
			srvcObj =new Service1();
		}
		//String input = "{\"commandCode\":\"2001\",\"serviceParams\":{\"groupId\":\"20\"}}";
		String input = "{\"commandCode\":\"2002\",\"serviceParams\":{\"userId\":\"1000008\"}}";
		//String output =  srvcObj.mapService(input); 
		_MerchantFavCustomer list = srvcObj.getMerchantSettingsWhenCodeIsNull(100);
		return new ResponseEntity<_MerchantFavCustomer>(list, HttpStatus.OK); 
            
       
	}
	
    @GetMapping(value = "/group/small")
	public ResponseEntity<_MerchantFavCustomer> whenGroupIdIsSmall() throws Exception  {
    
        
		ServiceBase srvcObj = null;
		if(1 == 1){
			srvcObj =new Service1();
		}
		_MerchantFavCustomer list = srvcObj.getMerchantSettingsWhenInputIsSmall(4);
		return new ResponseEntity<_MerchantFavCustomer>(list, HttpStatus.OK); 
            
       
	}
	
    @GetMapping(value = "/code/empty")
	public ResponseEntity<_MerchantFavCustomer> whenCodeIsEmpty() throws Exception  {
    
        
		ServiceBase srvcObj = null;
		if(1 == 1){
			srvcObj =new Service1();
		}
		_MerchantFavCustomer list = srvcObj.getMerchantSettingsWhenCodeIsEmpty(100);
		return new ResponseEntity<_MerchantFavCustomer>(list, HttpStatus.OK); 
            
       
	}
}
