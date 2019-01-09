/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.cbs;

/**
 *
 * @author Safarifone
 */
public class CbsShInterface {
    
     CbsDataAccessController cac = null;
    
    public CbsShInterface (CbsAgent ca){        
        cac = new CbsDataAccessController(ca);
    }
    
    public String someAction(String input){
        
        String dbresult = cac.someDbCall("input params");
        
        
        String custom_obj = dbresult;
        
        return custom_obj;
    }
}
