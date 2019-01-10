/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.services;

import org.springframework.jdbc.core.JdbcTemplate;

import com.safari.pg.util._UserAuthInfo;
import com.todo.cbs.CbsAgent;
import com.todo.cbs.CbsAuthInterface;

/**
 *
 * @author Safarifone
 */
public class ServiceBase {
    
    CbsAgent ca;
    
    
    public ServiceBase() {
        SpringApplicationContext appContext = new SpringApplicationContext();
    	JdbcTemplate jdbcTemplate = appContext.getContext().getBean("jdbcTemplate",JdbcTemplate.class );

    	this.ca = new CbsAgent(jdbcTemplate);
    	
    }
//    public void init(){
//        ca = new CbsAgent();
//    }
    public _UserAuthInfo process(String si){
    	CbsAuthInterface ai = new CbsAuthInterface(ca);
    	return ai.someAction(si);
    }
//    public void close(){
//        
//    }
}
