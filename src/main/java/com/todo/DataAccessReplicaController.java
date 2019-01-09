package com.todo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.safari.pg.util.DbRecord;
import com.safari.pg.util.DbResult;
import com.safari.pg.util._UserAuthInfo;

@Service
public class DataAccessReplicaController {

	public int retCode = 0;
	@Autowired
	private DBManager dm;
	public final int DBRC_SUCCESS = 0;
	
	private static final Logger logger = LogManager.getLogger(DataAccessReplicaController.class);
	
    public _UserAuthInfo db_get_User_AuthInfo_ByUserId(JdbcTemplate template, int userId) {
        String sp_name = "sp_get_user_authinfo_byuserid";           
        CallableStatement cs = null;
        _UserAuthInfo uobj = null;
        try {
            
                         //retCode = 0; 
                         
            Connection conn = template.getDataSource().getConnection();
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
    
    public boolean db_update_SysConfigParam(JdbcTemplate template, int doneByUserId, int id, String code, String name, String val){
    	boolean res = false;
    
	    String sp_name = "sp_update_sysconfigparam";           
	    CallableStatement cs = null;
	    try {
	                     retCode = 0; Connection conn = template.getDataSource().getConnection();
	        cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?)}");
	        cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
	       
	        cs.setInt("pPARAMID", id);
	        cs.setString("pPARAMCODE", code);
	        cs.setString("pPARAMNAME", name);
	        cs.setString("pPARAMVALUE", val);
	        cs.setInt("pDONEBYUSERID", doneByUserId);
	                                
	        cs.executeUpdate();
	        
	        ResultSet rset = null;
	        retCode = cs.getInt(1);
	        logger.info("db-returncode: "+retCode);
	        if(retCode == DBRC_SUCCESS){
	             res = true;
	        }
	        cs.closeOnCompletion();
	    } catch (Exception e) {
	        logger.info("db_call_error: "+e.getMessage());
	    }            
    
	    return res;
}
}
