/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.cbs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.safari.pg.util.DbRecord;
import com.safari.pg.util.DbResult;
import com.safari.pg.util._UserAuthInfo;


/**
 *
 * @author Safarifone
 */
public class CbsDataAccessController {
    CbsAgent ca;
	public int retCode = 0;
	public final int DBRC_SUCCESS = 0;
	private static final Logger logger = LogManager.getLogger(CbsDataAccessController.class);
    
    public CbsDataAccessController(CbsAgent ca) {
        this.ca = ca;
    }
    
    public _UserAuthInfo db_get_User_AuthInfo_ByUserId(int userId) {
        
    	 String sp_name = "sp_get_user_authinfo_byuserid";           
         CallableStatement cs = null;
         _UserAuthInfo uobj = null;
         try {
             
                          //retCode = 0; 
                          
             Connection conn = this.ca.getConnection();//.getDataSource().getConnection();
             logger.info("connection "+conn);
             cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
             cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
             
             cs.setInt("pUSERID", userId);
             
             cs.executeUpdate();
             logger.info("cs "+cs.getInt(1));
             ResultSet rset = null;
             retCode = cs.getInt(1);
             logger.info("db-returncode: "+retCode);
             if(retCode == DBRC_SUCCESS){
                 rset = cs.getResultSet();
                 
                 logger.info("extractData resultset");
                 DbResult rs = new DbResult(rset);
                 if (rs.hasNext()) {
                     DbRecord record = rs.next();

                     uobj = new _UserAuthInfo();
                     uobj.loadFromDbRecord(record);
                 }
             }
             cs.closeOnCompletion();
         } catch (Exception e) {
             logger.warn("db_call_error 4: "+e.getClass());
         }            
         return uobj;
    }
}
