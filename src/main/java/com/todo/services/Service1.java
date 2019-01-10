/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.services;

import org.springframework.jdbc.core.JdbcTemplate;

import com.safari.pg.util._UserAuthInfo;
import com.todo.cbs.CbsAuthInterface;

/**
 *
 * @author Safarifone
 */
public class Service1 extends ServiceBase {
    
    public Service1(){
        super();
    }
    
    @Override
    public _UserAuthInfo process(String input){
        //init();
        
        
        //use cbs services
        //for ex
//        
//        CbsAuthInterface ai = new CbsAuthInterface(ca);
//        String result_obj = ai.someAction("");
       return super.process(input);
                
        //close();    
        
        //return "somedata";
    }
    
}
