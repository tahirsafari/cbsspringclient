/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.services;



/**
 *
 * @author Safarifone
 */
public class Service1 extends ServiceBase {
    
    public Service1(){
        
    }
    
    @Override
    public String process(String input){
        init();
        
        
        //use cbs services
        //for ex
        
        CbsAuthInterface ai = new CbsAuthInterface(ca);
        String result_obj = ai.someAction();
        
                
        close();    
        
        return "somedata";
    }
    
}
