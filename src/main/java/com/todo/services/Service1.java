/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.services;

import org.springframework.jdbc.core.JdbcTemplate;

import com.safari.pg.cbs.def.CbsException;
import com.safari.pg.cbs.def._UserAuthInfo;


/**
 *
 * @author Safarifone
 */
public class Service1 extends ServiceBase {
    
    public Service1() throws CbsException{
        super();
    }
    
    @Override
    public String mapService(String input) throws Exception{
        //init();
        
        
        //use cbs services
        //for ex
//        
//        CbsAuthInterface ai = new CbsAuthInterface(ca);
//        String result_obj = ai.someAction("");
       //return super.mapService(input);
                
        //close();    
        
        return "somedata";
    }
    
}
