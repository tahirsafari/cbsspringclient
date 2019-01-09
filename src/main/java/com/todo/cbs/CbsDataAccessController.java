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
public class CbsDataAccessController {
    CbsAgent ca = mull;
    
    public CbsDataAccessController(CbsAgent ca) {
        this ca = ca;
    }
    
    public String someDbCall( String dbcall_input_params) {
        
        // actual db_call happens here with the help of ca
        // for ex
        //            Connection conn = ca.getConnection();
        //            cs = conn.prepareCall("{call " + sp_name + "(?)}");
        //            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
        //                       
        //            cs.executeUpdate();

        retun "dbresult";
    }
}
