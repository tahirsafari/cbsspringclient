package com.todo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.safari.pg.util.DbRecord;
import com.safari.pg.util.DbResult;
import com.safari.pg.util._AccountInfo;
import com.safari.pg.util._AccountStatus;
import com.safari.pg.util._BankInfo;
import com.safari.pg.util._CashierInfo;
import com.safari.pg.util._CashierTypeInfo;
import com.safari.pg.util._DeviceInfo;
import com.safari.pg.util._DistributorInfo;
import com.safari.pg.util._TranCancelRequest;
import com.safari.pg.util._MerchantFavCustomer;
import com.safari.pg.util._MerchantGroup;
import com.safari.pg.util._PaymentProcessorInfo;
import com.safari.pg.util._MerchantInfo;
import com.safari.pg.util._MerchantOrder;
import com.safari.pg.util._MerchantOrderResult;
import com.safari.pg.util._MerchantSetting;
import com.safari.pg.util._MerchantSettlementInfo;
import com.safari.pg.util._MerchantTransactionDetail;
import com.safari.pg.util._RouteInfo;
import com.safari.pg.util._PaymentProcessorInfoWithSystem;
import com.safari.pg.util._PaymentProcessorSystemInfo;
import com.safari.pg.util._PaymentProcessorType;
import com.safari.pg.util._ProductInfo;
import com.safari.pg.util._ProductServiceProfile;
import com.safari.pg.util._ProductServiceRate;
import com.safari.pg.util._ProductType;
import com.safari.pg.util._RateType;
import com.safari.pg.util._ServiceChargeModes;
import com.safari.pg.util._ServiceInfo;
import com.safari.pg.util._SettlementInfo;
import com.safari.pg.util._SettlementMode;
import com.safari.pg.util._SettlementStatus;
import com.safari.pg.util._SubscriptionType;
import com.safari.pg.util._SysConfigParam;
import com.safari.pg.util._TranCancelRequestStatus;
import com.safari.pg.util._TranStatus;
import com.safari.pg.util._TransactionInfo;
import com.safari.pg.util._UserAuthInfo;
import com.safari.pg.util._UserInfo;
import com.safari.pg.util._UserType;
import com.safari.pg.util._User_WebMenuPermission;
import com.safari.pg.util._WebPermission;
import com.safari.pg.util._WebRole;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Safarifone
 */
public class DataAccessController {
    Logger logger = Logger.getLogger(DataAccessController.class.getName());
    protected DataManager dm;
    Connection conn;
    
    public final int DBRC_SUCCESS = 0;
    public final int DBRC_DUPLICATE_RECORD = -1;
    
    public final int DBRC_LOGINID_ALREADY_EXIST = -2;
    public final int DBRC_TRANID_ALREADY_REQUESTED_FOR_CANCELLATION = -3;
    
    
    public int retCode = 0;
    
    public DataAccessController() throws Exception {    
        logger.info("initializing datamanger object");
        dm = new DataManager();
    }
    
    public Connection getConnection() throws Exception{
        if(conn == null){            
            logger.info("Conn obj is null, requesting new connection from datamanager");
            conn = dm.getConnection();            
        } else {
            logger.info("db-connection is already available");
            if(!conn.isValid(1000)){
                logger.info("dbconnection looks not valid, closing the connection");
               
                conn.close();               
                conn = null;
                
                logger.info("trying to get new connection");
                conn = dm.getConnection(); 
            }
        }
        
       return conn;
    }
    public void colse() throws SQLException{
        if(conn!=null){
            logger.info("closing connection: ");
            conn.close();
        } else {
            logger.info("conn is null");
        }
    }

        public int db_create_SysConfigParam(int cuserId, String code, String name, String val){        
                
         int paramId = 0;
        
        String sp_name = "sp_create_sysconfigparam";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pPARAMID", Types.NUMERIC);
            
            cs.setString("pPARAMCODE", code);
            cs.setString("pPARAMNAME", name);
            cs.setString("pPARAMVALUE", val);
            cs.setInt("pCREATEDBYUSERID", cuserId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 paramId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return paramId;
    }
        public boolean db_update_SysConfigParam(int doneByUserId, int id, String code, String name, String val){
        boolean res = false;
        
        String sp_name = "sp_update_sysconfigparam";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
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
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
        public _SysConfigParam db_get_SysConfigParam_Info(int id) {
        String sp_name = "sp_get_sysconfigparaminfo";           
        CallableStatement cs = null;
        _SysConfigParam uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pPARAMID", id);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _SysConfigParam();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
        public List<_SysConfigParam> db_get_SysConfigParam_List() {
        String sp_name = "sp_get_sysconfigparamlist";           
        CallableStatement cs = null;
        List<_SysConfigParam> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _SysConfigParam obj = new _SysConfigParam();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
     public _WebPermission db_get_WebPermission_Info(int id) {
        String sp_name = "sp_get_webpermissioninfo";           
        CallableStatement cs = null;
        _WebPermission uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pPERMISSIONID", id);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _WebPermission();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
     
     
     public List<_WebPermission> db_get_WebPermission_List() {
        String sp_name = "sp_get_webpermissions";           
        CallableStatement cs = null;
        List<_WebPermission> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _WebPermission obj = new _WebPermission();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
     
    public int db_create_WebPermission(int userId, String name, String desc){        
       
         
         int permissionId = 0;
        
        String sp_name = "sp_create_webpermission";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pPERMISSIONID", Types.NUMERIC);
            
            cs.setString("pNAME", name);
            cs.setString("pDESCRIPTION", desc);
            cs.setInt("pCREATEDBYUSERID", userId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 permissionId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return permissionId;
    }
    
     public boolean db_update_WebPermission(int id, String name, String desc, int isActive){      
        boolean res = false;
        
        String sp_name = "sp_update_webpermission";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
           
            cs.setInt("pPERMISSIONID", id);
            cs.setString("pNAME", name);
            cs.setString("pDESCRIPTION", desc);
            cs.setInt("pISACTIVE", isActive);
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    
    public int db_create_Route(int issuerId, String routePrefix, int isActive){        
         int routeId = 0;
        
        String sp_name = "sp_create_route";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pROUTEID", Types.NUMERIC);
            
            cs.setString("pROUTEPREFIX", routePrefix);
            cs.setInt("pBANKID", issuerId);
            cs.setInt("pISACTIVE", isActive);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 routeId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return routeId;
    }
    public boolean db_update_Route(int routeId, int issuerId, String routePrefix, int isActive){        
        boolean res = false;
        
        String sp_name = "sp_update_route";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
           
            cs.setInt("pROUTEID", routeId);
            cs.setString("pROUTEPREFIX", routePrefix);
            cs.setInt("pBANKID", issuerId);
            cs.setInt("pISACTIVE", isActive);
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public List<_RouteInfo> db_get_Routes_List() {
        String sp_name = "sp_get_routes_list";           
        CallableStatement cs = null;
        List<_RouteInfo> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _RouteInfo obj = new _RouteInfo();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
     public _RouteInfo db_get_Route_Info(int routeId) {
        String sp_name = "sp_get_routeinfo";           
        CallableStatement cs = null;
        _RouteInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pROUTEID", routeId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _RouteInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public List<_CashierTypeInfo> db_get_CashierType_List() {
        String sp_name = "sp_get_cashiertypes";           
        CallableStatement cs = null;
        List<_CashierTypeInfo> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _CashierTypeInfo obj = new _CashierTypeInfo();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    public List<_PaymentProcessorType> db_get_PaymentProcessorTypes() {
        String sp_name = "sp_get_pmtprocessortypes";           
        CallableStatement cs = null;
        List<_PaymentProcessorType> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _PaymentProcessorType obj = new _PaymentProcessorType();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
     public List<_PaymentProcessorSystemInfo> db_get_PaymentProcessorSystem_List() {
        String sp_name = "sp_get_pmtprocessorsystem_list";           
        CallableStatement cs = null;
        List<_PaymentProcessorSystemInfo> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _PaymentProcessorSystemInfo obj = new _PaymentProcessorSystemInfo();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
     public int db_create_PaymentProcessorSystem(int createdByUserId, String systemName, String desc,  String systemUrl, String systemSecret, int protocolId, String stystemUsername, String systemPassword, int isActive, String myIp, String myPartnerId){        
         int roleId = 0;
        
        String sp_name = "sp_create_pmtprocessorsystem";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pSYSTEMID", Types.NUMERIC);
            
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
            cs.setString("pSYSTEMNAME", systemName);
            cs.setString("pDESCRIPTION", desc);

            
            cs.setString("pSYSTEMURL", systemUrl);
            cs.setString("pSYSTEMSECRET", systemSecret);
            
             cs.setInt("pPROTOCOLID", protocolId);
            cs.setString("pSYSTEMUSERNAME", stystemUsername);
            cs.setString("pSYSTEMPASSWORD", systemPassword);
            
               cs.setInt("pISACTIVE", isActive);
            cs.setString("pMYIP", myIp);
            cs.setString("pMYPARTNERID", myPartnerId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 roleId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return roleId;
    }
     public boolean db_update_PaymentProcessorSystem(int systemId, String systemName, String desc, String systemUrl, String systemSecret, int protocolId, String stystemUsername, String systemPassword, int isActive, String myIp, String myPartnerId){               
        boolean res = false;
        
        String sp_name = "sp_update_pmtprocessorsystem";           
        CallableStatement cs = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
                   
            cs.setInt("pSYSTEMID", systemId);
            cs.setString("pSYSTEMNAME", systemName);
            cs.setString("pDESCRIPTION", desc);

            
            cs.setString("pSYSTEMURL", systemUrl);
            cs.setString("pSYSTEMSECRET", systemSecret);
            
             cs.setInt("pPROTOCOLID", protocolId);
            cs.setString("pSYSTEMUSERNAME", stystemUsername);
            cs.setString("pSYSTEMPASSWORD", systemPassword);
            
               cs.setInt("pISACTIVE", isActive);
            cs.setString("pMYIP", myIp);
            cs.setString("pMYPARTNERID", myPartnerId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    
     public _PaymentProcessorSystemInfo db_get_PaymentProcessorSystem_Info(int ppid) {
        String sp_name = "sp_get_pmtprocessorsysteminfo";           
        CallableStatement cs = null;
        _PaymentProcessorSystemInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pPAYMENTPROCESSORSYSTEMID", ppid);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _PaymentProcessorSystemInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
     
   

    public int db_create_WebRole(int userId, String name, String desc){        
         int roleId = 0;
        
        String sp_name = "sp_create_webrole";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pROLEID", Types.NUMERIC);
            
            cs.setInt("pUSERID", userId);
            cs.setString("pROLENAME", name);
            cs.setString("pDESCRIPTION", desc);
                      
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 roleId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return roleId;
    }
   public boolean db_update_WebRole(int id, String name, String desc){        
        boolean res = false;
        
        String sp_name = "sp_update_webroleinfo";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
           
            cs.setInt("pROLEID", id);
            cs.setString("pROLENAME", name);
            cs.setString("pDESCRIPTION", desc);
                                   
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public _WebRole db_get_WebRole_Info(int roleId) {
        String sp_name = "sp_get_webrole_info";           
        CallableStatement cs = null;
        _WebRole uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            cs.setInt("pROLEID", roleId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _WebRole();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public List<_WebRole> db_get_WebRole_List() {
        String sp_name = "sp_get_webrole_list";           
        CallableStatement cs = null;
        List<_WebRole> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _WebRole obj = new _WebRole();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    public int db_createMerchantSetting(String featureName, int createdByUserId)  {
        int serviceId = 0;
        
        String sp_name = "sp_create_merchantsetting";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pFEATUREID", Types.NUMERIC);            
                       
            cs.setString("pFEATURENAME", featureName);
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
                   
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               serviceId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return serviceId;
    }
    
    public boolean db_updateMerchantSettingInfo( int featureId, String featureName, int doneByUserId)  {
         boolean uobj = false;
        
        String sp_name = "sp_update_merchantsetting";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
                       
            cs.setInt("pFEATUREID", featureId);
            cs.setString("pFEATURENAME", featureName);                      
            cs.setInt("pDONEBYUSERID", doneByUserId);                    
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
           if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return uobj;
    }
     public _MerchantSetting db_get_MerchantSetting_Info(int featureId) {
        String sp_name = "sp_get_merchantsetting_info";           
        CallableStatement cs = null;
        _MerchantSetting uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            cs.setInt("pFEATUREID", featureId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _MerchantSetting();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public List<_MerchantSetting> db_get_MerchantSettings() {
        String sp_name = "sp_get_merchantsettings";           
        CallableStatement cs = null;
        List<_MerchantSetting> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _MerchantSetting obj = new _MerchantSetting();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    public _MerchantGroup db_get_MerchantGroup_Info(int groupId) {
        String sp_name = "sp_get_merchantgroup_info";           
        CallableStatement cs = null;
        _MerchantGroup uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            cs.setInt("pMERCHANTGROUPID", groupId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _MerchantGroup();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public List<_MerchantGroup> db_get_MerchantGroup_List() {
        String sp_name = "sp_get_merchantgroup_list";           
        CallableStatement cs = null;
        List<_MerchantGroup> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _MerchantGroup obj = new _MerchantGroup();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    public int db_createMerchantGroup(String groupName, String groupDesc, int createdByUserId)  {
        int serviceId = 0;
        
        String sp_name = "sp_create_merchantgroup";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pGROUPID", Types.NUMERIC);            
                       
            cs.setString("pGROUPNAME", groupName);
            cs.setString("pGROUPDESC", groupDesc);
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
                   
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               serviceId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return serviceId;
    }
   
   public boolean db_updateMerchantGroup( int merchantGroupId, String groupName, String description, int doneByUserId)  {
         boolean uobj = false;
        
        String sp_name = "sp_update_merchantgroup";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
                       
            cs.setInt("pGROUPID", merchantGroupId);
            cs.setString("pGROUPNAME", groupName);
            cs.setString("pGROUPDESC", description);             
            cs.setInt("pDONEBYUSERID", doneByUserId);                    
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
           if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return uobj;
    }
    public List<_MerchantSetting> db_get_merchantgroup_settings(int groupId) {
        String sp_name = "sp_get_merchantgroup_settings_list";           
        CallableStatement cs = null;
        List<_MerchantSetting> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);    
            cs.setInt("pMERCHANTGROUPID", groupId);
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _MerchantSetting obj = new _MerchantSetting();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
   public boolean db_update_merchantgroup_settings(int groupId, String settings) {
        String sp_name = "sp_update_merchantgroup_settings";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pGROUPID", groupId);
            cs.setString("pSETTINGS", settings);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public int db_createUser(int cuserId, String fname, String mname, String lname, String email, String tel, String addr, String loginId, String loginPwd, String userTitle, int userTypeId) {
        int userId = 0;
        
        String sp_name = "sp_create_user";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pUSERID", Types.NUMERIC);
            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);
            cs.setString("pEMAIL", email);
            
            cs.setString("pTELEPHONE", tel);
            cs.setString("pADDRESS", addr);
            cs.setString("pLOGINID", loginId);
            cs.setString("pLOGINPASSWORD", loginPwd);
            cs.setString("pUSERTITLE", userTitle);
            cs.setInt("pCREATEDBYUSERID", cuserId);
            cs.setInt("pUSERTYPEID", userTypeId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                 userId = cs.getInt(2);
//                rset = cs.getResultSet();
//                
//                logger.info("extractData resultset");
//                DbResult rs = new DbResult(rset);
//                if (rs.hasNext()) {
//                    logger.info("Record available");
//                    DbRecord record = rs.next();
//                    logger.info(record.toString());
//                    userId = record.getInt("_USERID");
//                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return userId;
    }
   public int db_createAdminOperator(int cuserId, String fname, String mname, String lname, String email, String tel, String addr,  String addr2, String city, String country, String website, String loginId, String loginPwd, String userTitle, int userTypeId, String currency) {
        int merchantId = 0;
        
        String sp_name = "sp_create_admin_user";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pUSERID", Types.NUMERIC);
            cs.registerOutParameter("pADMINOPERATORID", Types.NUMERIC);
                              
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);
            
            cs.setString("pEMAIL", email);                        
            cs.setString("pTELEPHONE", tel);            
            cs.setString("pADDRESS", addr);            
            cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            
            cs.setString("pLOGINID", loginId);
            cs.setString("pLOGINPASSWORD", loginPwd);            
            cs.setString("pUSERTITLE", userTitle);
            cs.setInt("pCREATEDBYUSERID", cuserId);
            cs.setInt("pUSERTYPEID", userTypeId);
              cs.setString("pCURRENCY", currency);
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               merchantId = cs.getInt(3);
               int userId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return merchantId;
    }
    public int db_createDistributor(int cuserId, String fname, String mname, String lname, String email, String tel, String addr,  String addr2, String city, String country, String website, String loginId, String loginPwd, String userTitle, int userTypeId, String currency) {
        int distributorId = 0;
        
        String sp_name = "sp_create_distributor";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pUSERID", Types.NUMERIC);
            cs.registerOutParameter("pDISTRIBUTORID", Types.NUMERIC);
            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);
            cs.setString("pEMAIL", email);            
            cs.setString("pTELEPHONE", tel);    
            
            cs.setString("pADDRESS", addr);
            cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            
            cs.setString("pLOGINID", loginId);
            cs.setString("pLOGINPASSWORD", loginPwd);
            cs.setString("pUSERTITLE", userTitle);
            cs.setInt("pCREATEDBYUSERID", cuserId);
            cs.setInt("pUSERTYPEID", userTypeId);
            cs.setString("pCURRENCY", currency);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               distributorId = cs.getInt(3);
               int userId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return distributorId;
    }
    public boolean db_updateDistributor(int cuserId, int distributorId, String fname, String mname, String lname, String email, String tel, String addr, String addr2, String city, String country, String website, String userTitle) {
        boolean res = false;
        
        String sp_name = "sp_update_distributor";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pDISTRIBUTORID", distributorId);  
             //cs.setString("pLOGINID", loginId);            
            cs.setString("pUSERTITLE", userTitle); 
            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);
            cs.setString("pEMAIL", email);            
            cs.setString("pTELEPHONE", tel);
            
            cs.setString("pADDRESS", addr);   
            cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public int db_createMerchant(int cuserId, int distributorId, String merchantUid, String fname, String mname, String lname, String email, String tel, String addr, String addr2, String city, String country, String website, String loginId, String loginPwd, String userTitle, int userTypeId, int productId, int merchantGroupId, String currency) {
        int merchantId = 0;
        
        String sp_name = "sp_create_merchant";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pUSERID", Types.NUMERIC);
            cs.registerOutParameter("pMERCHANTID", Types.NUMERIC);
            
            cs.setInt("pDISTRIBUTORID", distributorId);
            cs.setString("pMERCHANTUID", merchantUid);            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);
            
            cs.setString("pEMAIL", email);                        
            cs.setString("pTELEPHONE", tel);            
            cs.setString("pADDRESS", addr);            
            cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            
            cs.setString("pLOGINID", loginId);
            cs.setString("pLOGINPASSWORD", loginPwd);
            
            cs.setString("pUSERTITLE", userTitle);
            cs.setInt("pCREATEDBYUSERID", cuserId);
            cs.setInt("pUSERTYPEID", userTypeId);
            cs.setInt("pPRODUCTID", productId);
            cs.setInt("pMERCHANTGROUPID", merchantGroupId);
            cs.setString("pCURRENCY", currency);            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               merchantId = cs.getInt(3);
               int userId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return merchantId;
    }
    public boolean db_updateMerchant(int cuserId, int merchantId, String fname, String mname, String lname, String email, String tel, String addr,   String userTitle, int productId, String addr2, String city, String country, String website, int merchantGroupId) {
        boolean res = false;
        
        String sp_name = "sp_update_merchant";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pMERCHANTID", merchantId);  
            //cs.setString("pLOGINID", loginId);            
            cs.setString("pUSERTITLE", userTitle); 
            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);
            cs.setString("pEMAIL", email);            
            cs.setString("pTELEPHONE", tel);
            cs.setString("pADDRESS", addr);  
            cs.setInt("pPRODUCTID", productId);  
            
            cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            cs.setInt("pMERCHANTGROUPID", merchantGroupId);  
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public boolean db_updateMerchantSubscription(int doneByUserId, int merchantId, int subscriptionTypeId, int subscriptionAutoRenewal) {
        boolean res = false;
        
        String sp_name = "sp_update_merchant_subscription";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pMERCHANTID", merchantId);  
            cs.setInt("pSUBSCRIPTIONTYPEID", subscriptionTypeId); 
            cs.setInt("pSUBSCRIPTIONAUTORENEWAL", subscriptionAutoRenewal);             
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
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public boolean db_update_cashier_hppinfo(int doneByUserId, int cashierId, String hppSuccessUrl, String hppFailureUrl){
        boolean res = false;
        
        String sp_name = "sp_update_cashier_hppinfo";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pDONEBYUSERID", doneByUserId);  
            cs.setInt("pCASHIERID", cashierId);  
            
            cs.setString("pHPPSUCCESSURL", hppSuccessUrl);             
            cs.setString("pHPPFAILUREURL", hppFailureUrl);      
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
//    public boolean db_update_cashier_hpp_subscription(int doneByUserId, int cashierId, int flag, String hppKey){
//        boolean res = false;
//        
//        String sp_name = "sp_update_cashier_hpp_subsription";           
//        CallableStatement cs = null;
//        try {
//            retCode = 0; Connection conn = getConnection();
//            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
//            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
//            
//            cs.setInt("pDONEBYUSERID", doneByUserId);  
//            cs.setInt("pCASHIERID", cashierId);  
//            
//            cs.setInt("pSUBSRIPTIONSTATUS", flag);             
//            cs.setString("pHPPKEY", hppKey);      
//            
//            cs.executeUpdate();
//            
//            ResultSet rset = null;
//            retCode = cs.getInt(1);
//            logger.info("db-returncode: "+retCode);
//            if(retCode == DBRC_SUCCESS){
//               res = true;
//            }
//            cs.closeOnCompletion();
//        } catch (Exception e) {
//            logger.severe("db_call_error: "+e.getMessage());
//        }            
//        
//        return res;
//    }
    public boolean db_renewMerchantSubscription(int doneByUserId, int merchantId) {
        boolean res = false;
        
        String sp_name = "sp_renew_merchant_subscription";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);            
            cs.setInt("pMERCHANTID", merchantId);                               
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
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public boolean db_AddMerchantSettlementInfo(int merchantId, int bankId, String settlementAcc, int settlementModeId, int settlementCycleId){
        boolean res = false;
        
        String sp_name = "sp_add_merchant_settlementinfo";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pMERCHANTID", merchantId);
            cs.setInt("pBANKID", bankId);    
            cs.setString("pSETTLEMENTACCOUNT", settlementAcc);            
            cs.setInt("pSETTLEMENTMODEID", settlementModeId);    
            cs.setInt("pSETTLEMENTCYCLEID", settlementCycleId);    
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
              res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public int db_AddMerchantFavCustomer(int cuserid, int merchantId, String custAccountNo, int bonuspoints) {
        int favid=-1;
        
        String sp_name = "sp_create_merchant_favcustomer";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pFAVCUSTOMERID", Types.NUMERIC);
            
            cs.setInt("pMERCHANTID", merchantId);         
            cs.setString("pCUSTOMERACCOUNTNO", custAccountNo);            
            cs.setInt("pBONUSPOINTS", bonuspoints);    
            cs.setInt("pCREATEDBYUSERID", cuserid);    
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
              favid = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return favid;
    }
    public _MerchantFavCustomer db_GetMerchantFavCustomer(int merchantId, String custAccNo) {
        String sp_name = "sp_get_merchant_favcustomer";           
        CallableStatement cs = null;
        _MerchantFavCustomer uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            cs.setInt("pMERCHANTID", merchantId);         
            cs.setString("pCUSTOMERACCOUNTNO", custAccNo);
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _MerchantFavCustomer();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    
    public int db_createCashier(int cuserId, int merchantId, int cashierTypeId, String fname, String mname, String lname, String email, String tel, String addr, String addr2, String city, String country, String website, String loginId, String loginPwd, String userTitle, int userTypeId, int statusId, String activationToken, String secretKey) {
        int cashierId = 0;
        
        String sp_name = "sp_create_cashier";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pUSERID", Types.NUMERIC);
            cs.registerOutParameter("pCASHIERID", Types.NUMERIC);
            
            cs.setInt("pMERCHANTID", merchantId);
            cs.setInt("pCASHIERTYPEID", cashierTypeId);            
            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);            
            cs.setString("pEMAIL", email);                        
            cs.setString("pTELEPHONE", tel);            
            cs.setString("pADDRESS", addr);
            cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            
            cs.setString("pLOGINID", loginId);
            cs.setString("pLOGINPASSWORD", loginPwd);            
            cs.setString("pUSERTITLE", userTitle);
            cs.setInt("pCREATEDBYUSERID", cuserId);
            cs.setInt("pUSERTYPEID", userTypeId);
            cs.setInt("pSTATUSID", statusId);
            cs.setString("pACTIVATIONTOKEN", activationToken);
            cs.setString("pSECRETKEY", secretKey);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               cashierId = cs.getInt(3);
               int userId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return cashierId;
    }
    
    public int db_CreateCashierDevice(int cuserid, int cashierId, String manufacturer, String serialNo, String modelNo, String terminalId, int statusId) {
        int deviceId = 0;
        
        String sp_name = "sp_create_cashier_device";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pDEVICEID", Types.NUMERIC);
            
            cs.setInt("pCASHIERID", cashierId);  
            cs.setString("pMANUFACTURER", manufacturer);            
            cs.setString("pSERIALNO", serialNo);             
            cs.setString("pMODELNO", modelNo);
            cs.setString("pTERMINALID", terminalId);
            cs.setInt("pSTATUSID", statusId); 
            cs.setInt("pCREATEDBYUSERID", cuserid);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                deviceId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return deviceId;
    }
    
    public boolean db_updateCashierDeviceInfo(int createdByUserId, int cashierId, int deviceId, String manufacturer, String serialNo, String modelNo, String terminalId, int statusId) {
        boolean res = false;
        
        String sp_name = "sp_update_cashier_deviceinfo";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pCASHIERID", cashierId);  
            cs.setInt("pDEVICEID", deviceId);  
            cs.setString("pMANUFACTURER", manufacturer);            
            cs.setString("pSERIALNO", serialNo);             
            cs.setString("pMODELNO", modelNo);
            cs.setString("pTERMINALID", terminalId);
            cs.setInt("pSTATUSID", statusId); 
            cs.setInt("pCREATEDBYUSERID", createdByUserId);                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public boolean db_update_user_profilestatus (int userId, int statusId){
        boolean res = false;
        
        String sp_name = "sp_update_user_profilestatus";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pUSERID", userId);  
            cs.setInt("pSTATUSID", statusId);  
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public int db_CreateBank(int cuserid, String bankName, String bankCode, String address, int ppid) {
        int bankId = 0;
        
        String sp_name = "sp_create_bank";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pBANKID", Types.NUMERIC);
            
            cs.setString("pBANKNAME", bankName);  
            cs.setString("pBANKCODE", bankCode);            
            cs.setString("pADDRESS", address);             
            cs.setInt("pPAYMENTPROCESSORID", ppid);           
            cs.setInt("pCREATEDBYUSERID", cuserid);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                bankId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return bankId;
    }
    public boolean db_updateBank(int bankId, String bankName, String bankCode, String address, int ppid) {
        boolean res = false;
        
        String sp_name = "sp_update_bankinfo";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.setInt("pBANKID", bankId); 
            cs.setString("pBANKNAME", bankName);  
            cs.setString("pBANKCODE", bankCode);            
            cs.setString("pADDRESS", address);             
            cs.setInt("pPAYMENTPROCESSORID", ppid);           
                            
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public List<_BankInfo> db_get_bank_List() {
        String sp_name = "sp_get_banklist";           
        CallableStatement cs = null;
        List<_BankInfo> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _BankInfo obj = new _BankInfo();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    public _BankInfo db_get_BankInfo(int bankId) {
        String sp_name = "sp_get_bankinfo";           
        CallableStatement cs = null;
        _BankInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pBANKID", bankId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _BankInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
           cs.closeOnCompletion(); 
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public int db_createPaymentProcessorUser(int cuserId,  String fname, String mname, String lname, String email, String tel, String addr, String addr2, String city, String country, String website, String loginId, String loginPwd, String userTitle, int userTypeId, int pptypeid, String currency, int productId, int systemId) {
        int issuerId = 0;
        
        String sp_name = "sp_create_paymentprocessoruser";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pUSERID", Types.NUMERIC);
            cs.registerOutParameter("pPAYMENTPROCESSORID", Types.NUMERIC);    
            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);            
            cs.setString("pEMAIL", email);                        
            cs.setString("pTELEPHONE", tel); 
            
            cs.setString("pADDRESS", addr);
             cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            
            cs.setString("pLOGINID", loginId);
            cs.setString("pLOGINPASSWORD", loginPwd);            
            cs.setString("pUSERTITLE", userTitle);
            cs.setInt("pCREATEDBYUSERID", cuserId);            
            cs.setInt("pUSERTYPEID", userTypeId);
            
            cs.setInt("pPAYMENTPROCESSORTYPEID", pptypeid);
            cs.setString("pCURRENCY", currency);
            cs.setInt("pPRODUCTID", productId);
            cs.setInt("pSYSTEMID", systemId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               issuerId = cs.getInt(3);
               int userId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return issuerId;
    }
     public boolean db_update_PaymentProcessorUser(int cuserId, int paymentProcessorId, int paymentProcessorTypeId, String fname, String mname, String lname, String email, String tel, String addr,  String addr2, String city, String country, String website,String userTitle, int systemId) {
        boolean res = false;
        
        String sp_name = "sp_update_paymentprocessoruser";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,? )}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pPAYMENTPROCESSORID", paymentProcessorId);
            cs.setInt("pPAYMENTPROCESSORTYPEID", paymentProcessorTypeId);
             //cs.setString("pLOGINID", loginId);            
            cs.setString("pUSERTITLE", userTitle); 
            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);  
            cs.setString("pEMAIL", email);            
            cs.setString("pTELEPHONE", tel);
            cs.setString("pADDRESS", addr); 
            cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            cs.setInt("pSYSTEMID", systemId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public boolean db_updateCashier(int cuserId, int cashierId, String fname, String mname, String lname, String email, String tel, String addr,  String addr2, String city, String country, String website,  String userTitle) {
        boolean res = false;
        
        String sp_name = "sp_update_cashier";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pCASHIERID", cashierId);  
            //cs.setString("pLOGINID", loginId);            
            cs.setString("pUSERTITLE", userTitle); 
            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);
            cs.setString("pEMAIL", email);            
            cs.setString("pTELEPHONE", tel);
            
            cs.setString("pADDRESS", addr);     
             cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    public boolean db_updateCashierDeviceId(int cashierId, int deviceId) {
        boolean res = false;
        
        String sp_name = "sp_update_cashier_deviceid";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pCASHIERID", cashierId);  
            cs.setInt("pDEVICEID", deviceId);            
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
   
    public boolean db_update_AdminOperator(int cuserId, int operatorId, String fname, String mname, String lname, String email, String tel, String addr,  String addr2, String city, String country, String website, String userTitle) {
        boolean res = false;
        
        String sp_name = "sp_update_adminoperator";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            
            cs.setInt("pADMINOPERATORID", operatorId);  
             //cs.setString("pLOGINID", loginId);            
            cs.setString("pUSERTITLE", userTitle); 
            
            cs.setString("pFIRSTNAME", fname);
            cs.setString("pMIDDLENAME", mname);
            cs.setString("pLASTNAME", lname);  
            cs.setString("pEMAIL", email);            
            cs.setString("pTELEPHONE", tel);
            
            cs.setString("pADDRESS", addr);  
            cs.setString("pADDRESS2", addr2);
            cs.setString("pCITY", city);
            cs.setString("pCOUNTRY", country);
            cs.setString("pWEBSITE", website);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               res = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return res;
    }
    
    public List<_UserInfo> db_get_User_List(int userTypeId) {
        String sp_name = "sp_get_userlist";           
        CallableStatement cs = null;
        List<_UserInfo> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pUSERTYPEID", userTypeId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _UserInfo obj = new _UserInfo();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    public _UserInfo db_get_User_Info(int userId) {
        String sp_name = "sp_get_userinfo";           
        CallableStatement cs = null;
        _UserInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pUSERID", userId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _UserInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
           cs.closeOnCompletion(); 
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _DeviceInfo db_get_DeviceInfo(int deviceId) {
        String sp_name = "sp_get_deviceinfo";           
        CallableStatement cs = null;
        _DeviceInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pDEVICEID", deviceId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _DeviceInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
           cs.closeOnCompletion(); 
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _DeviceInfo db_get_DeviceInfo_by_CashierId(int cashierId) {
        String sp_name = "sp_get_deviceinfo_bycashierid";           
        CallableStatement cs = null;
        _DeviceInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pCASHIERID", cashierId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _DeviceInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
           cs.closeOnCompletion(); 
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _DistributorInfo db_get_Distributor_Info(int distributorId) {
        String sp_name = "sp_get_distributorinfo";           
        CallableStatement cs = null;
        _DistributorInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pDISTRIBUTORID", distributorId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _DistributorInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _PaymentProcessorInfo db_get_PaymentProcessorUserInfo(int ppid) {
        String sp_name = "sp_get_paymentprocessorUserinfo";           
        CallableStatement cs = null;
        _PaymentProcessorInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pPAYMENTPROCESSORID", ppid);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _PaymentProcessorInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    
    public _MerchantSettlementInfo db_get_Merchant_SettlementInfo(int merchantId) {
        String sp_name = "sp_get_merchant_settlementinfo";           
        CallableStatement cs = null;
        _MerchantSettlementInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pMERCHANTID", merchantId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _MerchantSettlementInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    
    
    public _MerchantInfo db_get_Merchant_Info(int merchantId) {
        String sp_name = "sp_get_merchantinfo";           
        CallableStatement cs = null;
        _MerchantInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pMERCHANTID", merchantId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _MerchantInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _MerchantInfo db_get_Merchant_Info_ByUid(String merchantUid) {
        String sp_name = "sp_get_merchantinfo_byuid";           
        CallableStatement cs = null;
        _MerchantInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setString("pMERCHANTUID", merchantUid);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _MerchantInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public boolean db_update_cashier_activation_status_withtoken(int cashierId, int status, String token, int doneByUserId) {
        String sp_name = "sp_reset_cashier_activation";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pCASHIERID", cashierId);
            cs.setInt("pSTATUSID", status);
            cs.setString("pACTIVATIONTOKEN", token);
            cs.setInt("pDONEBYUSERID", doneByUserId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
     public boolean db_update_merchant_account_status(int doneByUserId, int merchantId, String desc, int status) {
        String sp_name = "sp_update_merchant_accountstatus";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pMERCHANTID", merchantId);      
            cs.setString("pDESCRIPTION", desc);
            cs.setInt("pSTATUSID", status);
            cs.setInt("pDONEBYUSERID", doneByUserId);
         
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _CashierInfo db_get_Cashier_Info(int cashierId) {
        String sp_name = "sp_get_cashierinfo";           
        CallableStatement cs = null;
        _CashierInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pCASHIERID", cashierId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _CashierInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
           cs.closeOnCompletion(); 
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _CashierInfo db_get_Cashier_Info_ByUserId(int userId) {
        String sp_name = "sp_get_cashierinfo_byuserid";           
        CallableStatement cs = null;
        _CashierInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pUSERID", userId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _CashierInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _UserAuthInfo db_get_User_AuthInfo_ByLoginId(String loginId, int channelId) {
        String sp_name = "sp_get_user_authinfo";           
        CallableStatement cs = null;
        _UserAuthInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setString("pLOGINID", loginId);
            cs.setInt("pCHANNELID", channelId);
            
            cs.executeUpdate();
            
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
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _UserAuthInfo db_get_User_AuthInfo_ByUserId(int userId) {
        String sp_name = "sp_get_user_authinfo_byuserid";           
        CallableStatement cs = null;
        _UserAuthInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            
            cs.setInt("pUSERID", userId);
            
            cs.executeUpdate();
            
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
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public boolean db_update_User_Pwd(int userId, int pwdType, String pwd) {
        String sp_name = "sp_update_user_pwd";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pUSERID", userId);
            cs.setInt("pPWDTYPE", pwdType);
            cs.setString("pPWD", pwd);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public boolean db_update_User_Roles(int userId, String userRoles) {
        String sp_name = "sp_update_user_roles";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pUSERID", userId);
            cs.setString("pROLES", userRoles);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
     public boolean db_update_Role_Permissions(int roleId, String rolePermissions) {
        String sp_name = "sp_update_role_permissions";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pROLEID", roleId);
            cs.setString("pPERMISSIONS", rolePermissions);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
      public List<_MerchantInfo> db_get_Distributor_Merchant_List(int distributorId) {
        String sp_name = "sp_get_distributor_merchants";           
        CallableStatement cs = null;
        List<_MerchantInfo> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pDISTRIBUTORID", distributorId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _MerchantInfo obj = new _MerchantInfo();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
      public List<_CashierInfo> db_get_Merchant_Cashier_List(int merchantId) {
        String sp_name = "sp_get_merchant_cashiers";           
        CallableStatement cs = null;
        List<_CashierInfo> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pMERCHANTID", merchantId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _CashierInfo obj = new _CashierInfo();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
       public List<_PaymentProcessorInfo> db_get_PaymentProcessorUser_List() {
        String sp_name = "sp_get_all_paymentprocessors";           
        CallableStatement cs = null;
        List<_PaymentProcessorInfo> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _PaymentProcessorInfo obj = new _PaymentProcessorInfo();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
       public List<_DistributorInfo> db_get_Distributors_List() {
        String sp_name = "sp_get_all_distributors";           
        CallableStatement cs = null;
        List<_DistributorInfo> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _DistributorInfo obj = new _DistributorInfo();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
  public List<_User_WebMenuPermission> db_get_User_WebPermissions(int userId) {
        String sp_name = "sp_get_user_web_permissions";           
        CallableStatement cs = null;
        List<_User_WebMenuPermission> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pUSERID", userId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _User_WebMenuPermission obj = new _User_WebMenuPermission();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    public int db_create_requestlog(int userId, String serviceCode, String requestContent, String clientReferenceId, String responseContent) {
        int requestId = 0;
        
        String sp_name = "sp_create_requestlog";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pREQUESTID", Types.NUMERIC);            
            
            cs.setInt("pUSERID", userId);
            cs.setString("pSERVICECODE", serviceCode);
            cs.setString("pREQUESTCONTENT", requestContent);
            cs.setString("pCLIENTREFERENCEID", clientReferenceId);            
            cs.setString("pRESPONSECONTENT", responseContent); 
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               requestId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return requestId;
    }
    public int db_create_merchantorder(int merchantId, int cashierId, String referenceId, String invoiceId, double amount, String currency, int createdByUserId, int requestId, String serviceCode, String tranDesc, int statusId, int pmtMethodId, String scburl, String fcburl, int hppRespDataFormatId) {
        int orderId = 0;
        
        String sp_name = "sp_create_merchantorder";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pORDERID", Types.NUMERIC);            
            
            cs.setInt("pCASHIERID", cashierId);
            cs.setInt("pMERCHANTID", merchantId);
            cs.setString("pREFERENCEID", referenceId);
            cs.setString("pINVOICEID", invoiceId);
            cs.setDouble("pAMOUNT", amount);            
            cs.setString("pCURRENCY", currency);
            cs.setInt("pCREATEDBYUSERID", createdByUserId); 
            cs.setInt("pREQUESTID", requestId); 
            cs.setString("pSERVICECODE", serviceCode);
            cs.setString("pTRANDESC", tranDesc);
            cs.setInt("pSTATUSID", statusId); 
            cs.setInt("pPAYMENTMETHODID", pmtMethodId); 
            cs.setString("pSCBURL", scburl);
            cs.setString("pFCBURL", fcburl);
            cs.setInt("pHPPRESPDATAFORMATID", hppRespDataFormatId); 
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               orderId = cs.getInt(2);               
            }else if(retCode==DBRC_DUPLICATE_RECORD){
                orderId = DBRC_DUPLICATE_RECORD;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return orderId;
    }
    public int db_create_merchantorder_VendorResponse(int orderId, String issuerTranId, String issuerApprovalCode, String issuerRespCode, int resultCode, String resultMsg, String issuerTimestamp,
            String cardType, String cardholder, String f414, String expDate, String desc, double chargedAmount) {
        int seqid = 0;
        
        String sp_name = "sp_create_merchantorder_vendorresponse";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pSEQID", Types.NUMERIC);            
            
            cs.setInt("pORDERID", orderId);
            cs.setString("pISSUERTRANSACTIONID", issuerTranId);
            cs.setString("pISSUERAPPROVALCODE", issuerApprovalCode);
            cs.setString("pISSUERRESPCODE", issuerRespCode);
                    
            cs.setString("pISSUERTIMESTAMP", issuerTimestamp); 
            cs.setInt("pRESULTCODE", resultCode);    
            cs.setString("pRESULTMSG", resultMsg);
            
            cs.setString("pCARDTYPE", cardType);
            cs.setString("pCARDHOLDER", cardholder);
            cs.setString("pCARDNO", f414);
            cs.setString("pCARDEXPDATE", expDate);
            cs.setString("pDESCRIPTION", desc);
            cs.setDouble("pCHARGEDAMOUNT", chargedAmount);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               seqid = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return seqid;
    }
    public _MerchantOrder db_get_merchantOrderInfo(int orderId) {
        String sp_name = "sp_get_merchantorder";           
        CallableStatement cs = null;
        _MerchantOrder uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pORDERID", orderId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _MerchantOrder();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _MerchantOrderResult db_get_merchantOrder_result(int orderId) {
        String sp_name = "sp_get_merchantorder_result";           
        CallableStatement cs = null;
        _MerchantOrderResult uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pORDERID", orderId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _MerchantOrderResult();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public boolean db_update_merchantorder_status(int orderId, int status, int doneByUserId) {
        String sp_name = "sp_update_merchantorder_status";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pORDERID", orderId);
            cs.setInt("pSTATUSID", status);            
            cs.setInt("pDONEBYUSERID", doneByUserId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    

    
    public boolean db_update_merchantorder_procinfo(int orderId, int transactionId, String errCode, String desc) {
        String sp_name = "sp_update_merchantorder_procinfo";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pORDERID", orderId);
            cs.setInt("pTRANSACTIONID", transactionId);            
            cs.setString("pERRORCODE", errCode);
            cs.setString("pDESCRIPTION", desc);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _PaymentProcessorInfoWithSystem db_getCustomerRoutingInfo(String accountNo) {
        String sp_name = "sp_get_cust_pmtproc_info";           
        CallableStatement cs = null;
        _PaymentProcessorInfoWithSystem uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setString("pACCOUNTNO", accountNo);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _PaymentProcessorInfoWithSystem();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _PaymentProcessorInfoWithSystem db_getMerchantRoutingInfo(int merchantId) {
        String sp_name = "sp_get_merchant_pmtproc_info";           
        CallableStatement cs = null;
        _PaymentProcessorInfoWithSystem uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pMERCHANTID", merchantId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _PaymentProcessorInfoWithSystem();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _PaymentProcessorInfoWithSystem db_getTranRoutingInfo(int transactionId) {
        logger.fine("TransactionId: "+transactionId);
        String sp_name = "sp_get_tran_pmtproc_info";           
        CallableStatement cs = null;
        _PaymentProcessorInfoWithSystem uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pTRANSACTIONID", transactionId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _PaymentProcessorInfoWithSystem();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    

    public int db_createTransaction(int tranTypeId, int requestId, int createdByUserId, int PoSDevicedId, String payerId, String payerIdName, int payerIdType, int cashierId, int merchantId, int payerBankId, int paymentMethodId, int paymentChannelId, double sxTranAmount, String currency, String invoiceId, String description, double sxTranCharges, int transtatusid, int serviceId, int chargeModeId, double rxTranCharges, 
            String issuerTranId, String pmtProcTranId,  int refTranId, int sxUserId, int sxUserTypeId, int sxAccountId, int rxUserId, int rxUserTypeId, int rxAccountId) {
        int tranId = 0;
        
        String sp_name = "sp_create_transaction";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,? )}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pTRANSACTIONID", Types.NUMERIC); 
           
            cs.setInt("pTRANTYPEID", tranTypeId);
            cs.setInt("pREQUESTID", requestId);
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
            cs.setString("pTRANDESC", description);
            cs.setInt("pTRANSTATUSID", transtatusid);
            cs.setInt("pSERVICEID", serviceId);
            cs.setInt("pCHARGEMODEID", chargeModeId);
             
            cs.setDouble("pSXTRANAMOUNT", sxTranAmount); 
            cs.setDouble("pSXTRANCHARGES", sxTranCharges); 
            cs.setDouble("pRXTRANAMOUNT", sxTranAmount); 
            cs.setDouble("pRXTRANCHARGES", rxTranCharges); 
            cs.setString("pCURRENCY", currency);  
            
             cs.setString("pINVOICEID", invoiceId);  
            cs.setInt("pPAYMENTMETHODID", paymentMethodId);
             cs.setInt("pPAYMENTCHANNELID", paymentChannelId);
             cs.setInt("pDEVICEID", PoSDevicedId);
             cs.setInt("pPAYERBANKID", payerBankId);
             
            cs.setString("pISSUERTRANSACTIONID", issuerTranId);
            cs.setString("pPMTPROCTRANSACTIONID", pmtProcTranId);
            cs.setInt("pREFTRANID", refTranId);
             cs.setString("pPAYERID", payerId); 
             cs.setInt("pPAYERIDTYPE", payerIdType);
             cs.setString("pPAYERIDNAME", payerIdName);
             cs.setInt("pCASHIERID", cashierId);
             cs.setInt("pMERCHANTID", merchantId);            
             
             cs.setInt("pSXUSERID", sxUserId);  
             cs.setInt("pSXUSERTYPEID", sxUserTypeId);  
             cs.setInt("pSXACCOUNTID", sxAccountId);  
             
             cs.setInt("pRXUSERID", rxUserId);  
             cs.setInt("pRXUSERTYPEID", rxUserTypeId);  
             cs.setInt("pRXACCOUNTID", rxAccountId);  
             
             
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               tranId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return tranId;
    }
    
    public int db_createTranDetailEntry(int tranId, int userId, int userTypeId, int accountId, double debit, double credit, String remarks) {
        int trandetailid = 0;
        
        String sp_name = "sp_create_trandetail_entry";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pTRANDETAILID", Types.NUMERIC); 
           
            cs.setInt("pTRANSACTIONID", tranId);
            cs.setInt("pUSERID", userId);
            
            cs.setInt("pUSERTYPEID", userTypeId);
            cs.setInt("pACCOUNTID", accountId);
                      
            cs.setDouble("pDEBIT", debit); 
            cs.setDouble("pCREDIT", credit); 
            cs.setString("pREMARKS", remarks); 
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               trandetailid = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return trandetailid;
    }
    public int db_createSettlement(int merchantId, double amount, int statusId, int refTranHeadId, String issuerSettlementId, String description, int isCredit)  {
        int settlementId = 0;
        
        String sp_name = "sp_create_settlement";           
        CallableStatement cs = null;
        try {
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pSETTLEMENTID", Types.NUMERIC);            
                       
            cs.setInt("pMERCHANTID", merchantId);
            cs.setDouble("pAMOUNT", amount); 
            cs.setInt("pSTATUSID", statusId);
           
            cs.setString("pISSUERSETTLEMENTID", issuerSettlementId);             
            cs.setString("pDESCRIPTION", description); 
             cs.setInt("pREFTRANHEADID", refTranHeadId);
             cs.setInt("pSETTLEMENTTRANID", 0);
             cs.setInt("pIsCredit", isCredit);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               settlementId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return settlementId;
    }
    public int db_createAccount(int userId, String accountNo, int accountTypeId, int createdByUserId, String currency)  {
        int accountId = 0;
        
        String sp_name = "sp_create_account";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pACCOUNTID", Types.NUMERIC);            
                       
            cs.setInt("pUSERID", userId);
            cs.setString("pACCOUNTNO", accountNo);             
            cs.setInt("pACCOUNTTYPEID", accountTypeId);
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
             cs.setString("pCURRENCY", currency);   
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               accountId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return accountId;
    }
    public boolean db_updateTran_StatusAndIssuerId(int transactionId, int statusId, String issuerTransactionId, String pmtProcTransactionId) {
        String sp_name = "sp_update_tran_statusandissuerid";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
                         retCode = 0; Connection conn = getConnection();
                        
            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?, ?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            
            cs.setInt("pTRANSACTIONID", transactionId);
            cs.setInt("pSTATUSID", statusId);
            cs.setString("pISSUERTRANSACTIONID", issuerTransactionId);
            cs.setString("pPMTPROCTRANSACTIONID", pmtProcTransactionId);
            logger.info("Executing stored proc: "+sp_name);
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
            
            
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public boolean db_update_AccBalance(int accountId, int operation, double amount) {
        String sp_name = "sp_update_accountbalance";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
            retCode = 0; Connection conn = getConnection();
                        
            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            
            cs.setInt("pACCOUNTID", accountId);
            cs.setInt("pOPERATION", operation);
            cs.setDouble("pAMOUNT", amount);
            
            logger.info("Executing stored proc: "+sp_name);
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
            
            
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    
    public boolean db_updateSettlement_StatusAndIssuerId(int settlementId, int statusId, String issuerSettlementId, int settlementTranId) {
        String sp_name = "sp_update_settlement_statusandissuerid";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?, ?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            
            cs.setInt("pSETTLEMENTID", settlementId);
            cs.setInt("pSTATUSID", statusId);
            cs.setString("pISSUERSETTLEMENTID", issuerSettlementId);
            cs.setInt("pSETTLEMENTTRANID", settlementTranId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public boolean db_updateTran_Status(int transactionId, int statusId) {
        String sp_name = "sp_update_tran_status";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
             
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            
            cs.setInt("pTRANSACTIONID", transactionId);
            cs.setInt("pSTATUSID", statusId);            
            logger.info("Executing SP: "+sp_name);
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public boolean db_updateTranStatus_RefTranId(int transactionId, int statusId, int refTranId) {
        String sp_name = "sp_update_tran_status_reftranid";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
             
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            
            cs.setInt("pTRANSACTIONID", transactionId);
            cs.setInt("pSTATUSID", statusId);            
            cs.setInt("pREFTRANID", refTranId);  
            logger.info("Executing SP: "+sp_name);
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public boolean db_updateSettlement_Status(int settlementId, int statusId) {
        String sp_name = "sp_update_settlement_status";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            
            cs.setInt("pSETTLEMENTID", settlementId);
            cs.setInt("pSTATUSID", statusId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
   
public int db_creditMerchantsPayableAccount(int merchantId, double creditAmount, int transactionId) {

        try {

            int GLAccountId = 1; // TODO make it dynamic from Configuration , current GLAccountId =1 should always exist in GLAccounts table as Payable Account
            String currentTime = new Timestamp(System.currentTimeMillis()).toString();
            String query = "INSERT INTO GLTransactions VALUES (null,'" + currentTime + "'," + GLAccountId + "," + creditAmount + "," + 0 + "," + merchantId + "," + transactionId + ")";
            int key = dm.AddRecord(query);
            return key;

        } catch (Exception ex) {
           logger.log(Level.SEVERE, null, ex);
            return -1;
        }

    }

 public _TransactionInfo db_getTransactionInfo(int TransactionId) {
        String sp_name = "sp_get_transactioninfo";           
        CallableStatement cs = null;
        _TransactionInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            
            cs.setInt("pTRANSACTIONID", TransactionId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _TransactionInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
 
public int db_get_CreateMerchantTranCancelRequest(int pgRequestId, int createdByUserId, int merchantId, int transactionId, int statusId, String desc, String callbackUrl) throws Exception{
        int id = 0;
        
        String sp_name = "sp_create_merchantcancellationrequest";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pCANCELREQUESTID", Types.NUMERIC);            
            
            cs.setInt("pREQUESTID", pgRequestId);
            cs.setInt("pMERCHANTID", merchantId);            
            cs.setInt("pTRANSACTIONID", transactionId);
            cs.setInt("pSTATUSID", statusId);           
            cs.setString("pDESCRIPTION", desc); 
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
               
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
                        
            if(retCode == DBRC_SUCCESS){
               id = cs.getInt(2);               
            }else if(retCode==DBRC_DUPLICATE_RECORD){
                id = DBRC_DUPLICATE_RECORD;
            }
            
            
            
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return id;
    }
public _TranCancelRequest db_get_MerchantTranCancelRequest_Info(int id) {
        String sp_name = "sp_get_merchantcancellationrequestinfo";           
        CallableStatement cs = null;
        _TranCancelRequest uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pCANCELREQUESTID", id);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _TranCancelRequest();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _TranCancelRequest db_get_MerchantTranCancelRequest_Info_ByTranId(int id) {
        String sp_name = "sp_get_trancancelrequestinfonytranid";           
        CallableStatement cs = null;
        _TranCancelRequest uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pTRANSACTIONID", id);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _TranCancelRequest();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
 public List<_TranCancelRequest> db_get_MerchantTranCancelRequests(int merchantId, int statusId, int recordCount) {
        String sp_name = "sp_get_merchantcancellationrequests";           
        CallableStatement cs = null;
        List<_TranCancelRequest> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pMERCHANTID", merchantId);
            cs.setInt("pSTATUSID", statusId);
            cs.setInt("pRECORDCOUNT", recordCount);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _TranCancelRequest obj = new _TranCancelRequest();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
 public boolean db_updateMerchantTranCancelRequest(int doneByUserId, int requestId, int statusId, String description) {
        String sp_name = "sp_update_merchantcancellationrequest";           
        CallableStatement cs = null;
        boolean uobj = false;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?, ?, ?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC); 
            
            cs.setInt("pCANCELREQUESTID", requestId);
            cs.setInt("pSTATUSID", statusId);
            cs.setString("pDESCRIPTION", description);
            cs.setInt("pDONEBYUSERID", doneByUserId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
 
 public List<_TranCancelRequestStatus> db_get_TranCancelRequestStatusList() {
        String sp_name = "sp_get_trancancelrequeststatuslist";           
        CallableStatement cs = null;
        List<_TranCancelRequestStatus> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                      
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _TranCancelRequestStatus obj = new _TranCancelRequestStatus();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
 
public List<_MerchantTransactionDetail> db_get_MerchantTransactions(int merchantId, int statusId, int recordCount) {
        String sp_name = "sp_get_merchant_transactions";           
        CallableStatement cs = null;
        List<_MerchantTransactionDetail> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pMERCHANTID", merchantId);
            cs.setInt("pSTATUSID", statusId);
            cs.setInt("pRECORDCOUNT", recordCount);
            //cs.setDate("pSTARTDATE", startDate);
            //cs.setDate("pENDDATE", endDate);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _MerchantTransactionDetail obj = new _MerchantTransactionDetail();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }


public List<_MerchantTransactionDetail> db_get_MerchantTransactionsById(int merchantId, int statusId, int recordCount, java.sql.Date startDate, java.sql.Date endDate) {
        String sp_name = "sp_get_merchant_transactions_bydate";           
        CallableStatement cs = null;
        List<_MerchantTransactionDetail> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pMERCHANTID", merchantId);
            cs.setInt("pSTATUSID", statusId);
            cs.setInt("pRECORDCOUNT", recordCount);
            cs.setDate("pSTARTDATE", startDate);
            cs.setDate("pENDDATE", endDate);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _MerchantTransactionDetail obj = new _MerchantTransactionDetail();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }


public List<_MerchantTransactionDetail> db_get_MerchantTransactionsByUid(String merchantUid, int statusId, int recordCount, java.sql.Date startDate, java.sql.Date endDate) {
        String sp_name = "sp_get_merchant_transactions_byuid";           
        CallableStatement cs = null;
        List<_MerchantTransactionDetail> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setString("pMERCHANTUID", merchantUid);
            cs.setInt("pSTATUSID", statusId);
            cs.setInt("pRECORDCOUNT", recordCount);
             cs.setDate("pSTARTDATE", startDate);
            cs.setDate("pENDDATE", endDate);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _MerchantTransactionDetail obj = new _MerchantTransactionDetail();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }



public List<_TransactionInfo> db_get_CashierTransactions(int cashierId) {
        String sp_name = "sp_get_cashier_transactions";           
        CallableStatement cs = null;
        List<_TransactionInfo> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pCASHIERID", cashierId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _TransactionInfo obj = new _TransactionInfo();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
public List<_SettlementInfo> db_get_Settlements(int statusId, int recCount, int merchantId) {
        String sp_name = "sp_get_settlements";           
        CallableStatement cs = null;
        List<_SettlementInfo> list = null;
        
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pSTATUSID", statusId);
            cs.setInt("pRECORDCOUNT", recCount);
            cs.setInt("pMERCHANTID", merchantId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _SettlementInfo obj = new _SettlementInfo();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
public List<_SettlementMode> db_get_SettlementModes() {
        String sp_name = "sp_get_settlementmodes";           
        CallableStatement cs = null;
        List<_SettlementMode> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                       
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _SettlementMode obj = new _SettlementMode();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
public List<_SettlementStatus> db_get_SettlementStatusList() {
        String sp_name = "sp_get_settlementstatuslist";           
        CallableStatement cs = null;
        List<_SettlementStatus> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                       
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _SettlementStatus obj = new _SettlementStatus();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
public List<_TranStatus> db_get_TranStatusList() {
        String sp_name = "sp_get_transtatuslist";           
        CallableStatement cs = null;
        List<_TranStatus> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                       
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _TranStatus obj = new _TranStatus();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
public List<_AccountStatus> db_get_AccountStatusList() {
        String sp_name = "sp_get_accountstatuslist";           
        CallableStatement cs = null;
        List<_AccountStatus> list = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                       
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _AccountStatus obj = new _AccountStatus();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
public List<_SubscriptionType> db_get_SubscriptionTypes() {
        String sp_name = "sp_get_merchant_subscriptiontypes";           
        CallableStatement cs = null;
        List<_SubscriptionType> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                       
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _SubscriptionType obj = new _SubscriptionType();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }

public List<_RateType> db_get_RateTypes() {
        String sp_name = "sp_get_ratetypes";           
        CallableStatement cs = null;
        List<_RateType> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
                       
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _RateType obj = new _RateType();
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }

public _SettlementInfo db_getSettlementInfo_ByTranHeadId(int TransactionId) {
        String sp_name = "sp_get_settlementinfo_bytranheadid";           
        CallableStatement cs = null;
        _SettlementInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            
            cs.setInt("pTRANHEADID", TransactionId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _SettlementInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
 public _SettlementInfo db_getSettlementInfo(int settlementId) {
        String sp_name = "sp_get_settlementinfo";           
        CallableStatement cs = null;
        _SettlementInfo uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            
            cs.setInt("pSETTLEMENTID", settlementId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _SettlementInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }

   public _AccountInfo db_getAccountInfo(int accountId) {
        
        String sp_name = "sp_get_accountinfo";           
        CallableStatement cs = null;
        _AccountInfo uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.setInt("pACCOUNTID", accountId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _AccountInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
   public _ServiceInfo db_getServiceInfo(int serviceId) {
        
        String sp_name = "sp_get_serviceinfo";           
        CallableStatement cs = null;
        _ServiceInfo uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.setInt("pSERVICEID", serviceId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _ServiceInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
   
   
   
   public List<_ProductServiceProfile> db_get_Product_ServiceProfiles(int productId) {
        String sp_name = "sp_get_prod_srvcprofiles";           
        CallableStatement cs = null;
        List<_ProductServiceProfile> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pPRODUCTID", productId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _ProductServiceProfile obj = new _ProductServiceProfile();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    public _ProductServiceProfile db_getPrpdSrvcProf_WithCharges(String serviceCode, int productId, double amount) {
        String sp_name = "sp_get_prod_srvcprofile_withcharges";           
        CallableStatement cs = null;
        _ProductServiceProfile uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setString("pSERVICECODE", serviceCode);
            cs.setInt("pPRODUCTID", productId);
            cs.setDouble("pAMOUNT", amount);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _ProductServiceProfile();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _ProductServiceProfile db_getPrpdSrvcProf(String serviceCode, int productId) {
        String sp_name = "sp_get_prod_srvcprofile";           
        CallableStatement cs = null;
        _ProductServiceProfile uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setString("pSERVICECODE", serviceCode);
            cs.setInt("pPRODUCTID", productId);
                      
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _ProductServiceProfile();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    public _ProductServiceProfile db_getPrpdSrvcProfInfo(int serviceProfileId) {
        String sp_name = "sp_get_prod_srvcprofile_byprofileid";           
        CallableStatement cs = null;
        _ProductServiceProfile uobj = null;
        try {
            
                         retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pSERVICEPROFILEID", serviceProfileId);
            
                      
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _ProductServiceProfile();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    
   public int db_createProductServiceprofile(int createdByUserId, int productId, int serviceId, String serviceProfileDesc, int serviceChargeModeId, int maxOutTxAmount, int minOutTxAmount, int maxInTxAmount, int minInTxAmount, int dailyInTxLimit, int dailyOutTxLimit, int monthlyInTxLimit, int monthlyOutTxLimit) throws Exception{
        int serviceProfileId = 0;
        
        String sp_name = "sp_create_productserviceprofile";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pSERVICEPROFILEID", Types.NUMERIC);            
                       
            cs.setInt("pPRODUCTID", productId);
            cs.setInt("pSERVICEID", serviceId);
                     
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
              cs.setString("pPROFILEDESC", serviceProfileDesc);  
            cs.setDouble("pMAXOUTTXAMOUNT", maxOutTxAmount);
            cs.setDouble("pMINOUTTXAMOUNT", minOutTxAmount);
            
             cs.setDouble("pMAXINTXAMOUNT", maxInTxAmount);
            cs.setDouble("pMININTXAMOUNT", minInTxAmount);
            
             cs.setDouble("pDAILYINTXLIMIT", dailyInTxLimit);
            cs.setDouble("pDAILYOUTTXLIMIT", dailyOutTxLimit);
            
             cs.setDouble("pMONTHLYINTXLIMIT", monthlyInTxLimit);
            cs.setDouble("pMONTHLYOUTTXLIMIT", monthlyOutTxLimit);
           cs.setInt("pSERVICECHARGEMODEID", serviceChargeModeId);
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               serviceProfileId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return serviceProfileId;
    }
   public boolean db_updateProductServiceprofile(int productId, int serviceId, String serviceProfileDesc, int serviceChargeModeId, int maxOutTxAmount, int minOutTxAmount, int maxInTxAmount, int minInTxAmount, int dailyInTxLimit, int dailyOutTxLimit, int monthlyInTxLimit, int monthlyOutTxLimit) throws Exception{
         boolean uobj = false;
        
        String sp_name = "sp_update_productserviceprofile";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?, ?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
                      
            cs.setInt("pPRODUCTID", productId);
            cs.setInt("pSERVICEID", serviceId);
            cs.setString("pPROFILEDESC", serviceProfileDesc);  
            cs.setDouble("pMAXOUTTXAMOUNT", maxOutTxAmount);
            cs.setDouble("pMINOUTTXAMOUNT", minOutTxAmount);            
             cs.setDouble("pMAXINTXAMOUNT", maxInTxAmount);
            cs.setDouble("pMININTXAMOUNT", minInTxAmount);
            
             cs.setDouble("pDAILYINTXLIMIT", dailyInTxLimit);
            cs.setDouble("pDAILYOUTTXLIMIT", dailyOutTxLimit);            
             cs.setDouble("pMONTHLYINTXLIMIT", monthlyInTxLimit);
            cs.setDouble("pMONTHLYOUTTXLIMIT", monthlyOutTxLimit);
           cs.setInt("pSERVICECHARGEMODEID", serviceChargeModeId);
           
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
           if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            
            logger.log(Level.SEVERE, e.getMessage(), e);
        }            
        
        return uobj;
    }
   public boolean db_removeProductServiceprofile(int doneByUserId, int productId, int serviceId)throws Exception{
         boolean uobj = false;
        
        String sp_name = "sp_remove_productserviceprofile";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
                      
            cs.setInt("pPRODUCTID", productId);
            cs.setInt("pSERVICEID", serviceId);
            cs.setInt("pDONEBYUSERID", serviceId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
           if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            
            logger.log(Level.SEVERE, e.getMessage(), e);
        }            
        
        return uobj;
    }
   public int db_createServiceprofile(int createdByUserId, int serviceId, String serviceProfileDesc, int serviceChargeModeId, int maxOutTxAmount, int minOutTxAmount, int maxInTxAmount, int minInTxAmount, int dailyInTxLimit, int dailyOutTxLimit, int monthlyInTxLimit, int monthlyOutTxLimit) throws Exception{
        int serviceProfileId = 0;
        
        String sp_name = "sp_create_serviceprofile";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?, ?,?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pSERVICEPROFILEID", Types.NUMERIC);            
                       
            cs.setInt("pSERVICEID", serviceId);
                     
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
              cs.setString("pPROFILEDESC", serviceProfileDesc);  
            cs.setDouble("pMAXOUTTXAMOUNT", maxOutTxAmount);
            cs.setDouble("pMINOUTTXAMOUNT", minOutTxAmount);
            
             cs.setDouble("pMAXINTXAMOUNT", maxInTxAmount);
            cs.setDouble("pMININTXAMOUNT", minInTxAmount);
            
             cs.setDouble("pDAILYINTXLIMIT", dailyInTxLimit);
            cs.setDouble("pDAILYOUTTXLIMIT", dailyOutTxLimit);
            
             cs.setDouble("pMONTHLYINTXLIMIT", monthlyInTxLimit);
            cs.setDouble("pMONTHLYOUTTXLIMIT", monthlyOutTxLimit);
           cs.setInt("pSERVICECHARGEMODEID", serviceChargeModeId);
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               serviceProfileId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return serviceProfileId;
    }
   public boolean db_updateServiceprofile( int serviceId, String serviceProfileDesc, int serviceChargeModeId, int maxOutTxAmount, int minOutTxAmount, int maxInTxAmount, int minInTxAmount, int dailyInTxLimit, int dailyOutTxLimit, int monthlyInTxLimit, int monthlyOutTxLimit) throws Exception{
         boolean uobj = false;
        
        String sp_name = "sp_update_serviceprofile";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?, ?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
                      
            cs.setInt("pSERVICEID", serviceId);
            cs.setString("pPROFILEDESC", serviceProfileDesc);  
            cs.setDouble("pMAXOUTTXAMOUNT", maxOutTxAmount);
            cs.setDouble("pMINOUTTXAMOUNT", minOutTxAmount);            
             cs.setDouble("pMAXINTXAMOUNT", maxInTxAmount);
            cs.setDouble("pMININTXAMOUNT", minInTxAmount);
            
             cs.setDouble("pDAILYINTXLIMIT", dailyInTxLimit);
            cs.setDouble("pDAILYOUTTXLIMIT", dailyOutTxLimit);            
             cs.setDouble("pMONTHLYINTXLIMIT", monthlyInTxLimit);
            cs.setDouble("pMONTHLYOUTTXLIMIT", monthlyOutTxLimit);
           cs.setInt("pSERVICECHARGEMODEID", serviceChargeModeId);
           
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
           if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            
            logger.log(Level.SEVERE, e.getMessage(), e);
        }            
        
        return uobj;
    }
    
   public int db_createService(String serviceCode, String serviceDesc, int isActive, int isTransactional, int createdByUserId)  {
        int serviceId = 0;
        
        String sp_name = "sp_create_service";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pSERVICEID", Types.NUMERIC);            
                       
            cs.setString("pSERVICECODE", serviceCode);
            cs.setString("pSERVICEDESC", serviceDesc);             
            cs.setInt("pISACTIVE", isActive);
            cs.setInt("pISTRANSACTIONAL", isTransactional);
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               serviceId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return serviceId;
    }
   
   public boolean db_updateService( int serviceId, String serviceCode, String serviceDesc, int isActive, int isTransactional)  {
         boolean uobj = false;
        
        String sp_name = "sp_update_service";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
                       
            cs.setInt("pSERVICEID", serviceId);
            cs.setString("pSERVICECODE", serviceCode);
            cs.setString("pSERVICEDESC", serviceDesc);             
            cs.setInt("pISACTIVE", isActive);
            cs.setInt("pISTRANSACTIONAL", isTransactional);
           
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
           if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return uobj;
    }
   
   public List<_ServiceInfo> db_get_ServiceList(){
        String sp_name = "sp_get_service_list";           
        CallableStatement cs = null;
        List<_ServiceInfo> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _ServiceInfo obj = new _ServiceInfo();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
  public List<_ServiceChargeModes> db_get_ServiceChargeModes(){
        String sp_name = "sp_get_service_chargemodes";           
        CallableStatement cs = null;
        List<_ServiceChargeModes> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _ServiceChargeModes obj = new _ServiceChargeModes();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
  
   
   public _ProductInfo db_getProductInfo(int productId) {
        
        String sp_name = "sp_get_productinfo";           
        CallableStatement cs = null;
        _ProductInfo uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.setInt("pPRODUCTID", productId);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _ProductInfo();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
   
   public List<_ProductInfo> db_get_ProductList(int userTypeid){
        String sp_name = "sp_get_product_list";           
        CallableStatement cs = null;
        List<_ProductInfo> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);  
            cs.setInt("pPRODUCTOWNER", userTypeid);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _ProductInfo obj = new _ProductInfo();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
   public int db_createProduct(String productName, String productDesc, int productOwner, int productType, int createdByUserId)  {
        int serviceId = 0;
        
        String sp_name = "sp_create_product";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pPRODUCTID", Types.NUMERIC);            
                       
            cs.setString("pPRODUCTNAME", productName);
            cs.setString("pPRODUCTDESC", productDesc);             
            cs.setInt("pPRODUCTOWNER", productOwner);
            cs.setInt("pPRODUCTTYPE", productType);
            cs.setInt("pCREATEDBYUSERID", createdByUserId);
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
               serviceId = cs.getInt(2);               
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return serviceId;
    }
   
   public boolean db_updateProduct( int productId, String productName, String productDesc, int productOwner, int productType)  {
         boolean uobj = false;
        
        String sp_name = "sp_update_product";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
                       
            cs.setInt("pPRODUCTID", productId);
            cs.setString("pPRODUCTNAME", productName);
            cs.setString("pPRODUCTDESC", productDesc);             
            cs.setInt("pPRODUCTOWNER", productOwner);
            cs.setInt("pPRODUCTTYPE", productType);
           
           
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
           if(retCode == DBRC_SUCCESS){
               uobj = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return uobj;
    }
   public List<_UserType> db_get_UserTypes(){
        String sp_name = "sp_get_usertypes";           
        CallableStatement cs = null;
        List<_UserType> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _UserType obj = new _UserType();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    public List<_ProductType> db_get_ProductTypes(){
        String sp_name = "sp_get_producttypes";           
        CallableStatement cs = null;
        List<_ProductType> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _ProductType obj = new _ProductType();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
    
    public int db_CreateProductServiceRate(int cuserid, int serviceProfId, double senderRate, int senderRateTypeId, double rcvRate, int rcvRateTypeId, double minTxAmt, double maxTxAmt, java.sql.Date effdate, java.sql.Date expdate) {
        int rateId = 0;
        
        String sp_name = "sp_create_prod_servicerate";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);
            cs.registerOutParameter("pRATEID", Types.NUMERIC);
            
            cs.setInt("pSERVICEPROFILEID", serviceProfId);  
            cs.setDouble("pSENDERRATE", senderRate);            
            cs.setInt("pSENDERRATETYPEID", senderRateTypeId);             
            cs.setDouble("pRECEIVERRATE", rcvRate);
            cs.setInt("pRECEIVERRATETYPEID", rcvRateTypeId);
            cs.setDouble("pMINTXAMOUNT", minTxAmt); 
            cs.setDouble("pMAXTXAMOUNT", maxTxAmt);
            cs.setDate("pEFFECTIVEDATE", effdate); 
            cs.setDate("pEXPDATE", expdate);
            cs.setInt("pCREATEDBYUSERID", cuserid);
            
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rateId = cs.getInt(2);
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return rateId;
    }
    
    public boolean db_UpdateProductServiceRate(int rateId, double senderRate, int senderRateTypeId, double rcvRate, int rcvRateTypeId, double minTxAmt, double maxTxAmt, java.sql.Date effdate, java.sql.Date expdate) {
        boolean r = false;
        
        String sp_name = "sp_update_prod_servicerate";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?,?,?,?, ?,?,?,?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);            
            
            cs.setInt("pRATEID", rateId);  
            cs.setDouble("pSENDERRATE", senderRate);            
            cs.setInt("pSENDERRATETYPEID", senderRateTypeId);             
            cs.setDouble("pRECEIVERRATE", rcvRate);
            cs.setInt("pRECEIVERRATETYPEID", rcvRateTypeId);
            cs.setDouble("pMINTXAMOUNT", minTxAmt); 
            cs.setDouble("pMAXTXAMOUNT", maxTxAmt);
            cs.setDate("pEFFECTIVEDATE", effdate); 
            cs.setDate("pEXPDATE", expdate);            
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                r = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return r;
    }
    
public boolean db_RemoveProductServiceRate(int doneByUserId, int rateId) {
        boolean r = false;
        
        String sp_name = "sp_remove_prod_servicerate";           
        CallableStatement cs = null;
        try {
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);            
            cs.setInt("pRATEID", rateId);  
                        
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                r = true;
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        
        return r;
    }
    
public List<_ProductServiceRate> db_get_ProductServiceRates(int serviceProfileId){
        String sp_name = "sp_get_prod_servicerates";           
        CallableStatement cs = null;
        List<_ProductServiceRate> list = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                                    
            cs.setInt("pSERVICEPROFILEID", serviceProfileId);  
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                list = new ArrayList<>();
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                while (rs.hasNext()) {
                    DbRecord record = rs.next();

                    _ProductServiceRate obj = new _ProductServiceRate();
                    logger.info("loadFromDbRecord");
                    obj.loadFromDbRecord(record);

                    list.add(obj);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return list;
    }
public _ProductServiceRate db_get_ProductServiceRate(int rateId) {
        String sp_name = "sp_get_prod_servicerate";           
        CallableStatement cs = null;
        _ProductServiceRate uobj = null;
        try {
            
            retCode = 0; Connection conn = getConnection();
            cs = conn.prepareCall("{call " + sp_name + "(?,?)}");
            cs.registerOutParameter("pRETURNCODE", Types.NUMERIC);                        
            cs.setInt("pRATEID", rateId);
                                 
            cs.executeUpdate();
            
            ResultSet rset = null;
            retCode = cs.getInt(1);
            logger.info("db-returncode: "+retCode);
            if(retCode == DBRC_SUCCESS){
                rset = cs.getResultSet();
                
                logger.info("extractData resultset");
                DbResult rs = new DbResult(rset);
                if (rs.hasNext()) {
                    DbRecord record = rs.next();

                    uobj = new _ProductServiceRate();
                    uobj.loadFromDbRecord(record);
                }
            }
            cs.closeOnCompletion();
        } catch (Exception e) {
            logger.severe("db_call_error: "+e.getMessage());
        }            
        return uobj;
    }
    
}





//    /**
//     * *
//     *
//     * @param accountTitle
//     * @param accountNumber
//     * @return
//     */
//    public int createAccount(String accountTitle, String accountNumber) {
//        try {
//            String query = "INSERT INTO Accounts VALUES(null,'" + accountTitle + "','" + accountNumber + "'," + 1 + ",0,0)";
//            
//            int accountId = dm.AddRecord(query);
//            return accountId;
//        } catch (Exception exp) {
//            return -1;
//        }
//
//    }
//
//    /**
//     * *
//     *
//     * @param merchantAccountId
//     */
//    public void deletAccount(int merchantAccountId) {
//        try {
//            String query = "DELETE FROM ACCOUNTS where AccountId =" + merchantAccountId;
//            
//            dm.DeleteRecord(query);
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    public int createCashier(String name, String username, String password, int merchantId, boolean isAuthorizePoSDevice, boolean isActive) {
//        try {
//
//            String query = "INSERT INTO CASHIERS VALUES(null,'" + name + "','" + username + "','"
//                    + password + "'," + merchantId + "," + isAuthorizePoSDevice + ","
//                    + isActive + ")";
//           
//            int ret = dm.AddRecord(query);
//            return ret;
//        } catch (Exception exp) {
//            return -1;
//        }
//    }
//
//    public List<Cashier> getCashiers() {
//        try {
//            List<Cashier> cashiers = new ArrayList<>();
//            String query = "Select * from Cashiers";
//
//            DbResult rs = dm.getRecord(query);
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                Cashier cashier = new Cashier();
//                cashier.setCashierId(record.getInt("CashierId"));
//                cashier.setMerchantId(record.getInt("MerchantId"));
//                cashier.setName(record.getString("Name"));
//                cashier.setUsername(record.getString("Username"));
//                cashier.setPassword(record.getString("Password"));
//                cashier.setIsAuthorizePoSDevice(record.getBoolean("isAuthorizePoSDevice"));
//                cashier.setIsActive(record.getBoolean("IsActive"));
//                cashiers.add(cashier);
//                
//            }
//         
//            return cashiers;
//        } catch (Exception exp) {
//            Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//    
//    public List<Cashier> getCashier(int merchantId) {
//        try {
//            List<Cashier> cashiers = new ArrayList<>();
//            String query = "Select * from Cashiers where MerchantId="+merchantId;
//
//            
//            DbResult rs = dm.getRecord(query);
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                   
//                Cashier cashier = new Cashier();
//                cashier.setCashierId(record.getInt("CashierId"));
//                cashier.setMerchantId(record.getInt("MerchantId"));
//                cashier.setName(record.getString("Name"));
//                cashier.setUsername(record.getString("Username"));
//                cashier.setPassword(record.getString("Password"));
//                cashier.setIsAuthorizePoSDevice(record.getBoolean("isAuthorizePoSDevice"));
//                cashier.setIsActive(record.getBoolean("IsActive"));
//                cashiers.add(cashier);
//                
//            }
//            
//            return cashiers;
//        } catch (Exception exp) {
//            Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }    
//
//    public boolean updateCashier(int cashierId, String name, String username, String password, int merchantId, int isAuthorizePoSDevice, int isActive) {
//          try {
//            String strQuery = "UPDATE Cashiers "
//                    + " SET Name='" + name +"'"
//                    + " , Username='" + username +"'"
//                    + " , Password='" + password +"'"
//                    + " , MerchantId='" + merchantId + "'"
//                    + " , isAuthorizePoSDevice='" + isAuthorizePoSDevice + "'"
//                    + " , IsActive='" + isActive + "'"
//                    + " WHERE CashierId = " + cashierId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//
//    public Cashier getCashierInfo(int cashierId) {
//
//        try {
//            String strCashierInfoQuery = "SELECT * FROM Cashiers WHERE CashierId=" + cashierId;
//
//            Cashier cashier = new Cashier();
//            DbResult rs = dm.getRecord(strCashierInfoQuery);
//            if (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                cashier.setCashierId(record.getInt("CashierId"));
//                cashier.setName(record.getString("Name"));
//                cashier.setUsername(record.getString("Username"));
//                cashier.setPassword(record.getString("Password"));
//                cashier.setIsAuthorizePoSDevice(record.getBoolean("isAuthorizePoSDevice"));
//                cashier.setIsActive(record.getBoolean("IsActive"));
//            }
//                          
//            return cashier;
//        } catch (Exception ex) {
//
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//
//    }
//    
//    public int createChargingPlan(String chargingPlanName, String validFrom, String validTo, boolean isActive, List<TransactionCharge> transactionCharges) {
//        try {
//            String query = "INSERT INTO CHARGINGPLANS VALUES(null,'" + chargingPlanName + "','" + validFrom + "','" + validTo + "'," + isActive + ")";
//            
//            int retChargingPlanId = dm.AddRecord(query);
//
//            if (retChargingPlanId != -1) {
//
//                for (TransactionCharge charge : transactionCharges) {
//                    double chargeValue = charge.getChargeValue();
//                    int chargeValueTypeId = charge.getChargeValueTypeId();
//                    int transactionTypeId = charge.getTransactionTypeId();
//                    String queryCharges = "INSERT INTO TRANSACTIONCHARGES VALUES(null," + chargeValue + "," + chargeValueTypeId + "," + retChargingPlanId + "," + transactionTypeId + ")";
//                    dm.AddRecord(queryCharges);
//                }
//            }
//            return retChargingPlanId;
//        } catch (Exception exp) {
//            return -1;
//        }
//    }
//    
//    public int updateChargingPlan(int chargingPlanId, String chargingPlanName, String validFrom, String validTo, String isActive, List<TransactionCharge> transactionCharges) {
//        try {
//            String query = "UPDATE CHARGINGPLANS"
//                    + " SET ChargingPlanName= "+"'"+chargingPlanName+"'"
//                    + " ,ValidFrom="+"'"+validFrom+"'"
//                    + ", ValidTo= "+"'"+validTo+"'"
//                    + ", IsActive="+"'"+isActive+"'"
//                    + " WHERE ChargingPlanId="+chargingPlanId;
//           
//            int retChargingPlanId = dm.AddRecord(query);
//
//            if (retChargingPlanId != -1) {
//
//                for (TransactionCharge charge : transactionCharges) {
//                    double chargeValue = charge.getChargeValue();
//                    int chargeValueTypeId = charge.getChargeValueTypeId();
//                    int transactionTypeId = charge.getTransactionTypeId();
//                    int chargeId = charge.getChargeId();
//                    String queryCharges = "UPDATE TRANSACTIONCHARGES "
//                            + "SET  ChargeValue="+ "'"+ chargeValue+"'"
//                            + ", ChargeValueTypeId="+"'"+chargeValueTypeId+"'"
//                            + ", ChargingPlanId="+"'"+chargingPlanId+"'"
//                            + ", TransactionTypeId="+"'"+transactionTypeId+"'"
//                            + "WHERE ChargeId="+ chargeId;
//                    dm.AddRecord(queryCharges);
//                }
//            }
//            return retChargingPlanId;
//        } catch (Exception exp) {
//            return -1;
//        }
//    }
//
//    public List<ChargingPlan> getAllPlans() {
//
//        List<ChargingPlan> plans = new ArrayList<>();
//    
//        try {
//            String strQuery = "select * from ChargingPlans ";
//            
//            DbResult rs = dm.getRecord(strQuery);
//           
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                ChargingPlan plan = new ChargingPlan();               
//                plan.setChargingPlanName(record.getString("chargingPlanName"));
//                plan.setValidFrom(record.getString("validFrom"));
//                plan.setValidTo(record.getString("validTo"));
//                plan.setChargingPlanId(record.getInt("ChargingPlanId"));
//                plan.setIsActive(record.getBoolean("IsActive"));
//                String queryCharges = "select * from TransactionCharges WHERE ChargingPlanId= " + plan.getChargingPlanId();
//               
//                 List<TransactionCharge> charges = new ArrayList<>();
//                DbResult rsCharges = dm.getRecord(queryCharges);
//                while (rsCharges.hasNext()) {
//                    DbRecord record1 = rsCharges.next();
//                    
//                    TransactionCharge charge = new TransactionCharge();
//                    charge.setChargeId(record1.getInt("ChargeId"));
//                    charge.setChargeValue(record1.getDouble("ChargeValue"));
//                    charge.setChargeValueTypeId(record1.getInt("ChargeValueTypeId"));
//                    charge.setTransactionTypeId(record1.getInt("TransactionTypeId"));
//                    charges.add(charge);
//                }
//                plan.setTransactionCharges(charges);
//                plans.add(plan);
//
//            }
//
//            
//            return plans;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public TransactionCharge getTransactionCharge(int chargingPlanId, int transactionTypeId) {
//
//        try {
//            String strChargesQuery = "select * from transactioncharges "
//                    + "where ChargingPlanId =" + chargingPlanId
//                    + " AND TransactionTypeId =" + transactionTypeId;
//            DbResult rs = dm.getRecord(strChargesQuery);
//            TransactionCharge charge = new TransactionCharge();
//            if (rs.hasNext()) {
//                DbRecord record = rs.next();
//                        
//                charge.setChargeId(record.getInt("ChargeId"));
//                charge.setChargeValue(Double.parseDouble(record.getString("ChargeValue")));
//                charge.setChargeValueTypeId(record.getInt("ChargeValueTypeId"));
//                charge.setChargingPlanId(record.getInt("ChargingPlanId"));
//                charge.setTransactionTypeId(record.getInt("TransactionTypeId"));
//              
//            }
//
//            return charge;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//
//    }
//    
//    public boolean createCardPrefixes(String cardPrefixes, int processorId) {
//
//        try {
//            String query = "INSERT INTO CARDPREFIXES VALUES('" + cardPrefixes + "'," + processorId + ")";
//            
//            int ret = dm.AddRecord(query);
//            if (ret != -1) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//
//    }
//    //get Card prefex
//    public String getCardPrexes(int bankId) {
//        String cardPFix="";
//        try{
//            String query = "SELECT * FROM CARDPREFIXES WHERE BankId = "+bankId;
//            DbResult rs = dm.getRecord(query);
//            if(rs.hasNext()) {
//                DbRecord record = rs.next();
//                cardPFix = record.getString("CardPrefix");
//            }
//
//            return cardPFix;
//        }
//        catch (Exception ex)
//        {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//        
//    }
//    
//    public List<Country> getCountries() {
//
//        List<Country> countries = new ArrayList<>();
//        try {
//            
//            DbResult rs = dm.getRecord("Select * from Countries");
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//
//                Country country = new Country();
//                country.Id = record.getInt("CountryId");
//                country.name = record.getString("Name");
//                countries.add(country);
//            }
//
//            return countries;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//        }
//
//        return null;
//    }
//    
//    public String getCountry(int countryId) {
//
//    //    List<Country> countries = new ArrayList<>();
//        try {
//            
//            DbResult rs = dm.getRecord("Select * from Countries where CountryId="+countryId);        
//            Country country = new Country();
//            if (rs.hasNext()) {
//                DbRecord record = rs.next();
//                country.Id = record.getInt("CountryId");
//                country.name = record.getString("Name");
//           //     countries.add(country);
//            }
//
//            return country.name;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//        }
//
//        return null;
//    }
//    
//    public List<State> getStates(int countryId) {
//        List<State> states = new ArrayList<>();
//        try {
//            
//            DbResult rs = dm.getRecord("Select * from States WHERE CountryId = " + countryId);
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                State state = new State();
//                state.setStateId(record.getInt("StateId"));
//                state.setName(record.getString("Name"));
//                states.add(state);
//            }
//
//            return states;
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//        }
//
//        return null;
//    }
//    
//    public String getState(int stateId) {
//      //  List<State> states = new ArrayList<>();
//        try {
//            
//            DbResult rs = dm.getRecord("Select * from States WHERE StateId = " + stateId);
//           
//            State state = new State();
//            if(rs.hasNext()) {
//                DbRecord record = rs.next();
//                state.setStateId(record.getInt("StateId"));
//                state.setName(record.getString("Name"));
//             //   states.add(state);
//            }
//
//            return state.getName();
//        } catch (Exception ex) {
//          logger.log(Level.SEVERE, null, ex);
//        }
//
//        return null;
//    }
//
//    public List<City> getCities(int stateId) {
//        List<City> cities = new ArrayList<>();
//        try {
//            
//            DbResult rs = dm.getRecord("Select * from Cities WHERE StateId = " + stateId);
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                City city = new City();
//                city.setId(record.getInt("CityId"));
//                city.setName(record.getString("Name"));
//                cities.add(city);
//            }
//
//            return cities;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//
//    }
//    
//    public String getCity(int cityId) {
//      //  List<City> cities = new ArrayList<>();
//        try {
//            
//            DbResult rs = dm.getRecord("Select * from Cities WHERE CityId = " + cityId); 
//            City city = new City();
//            if(rs.hasNext()) {
//                DbRecord record = rs.next();
//                city.setId(record.getInt("CityId"));
//                city.setName(record.getString("Name"));
//       //         cities.add(city);
//            }
//
//            return city.getName();
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//
//    }
//
//    public Customer getCustomerInfo(String cardNumber) {
//        try {
//            
//            
//            // This is for Foreign Customer    
//            String strQuery = "select PP.*,proto.ProtocolName, ppt.PaymentProcessorType from cardprefixes CP\n"
//                    + "INNER JOIN PaymentProcessors PP on CP.BankId = PP.BankId\n"
//                    + "INNER JOIN protocols proto on PP.ProtocolId = proto.ProtocolId\n"
//                    + "INNER JOIN paymentprocessortypes ppt on PP.PaymentProcessorTypeId = ppt.PaymentProcessorTypeId\n"
//                    + "\n"
//                    + "where '" + cardNumber + "' like Concat(CP.CardPrefix,'%') AND PP.IsActive =TRUE and proto.IsActive =TRUE ORDER BY CP.CardPrefix DESC LIMIT 1";
//            //logger.info("CMD: "+strQuery);
//            DbResult rs = dm.getRecord(strQuery);
//
//            PaymentProcessor paymentProcessorInfo = null;
//
//            Customer customer = new Customer();
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                logger.info(record.toString());
//                
//                paymentProcessorInfo = new PaymentProcessor();
//
//                customer.setIsLocalCustomer(false);
//                paymentProcessorInfo.setPaymentProcessorId(record.getInt("PaymentProcessorId"));
//                paymentProcessorInfo.setSystemURL(record.getString("SystemURL"));
//                paymentProcessorInfo.setIsLocalProcessor(false);
//                paymentProcessorInfo.setPaymentProcessorType(record.getString("PaymentProcessorType"));
//                paymentProcessorInfo.setPaymentProcessorTypeId(record.getInt("PaymentProcessorTypeId"));                
//                paymentProcessorInfo.setIsActive(record.getBoolean("isActive"));                
//                paymentProcessorInfo.setProtocol(record.getString("ProtocolName"));
//                paymentProcessorInfo.setUserName(record.getString("SystemUsername"));
//                paymentProcessorInfo.setPassword(record.getString("SystemPassword"));
//                paymentProcessorInfo.setSystemSecret(record.getString("SystemSecret"));
//                paymentProcessorInfo.setMyIP(record.getString("MyIP"));
//                paymentProcessorInfo.setMyPartnerId(record.getString("MyPartnerId"));
//                paymentProcessorInfo.setName(record.getString("Name"));
//                paymentProcessorInfo.setDescription(record.getString("Description"));
//
//                customer.setPaymentProcessorInfo(paymentProcessorInfo);
//            }
//            
//            return customer;
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//    
//    public double getTotalAmount(int cashierId) {
//        
//        double totalAmount = 0;
//         try {
//            String query = "SELECT SUM(AMOUNT) AS TodayBalance FROM transactions where (CashierId = " + cashierId + " AND TransactionTime = CURDATE()) AND TransactionId =3";
//
//            DbResult rs = dm.getRecord(query);
//            Transaction transaction = new Transaction();
//
//            if (rs.hasNext()) {
//                DbRecord record = rs.next();
//                totalAmount = record.getDouble("TodayBalance");               
//            }
//
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//
//        }
//         
//        return totalAmount;
//    }
//    
//    public List<Distributor> getDistributors() {
//        try {
//            List<Distributor> distributors = new ArrayList<>();
//            String query = "Select * from Distributors";
//
//            
//            DbResult rs = dm.getRecord(query);
//
//             while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                Distributor distributor = new Distributor();
//                distributor.setDistributorId(record.getInt("DistributorId"));
//                distributor.setName(record.getString("Name"));
//                distributor.setEmail(record.getString("Email"));
//                distributor.setPhone(record.getString("Phone"));
//                distributor.setUsername(record.getString("Username"));
//                distributor.setPassword(record.getString("Password"));
//                distributor.setAddress(record.getString("Address"));
//                distributor.setCityId(record.getInt("CityId"));
//                distributor.setCountryId(record.getInt("CountryId"));
//                distributor.setStateId(record.getInt("StateId"));
//                distributor.setIsActive(record.getBoolean("IsActive"));
//                distributors.add(distributor);
//            }      
//            
//             return distributors;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public int createDistributor(String name, String email, String phone, String username, String password, String address, int cityId, int stateId, int countryId, boolean isActive) {
//
//        try {
//            String query = "INSERT INTO Distributors VALUES (null,'" + name + "','" + email + "','" + phone
//                    + "','" + username + "','" + password + "','" + address + "'," + cityId + "," + stateId + "," + countryId
//                    + "," + isActive + ")";
//
//            
//            int distributorId = dm.AddRecord(query);
//            return distributorId;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//
//    }
//    
//    public boolean updateDistributor(int distributorId, String name, String email, String phone,
//             String IsActive, String username, String password, String Address, String CountryId,
//             String StateId, String CityId) {
//          try {
//            String strQuery = "UPDATE Distributors "
//                    + " SET Name= " +"'" +name+"'"
//                    + " , Email=" + "'"+email+"'"
//                    + " , Phone=" + "'"+phone+"'"
//                    + " , Username=" + "'"+ username +"'"
//                    + " , Password=" +"'"+ password+"'"
//                    + " , Address=" +"'"+ Address+ "'"
//                    + " , CityId=" + CityId
//                    + " , StateId=" + StateId
//                    + " , CountryId=" + CountryId
//                    + " , IsActive=" + IsActive 
//                    + " WHERE DistributorId=" + distributorId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//    
//    public int createMerchant(String name, String merchantUID, String email, String phone, String username, String password,
//            int chargingPlanId, int merchantAccountId, int bankId, String settlementAccountId, int settlementMethodId, int settlementModeId,
//            int settlementCycleId, int distributorId, String address, int cityId, int stateId, int countryId, boolean isActive) {
//
//        
//
//        try {
//            String APIKey = (new CommMod()).generateMerchantApiKey(merchantUID); 
//            String query = "INSERT INTO Merchants VALUES (null,'" + name + "','" + merchantUID + "','" + email + "','" + phone
//                    + "','" + username + "','" + password + "'," + chargingPlanId + "," + bankId
//                    + "," + settlementAccountId + "," + settlementMethodId + "," + settlementModeId
//                    + "," + settlementCycleId + "," + distributorId + ",'" + address + "'," + cityId + "," + stateId + "," + countryId
//                    + "," + isActive + ",'" + APIKey + "')";
//
//            
//            int merchantId = dm.AddRecord(query);
//            return merchantId;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//
//    }
//
//    public List<Merchant> getMerchants() throws Exception {
//        try {
//            List<Merchant> merchants = new ArrayList<>();
//            String query = "Select * from Merchants";
//
//            
//            DbResult rs = dm.getRecord(query);
//
//           while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                Merchant merchant = new Merchant();
//                merchant.setMerchantId(record.getInt("MerchantId"));
//                merchant.setName(record.getString("Name"));
//                merchant.setMerchantUID(record.getString("UID"));
//                merchant.setEmail(record.getString("Email"));
//                merchant.setPhone(record.getString("Phone"));
//                merchant.setUsername(record.getString("Username"));
//                merchant.setPassword(record.getString("Password"));
//                merchant.setSettlementAccountId(record.getString("SettlementAccountId"));
//                merchant.setChargingPlanId(record.getInt("ChargingPlanId"));
//                merchant.setSettlementModeId(record.getInt("SettlementModeId"));
//                merchant.setSettlementCycleId(record.getInt("SettlementCycleId"));
//                merchant.setSettlementMethodId(record.getInt("SettlementMethodId"));
//                merchant.setCityId(record.getInt("CityId"));
//                merchant.setAddress(record.getString("Address"));
//                merchant.setStateId(record.getInt("StateId"));
//                merchant.setCountryId(record.getInt("CountryId"));
//                merchant.setIsActive(record.getBoolean("IsActive"));
//                merchant.setAPIKey(record.getString("APIKey"));
//                merchant.setDistributorId(record.getInt("DistributorId"));
//                merchants.add(merchant);
//            }
//            
//            return merchants;
//        } catch (Exception exp) {
//            throw exp;
//        }
//    }
//
//    public List<Merchant> getMerchants(int distributorId) throws Exception {
//        try {
//            List<Merchant> merchants = new ArrayList<>();
//            String query = "Select * from Merchants where DistributorId = " + distributorId;
//
//            
//            DbResult rs = dm.getRecord(query);
//
//           while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                Merchant merchant = new Merchant();
//                merchant.setMerchantId(record.getInt("MerchantId"));
//                merchant.setName(record.getString("Name"));
//                merchant.setMerchantUID(record.getString("UID"));
//                merchant.setEmail(record.getString("Email"));
//                merchant.setPhone(record.getString("Phone"));
//                merchant.setUsername(record.getString("Username"));
//                merchant.setPassword(record.getString("Password"));
//                merchant.setSettlementAccountId(record.getString("SettlementAccountId"));
//                merchant.setChargingPlanId(record.getInt("ChargingPlanId"));
//                merchant.setSettlementModeId(record.getInt("SettlementModeId"));
//                merchant.setSettlementCycleId(record.getInt("SettlementCycleId"));
//                merchant.setSettlementMethodId(record.getInt("SettlementMethodId"));
//                merchant.setCityId(record.getInt("CityId"));
//                merchant.setAddress(record.getString("Address"));
//                merchant.setStateId(record.getInt("StateId"));
//                merchant.setCountryId(record.getInt("CountryId"));
//                merchant.setIsActive(record.getBoolean("IsActive"));
//                merchant.setAPIKey(record.getString("APIKey"));
//                merchants.add(merchant);
//            }
//
//          
//
//            return merchants;
//        } catch (Exception exp) {
//            throw exp;
//        }
//    }
//
//    public List<Device> getDevice(int merchantId) {
//        try {
//            List<Device> devices = new ArrayList<>();
//            String query = "Select * from posdevices where MerchantId=" + merchantId;
//
//            
//            DbResult rs = dm.getRecord(query);
//
//              while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                Device device = new Device();
//                device.setId(record.getInt("PoSDeviceId"));
//                //      device.setDeviveName(rs.getString("DeviceName"));
//                device.setModel(record.getString("Model"));
//                device.setSerialNumber(record.getString("SerialNo"));
//                device.setVendor(record.getString("VendorName"));
//                device.setIsActive(record.getBoolean("IsActive"));
//
//                devices.add(device);
//
//            }
//
//            return devices;
//        } catch (Exception exp) {
//            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//
////    public int createDevice() {
////        return 1;
////    }
//    public int createDevice(String serialNumber, String model, String vendor, int merchantId, int isActive) {
//        try {
//
//            String query = "INSERT INTO posdevices VALUES(null,'" + serialNumber + "','" + model + "','"
//                    + vendor + "'," + merchantId + "," + isActive + ")";
//            
//            int ret = dm.AddRecord(query);
//            return ret;
//        } catch (Exception exp) {
//            return -1;
//        }
//    }
//
//    public boolean updateDevice(int deviceId, String model, String serialNumber, String vendor, int merchantId, int isActive) {
//
//        try {
//            String strQuery = "UPDATE posdevices "
//                    + " SET SerialNo=" + "'" + serialNumber + "'"
//                    + " , Model=" + "'" + model + "'"
//                    + " , VendorName=" + "'" + vendor + "'"
//                    + " , MerchantId=" + "'" + merchantId + "'"
//                    + " , IsActive=" + "'" + isActive + "'"
//                    + " WHERE PoSDeviceId = " + deviceId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//
//    }
//
//    //to be implemented
//    public boolean updateMerchant(String name, String username, String password, String merchantId, String merUid,
//            String email, String phone, String chargePlanId, String accountId, String settAccId, String settmethId, String settModeId,
//            String settCycleId, String address, String cityId, String stateId, String countryId, String isActive, String APIKey) {
//        try {
//            String strQuery = "UPDATE Merchants "
//                    + " SET Name= " + "'" + name + "'"
//                    + " , UID=" + "'" + merUid + "'"
//                    + " , Email=" + "'" + email + "'"
//                    + " , Phone=" + "'" + phone + "'"
//                    + " , Username=" + "'" + username + "'"
//                    + " , Password=" + "'" + password + "'"
//                    + " , ChargingPlanId=" + "'" + chargePlanId + "'"
//                    + " , BankId=" + "'" + accountId + "'"
//                    + " , SettlementAccountId=" + "'" + settAccId + "'"
//                    + " , SettlementMethodId=" + "'" + settmethId + "'"
//                    + " , SettlementModeId=" + "'" + settModeId + "'"
//                    + " , SettlementCycleId=" + "'" + settCycleId + "'"
//                    + " , Address=" + "'" + address + "'"
//                    + " , CityId=" + cityId
//                    + " , StateId=" + stateId
//                    + " , CountryId=" + countryId
//                    + " , IsActive=" + isActive
//                    + " , APIKey=" + "'" + APIKey + "'"
//                    + " WHERE MerchantId = " + merchantId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//
//    public Merchant getMerchant(int merchantId) {
//
//        try {
//            String strMerchantInfoQuery = "SELECT * FROM Merchants WHERE MerchantId=" + merchantId;
//            DbResult rs = dm.getRecord(strMerchantInfoQuery);
//
//            if (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                Merchant merchant = new Merchant();
//                merchant.setMerchantId(record.getInt("MerchantId"));
//                merchant.setName(record.getString("Name"));
//                merchant.setMerchantUID(record.getString("UID"));
//                merchant.setEmail(record.getString("Email"));
//                merchant.setPhone(record.getString("Phone"));
//                merchant.setUsername(record.getString("Username"));
//                merchant.setPassword(record.getString("Password"));
//                merchant.setSettlementAccountId(record.getString("SettlementAccountId"));
//                merchant.setChargingPlanId(record.getInt("ChargingPlanId"));
//                merchant.setSettlementModeId(record.getInt("SettlementModeId"));
//                merchant.setSettlementCycleId(record.getInt("SettlementCycleId"));
//                merchant.setCityId(record.getInt("CityId"));
//                merchant.setAddress(record.getString("Address"));
//                merchant.setStateId(record.getInt("StateId"));
//                merchant.setCountryId(record.getInt("CountryId"));
//                merchant.setIsActive(record.getBoolean("IsActive"));
//                merchant.setAPIKey(record.getString("APIKey"));
//                merchant.setBankId(record.getInt("bankId"));
//
//                
//                return merchant;
//            } else {
//                return null;
//            }
//
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//
//    }
//
//    public Merchant getMerchantByUID(String merchantUId) {
//
//        try {
//            String strMerchantInfoQuery = "SELECT * FROM Merchants WHERE UID='" + merchantUId+"'";
//            DbResult rs = dm.getRecord(strMerchantInfoQuery);
//            Merchant merchant = null;
//           if (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                merchant = new Merchant();
//                merchant.setMerchantId(record.getInt("MerchantId"));
//                //merchant.setName(record.getString("Name"));
//                //merchant.setEmail(record.getString("Email"));
//                //merchant.setPhone(record.getString("Phone"));
//                //merchant.setUsername(record.getString("Username"));
//                //merchant.setPassword(record.getString("Password"));
//                merchant.setSettlementAccountId(record.getString("SettlementAccountId"));
//                merchant.setChargingPlanId(record.getInt("ChargingPlanId"));
//                merchant.setSettlementModeId(record.getInt("SettlementModeId"));
//                merchant.setSettlementCycleId(record.getInt("SettlementCycleId"));
//                //merchant.setCityId(record.getInt("CityId"));
//                //merchant.setAddress(record.getString("Address"));
//                //merchant.setStateId(record.getInt("StateId"));
//                //merchant.setCountryId(record.getInt("CountryId"));
//                //merchant.setIsActive(record.getBoolean("IsActive"));
//                merchant.setAPIKey(record.getString("APIKey"));
//                //merchant.setBankId(record.getInt("bankId"));
//            }
//          
//
//            return merchant;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//
//    }
//
//    public int creditMerchantsPayableAccount(int merchantId, double creditAmount, int transactionId) {
//
//        try {
//
//            int GLAccountId = 1; // TODO make it dynamic from Configuration , current GLAccountId =1 should always exist in GLAccounts table as Payable Account
//            String currentTime = new Timestamp(System.currentTimeMillis()).toString();
//            String query = "INSERT INTO GLTransactions VALUES (null,'" + currentTime + "'," + GLAccountId + "," + creditAmount + "," + 0 + "," + merchantId + "," + transactionId + ")";
//            int key = dm.AddRecord(query);
//            return key;
//
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//
//    }
//
//    public int debitMerchantsPayableAccount(int merchantId, double debitAmount, int transactionId) {
//
//        try {
//            int GLAccountId = 1; // TODO make it dynamic from Configuration , current GLAccountId =1 should always exist in GLAccounts table as Payable Account
//            String currentTime = new Timestamp(System.currentTimeMillis()).toString();
//            String query = "INSERT INTO GLTransactions VALUES (null,'" + currentTime + "'," + GLAccountId + ",0"+ "," + debitAmount + "," + merchantId + "," + transactionId + ")";
//            int key = dm.AddRecord(query);
//            return key;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//
//    }
//
//    public PaymentProcessor getMerchantPaymentProcessor(int bankId) {
//        try {
//            String strQuery = "select distinct PP.*,proto.ProtocolName, ppt.PaymentProcessorType from Merchants M\n"
//                    + "INNER JOIN PaymentProcessors PP on M.BankId = PP.BankId\n"
//                    + "INNER JOIN protocols proto on PP.ProtocolId = proto.ProtocolId\n"
//                    + "INNER JOIN paymentprocessortypes ppt on PP.PaymentProcessorTypeId = ppt.PaymentProcessorTypeId\n"
//                    + "\n"
//                    + "where M.bankId = " + bankId;
//            
//            DbResult rs = dm.getRecord(strQuery);
//
//            PaymentProcessor paymentProcessorInfo = null;
//
//             while (rs.hasNext()) {
//                DbRecord record = rs.next();                
//
//                paymentProcessorInfo = new PaymentProcessor();
//
//                paymentProcessorInfo.setPaymentProcessorId(record.getInt("PaymentProcessorId"));
//                paymentProcessorInfo.setSystemURL(record.getString("SystemURL"));
//                paymentProcessorInfo.setIsLocalProcessor(false);
//                paymentProcessorInfo.setPaymentProcessorType(record.getString("PaymentProcessorType"));
//                paymentProcessorInfo.setPaymentProcessorTypeId(record.getInt("PaymentProcessorTypeId"));
//                paymentProcessorInfo.setIsActive(record.getBoolean("isActive"));
//                paymentProcessorInfo.setProtocol(record.getString("ProtocolName"));
//                paymentProcessorInfo.setUserName(record.getString("SystemUsername"));
//                paymentProcessorInfo.setPassword(record.getString("SystemPassword"));
//                paymentProcessorInfo.setSystemSecret(record.getString("SystemSecret"));
//                paymentProcessorInfo.setMyIP(record.getString("MyIP"));
//                paymentProcessorInfo.setMyPartnerId(record.getString("MyPartnerId"));
//                paymentProcessorInfo.setName(record.getString("Name"));
//                paymentProcessorInfo.setDescription(record.getString("Description"));
//
//
//            }
//           
//            return paymentProcessorInfo;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//    
//     public int createPaymentProvider(String name, String email, String phone, String address, int cityId, int stateId, int countryId, boolean isActive) {
//
//        try {
//
//            String queryInsert = "INSERT INTO Issuers VALUES(null,'" + name + "','" + email + "','" + phone + "','" + address
//                    + "'," + cityId + "," + stateId + "," + countryId +","+ isActive + ")";
//            
//            int providerId = dm.AddRecord(queryInsert);
//
//            return providerId;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//
//    }
//
//    public List<PaymentProvider> getPaymentProviders() {
//        ArrayList<PaymentProvider> providers = new ArrayList<>();
//        try {
//            String strQuery = //"select * from issuers";
//                    "SELECT IssuerId,Name,Email,Phone,Address,CityId,StateId,CountryId,IsActive,\n"
//                    + "cardprefixes.CardPrefix\n"
//                    + " FROM issuers\n"
//                    + " inner join cardprefixes\n"
//                    + " on cardprefixes.BankId = issuers.IssuerId;";
//            DbResult rs = dm.getRecord(strQuery);
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                PaymentProvider paymentProvider = new PaymentProvider();
//                paymentProvider.setId(record.getInt("IssuerId"));
//                paymentProvider.setName(record.getString("name"));
//                paymentProvider.setEmail(record.getString("email"));
//                paymentProvider.setPhone(record.getString("phone"));
//                paymentProvider.setAddress(record.getString("address"));
//                paymentProvider.setCityId(record.getInt("cityId"));
//                paymentProvider.setStateId(record.getInt("stateId"));
//                paymentProvider.setCountryId(record.getInt("countryId"));
//                paymentProvider.setIsActive(record.getBoolean("IsActive"));
//                paymentProvider.setCardprefix(record.getString("CardPrefix"));
//                
//                providers.add(paymentProvider);
//
//            }
//            
//            return providers;
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//     public List<PaymentProcessor> getPaymentProcessors(int bankId) {
//        try {
//
//            String strQuery = "SELECT * FROM paymentprocessors" 
//                    + " where BankId = " + bankId;
//            DbResult rs = dm.getRecord(strQuery);
//            ArrayList<PaymentProcessor> processors = new ArrayList<>();
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                PaymentProcessor processor = new PaymentProcessor();
//                processor.setPaymentProcessorId(record.getInt("PaymentProcessorId"));
//                processor.setSystemURL(record.getString("systemURL"));
//                processor.setProtocolId(record.getInt("protocolId"));
//           //     processor.setProtocol(record.getString("ProtocolName"));
//                processor.setIsActive(record.getBoolean("isActive"));
//                processor.setUserName(record.getString("SystemUsername"));
//                processor.setPassword(record.getString("SystemPassword"));
//                processor.setSystemSecret(record.getString("SystemSecret"));
//              
//                //processor.setIsLocalProcessor(false);
//                processor.setPaymentProcessorTypeId(record.getInt("PaymentProcessorTypeId"));
//              //  processor.setPaymentProcessorType(record.getString("PaymentProcessorType"));
//                processor.setPaymentProcessorId(record.getInt("PaymentProcessorId"));
//                processor.setMyIP(record.getString("MyIP"));
//                processor.setMyPartnerId(record.getString("MyPartnerId"));
//                processor.setBankId(record.getInt("BankId"));
//                processor.setName(record.getString("Name"));
//                processor.setDescription(record.getString("Description"));
//
//                processors.add(processor);
//            }
//            
//            return processors;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public PaymentProcessor getPaymentProcessorInfo(int paymentProcessorId) {
//        try {
//
//            String strQuery = "Select PP.*,PPT.* ,proto.* from PaymentProcessors PP "
//                    + " INNER JOIN PaymentProcessorTypes PPT ON PP.PaymentProcessorTypeId=PPT.PaymentProcessorTypeId "
//                    + " INNER JOIN Protocols proto ON proto.ProtocolId=PP.ProtocolId "
//                    + "where PP.PaymentProcessorId = " + paymentProcessorId;
//            DbResult rs = dm.getRecord(strQuery);
//            PaymentProcessor processor = new PaymentProcessor();
//             while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                processor.setSystemURL(record.getString("systemURL"));
//                processor.setProtocolId(record.getInt("protocolId"));
//                processor.setProtocol(record.getString("ProtocolName"));
//                processor.setIsActive(record.getBoolean("isActive"));
//                processor.setUserName(record.getString("SystemUsername"));
//                processor.setPassword(record.getString("SystemPassword"));
//                processor.setSystemSecret(record.getString("SystemSecret"));
//                processor.setIsLocalProcessor(false);
//                processor.setPaymentProcessorTypeId(record.getInt("PaymentProcessorTypeId"));
//                processor.setPaymentProcessorType(record.getString("PaymentProcessorType"));
//                processor.setPaymentProcessorId(record.getInt("PaymentProcessorId"));
//                processor.setMyIP(record.getString("MyIP"));
//                processor.setBankId(record.getInt("BankId"));
//                processor.setMyPartnerId(record.getString("MyPartnerId"));
//                processor.setName(record.getString("Name"));
//                processor.setDescription(record.getString("Description"));
//
//            }
//            
//            return processor;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public int createProcessor(int paymentProcessorTypeId, String systemURL, String systemSecret, int protocolId, String userName, String password, String isActive,String bankId , String myIP, String myPartnerId) {
//        try {
//
//            String queryProcessorInsert = "INSERT INTO PaymentProcessors VALUES(null,'', '', " + paymentProcessorTypeId + "','"
//                    + systemURL + "','" + systemSecret + "'," + protocolId + ",'" + userName + "','"
//                    + password + "'," + isActive +",'" + bankId + "','" + myIP + "','" + myPartnerId + "')";
//            
//
//            int processorID = dm.AddRecord(queryProcessorInsert);
//
//            return processorID;
//
//        } catch (Exception exp) {
//            return -1;
//        }
//    }
//    public int updateProcessor(int paymentProcessorId,int paymentProcessorTypeId, String systemURL, String systemSecret, int protocolId, String userName, String password, int isActive,String bankId , String myIP, String myPartnerId) {
//        try {
//
//            String queryProcessorInsert = "UPDATE PaymentProcessors "
//                    + "SET PaymentProcessorTypeId=" +"'"+ paymentProcessorTypeId+"'" 
//                    + ", SystemURL=" + "'"+systemURL+"'" 
//                    + ", SystemSecret=" +"'"+ systemSecret +"'"
//                    + ", ProtocolId=" + "'"+protocolId +"'"
//                    + ", SystemUsername=" + "'"+userName +"'"
//                    + ", SystemPassword=" +"'"+ password  +"'"
//                    + ", IsActive=" +"'"+ isActive +"'"
//                    + ", BankId=" +"'"+ bankId +"'"
//                    + ", MyIP=" +"'"+ myIP +"'"
//                    + ", MyPartnerId=" +"'"+ myPartnerId +"'"
//                    + " WHERE PaymentProcessorId="+paymentProcessorId;
//            //   	 	 	 	 	
//            
//
//            int processorID = dm.AddRecord(queryProcessorInsert);
//
//            return processorID;
//
//        } catch (Exception exp) {
//            return -1;
//        }
//    }
//    public int updatePaymentProvider(int issuerId, String name, String email, String phone, String address, int cityId, int stateId, int countryId, int isActive) {
//
//        try {
//
//            String query = "UPDATE Issuers"
//                    + " SET Name=" +"'"+ name +"'"
//                    + ", Email=" +"'"+ email +"'"
//                    + ", Phone=" +"'"+ phone +"'"
//                    + ", Address=" +"'"+ address +"'"
//                    + ", CityId=" +"'"+ cityId +"'"
//                    + ", StateId=" +"'"+ stateId +"'"
//                    + ", CountryId=" +"'"+ countryId +"'"
//                    + ", IsActive="+"'"+ isActive +"'"
//                    + "WHERE IssuerId="+issuerId;
//            //  	 	 	 	 	 	 	 	
//            
//            int providerId = dm.AddRecord(query);
//
//            return providerId;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//
//    }
//     public List<PaymentProcessorTypes> getPPTypes() {
//            try {
//            List<PaymentProcessorTypes> types = new ArrayList<>();
//            String query = "Select * from paymentprocessortypes";
//
//            
//            DbResult rs = dm.getRecord(query);
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                PaymentProcessorTypes type = new PaymentProcessorTypes();
//                type.setId(record.getInt("PaymentProcessorTypeId"));
//                type.setType(record.getString("PaymentProcessorType"));    
//                types.add(type);
//                
//            }
//            
//            
//            return types;
//        } catch (Exception exp) {
//            Logger.getLogger(PaymentProcessorTypes.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//     public List<PaymentProtocols> getPrtocol() {
//            try {
//            List<PaymentProtocols> types = new ArrayList<>();
//            String query = "Select * from protocols";
//
//            
//            DbResult rs = dm.getRecord(query);
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                PaymentProtocols type = new PaymentProtocols();
//                type.setId(record.getInt("ProtocolId"));
//                type.setName(record.getString("ProtocolName"));
//                type.setIsActive(record.getBoolean("IsActive"));
//                    
//                types.add(type);
//                
//            }
//            
//            return types;
//        } catch (Exception exp) {
//            Logger.getLogger(PaymentProcessorTypes.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//     
//     public int createSettlement(int merchantId, double amount, int statusId, int transactionId, String issuerSettlementId, String description) {
//
//        try {
//            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//            String strQuery = "INSERT INTO Settlements Values(null,'" + currentTime.toString() + "'," + merchantId
//                    + "," + amount + "," + statusId + "," + transactionId + ",'" + issuerSettlementId + "','" + description + "')";
//
//            return dm.AddRecord(strQuery);
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//    }
//
//    public int createSettTrans(int settlementId, List<String> transId) throws Exception {
//
//        try {
//            //    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//            String strQuery = "INSERT INTO `settlementtransactions` (`id`,`SettlementId`, `TransactionId`) VALUES";
//            for (int i = 0; i < transId.size(); i++) {
//                strQuery += "(NULL," + settlementId + "," + transId.get(i) + ")";
//                if (i < transId.size() - 1)//not the end of the array
//                {
//                    strQuery += ",";
//                }
//            }
//
//            return dm.AddRecord(strQuery);
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            throw ex;
//        }
//    }
//
//    public boolean updateStatusAndIssuerId(int settlementId, int statusId, String issuerSettlementId) {
//        try {
//            String strQuery = "UPDATE SETTLEMENTS "
//                    + " Set SettlementStatusId=" + statusId
//                    + ", issuerSettlementId='" + issuerSettlementId + "'"
//                    + " WHERE SettlementId = " + settlementId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//
//    public boolean updateStatus(int statusId, int settlementId) {
//        try {
//            String strQuery = "UPDATE SETTLEMENTS "
//                    + " Set SettlementStatusId=" + statusId
//                    + " WHERE SettlementId = " + settlementId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//
//    public boolean update(int settlementId, String merchantId, String transactionTime,
//            String amount, String settlementStatusId, String description) {
//        // 	 	 	 	 	 	 	
//        try {
//            String strQuery = "UPDATE Settlements "
//                    + " Set SettlementTime=" + "'" + transactionTime + "'"
//                    + ", MerchantId=" + "'" + merchantId + "'"
//                    + ", Amount=" + "'" + amount + "'"
//                    + ", SettlementStatusId=" + "'" + settlementStatusId + "'"
//                    + ", Description=" + "'" + description + "'"
//                    + " WHERE SettlementId = " + settlementId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//
//    public _TransactionResponse resolvePendingOnlineSettlment(int settlementId) {
//        Settlement settlement = null;
//        
//        try {
//            String strQuery = "SELECT * FROM SETTLEMENTS where SettlementId= " + settlementId;
//            DbResult rs = dm.getRecord(strQuery);
//                 while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                    settlement = new Settlement();
//                    settlement.setSettlementId(record.getInt("settlementId"));
//                    settlement.setTransactionTime(record.getTimestamp("SettlementTime"));
//                    settlement.setMerchantId(record.getInt("merchantId"));
//                    settlement.setAmount(record.getDouble("amount"));
//                    settlement.setSettlementStatusId(record.getInt("settlementStatusId"));
//                    settlement.setReferenceId(record.getInt("transactionId"));
//                    settlement.setIssuerSettlementId(record.getString("IssuerSettlementId"));
//                    settlement.setDescription(record.getString("description"));
//                    
//                }
//            
//            if(settlement ==null)
//                return null;
//            
//            
//            Merchant merchantInfo = getMerchant(settlement.getMerchantId());
//            PaymentProcessor merchantPaymentProcessor = getMerchantPaymentProcessor(merchantInfo.getBankId());
//            TransactionInfo merchantTransaction = new TransactionInfo();
//            merchantTransaction.setReferenceId(settlement.getSettlementId());
//            merchantTransaction.setGatewayTransactionId(settlement.getSettlementId());
//            merchantTransaction.setProviderTransactionId(settlement.getIssuerSettlementId());
//            merchantTransaction.setAmount(settlement.getAmount());
//            merchantTransaction.setDescription(settlement.getDescription());
//            merchantTransaction.setInvoiceId("resolve-settlement");
//            merchantTransaction.setCurrency("USD");
//            merchantTransaction.setMerchantName(merchantInfo.getName());
//          
//            
//            double creditAmount = settlement.getAmount();
//            String newRequestId = String.valueOf(Math.random());
//            PmtBrokerRequest sr2 = new PmtBrokerRequest();                           
//            sr2.setTransactionInfo(newRequestId, merchantInfo.getName(), "resolve-settlement", creditAmount, "USD", settlement.getDescription(), "", "", ""+settlement.getSettlementId(), merchantInfo.getSettlementAccountId(), "", "");                    
//            //sr2.setVendorInfo(merchantPaymentProcessor, settlement.getIssuerSettlementId());
//            PaymentBroker paymentBroker = new PaymentBroker(sr2.paymentProcessorName);
//            PmtBrokerResponse settlmentResponse = paymentBroker.doMerchantCredit(sr2);
//            
//            if ("credited".compareToIgnoreCase(settlmentResponse.state) == 0 || "Cannot Create Transfer Duplicate Sender Transfer Id".compareToIgnoreCase(settlmentResponse.description) == 0) {
//                //Update Local Transaction Status to Approved.
//                updateSettlementStatus(settlement.getReferenceId(), SettlementStatus.Credited);
//                //Update the Settlement Transaction to Credited
//                this.updateStatusAndIssuerId(settlementId, SettlementStatus.Credited, settlmentResponse.pmtProcessorTransactionId);
//                settlmentResponse.state ="credited";
//                //return settlmentResponse;
//            } //If Merchant Bank was unable to process the Credit then createSettlement a local record
//            else{
//                //return settlmentResponse;
//            }
//            return null;
//
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public boolean updateSettTrans(int settlementId, List<String> transId) {
//        // 	 	 	 	 	 	 	
//        try {
//            String strQuery = "";
//            
//            for (int i = 0; i < transId.size(); i++) {
//                strQuery = "UPDATE settlementtransactions "
//                        + " Set TransactionId=" + "'" + transId.get(i) + "'"
//                        + " WHERE SettlementId = " + settlementId;
//
//                dm.AddRecord(strQuery);
//            }
//            //         boolean ret = dm.UpdateRecord(strQuery);
//            return true;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//
//    public Settlement getSettmentByTransactionId(int transactionId) {
//
//        Settlement settlement = new Settlement();
//        try {
//            String strQuery = "SELECT * FROM SETTLEMENTS where transactionId= " + transactionId;
//            DbResult rs = dm.getRecord(strQuery);
//
//            while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//
//                settlement.setSettlementId(record.getInt("settlementId"));
//                settlement.setTransactionTime(record.getTimestamp("SettlementTime"));
//                settlement.setMerchantId(record.getInt("merchantId"));
//                settlement.setAmount(record.getDouble("amount"));
//                settlement.setSettlementStatusId(record.getInt("settlementStatusId"));
//                settlement.setReferenceId(record.getInt("transactionId"));
//                settlement.setIssuerSettlementId(record.getString("IssuerSettlementId"));
//                settlement.setDescription(record.getString("description"));
//
//            }
//           
//            return settlement;
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public List<Settlement> getAllSettlements() {
//        ArrayList<Settlement> allSettlements = new ArrayList<>();
//        try {
//            String strQuery
//                    = //"select * from Settlements";
//                    "SELECT SettlementId, SettlementTime, settlements.MerchantId, Amount,TransactionId, Description,\n"
//                    + "settlements.SettlementStatusId,\n"
//                    + "settlementstatus.SettlementStatus,\n"
//                    + "merchants.Name\n"
//                    + "FROM paymentgateway.settlements\n"
//                    + "inner join settlementstatus\n"
//                    + "on settlements.SettlementStatusId= settlementstatus.SettlementStatusId\n"
//                    + "inner join merchants\n"
//                    + "on settlements.MerchantId = merchants.MerchantId";
//            DbResult rs = dm.getRecord(strQuery);
//
//          while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                Settlement settlement = new Settlement();
//                settlement.setSettlementId(record.getInt("settlementId"));
//                settlement.setMerchantId(record.getInt("merchantId"));
//                settlement.setReferenceId(record.getInt("transactionId"));
//                settlement.setMerchantName(record.getString("Name"));
//                settlement.setAmount(record.getDouble("amount"));
//                settlement.setSettlementStatusId(record.getInt("settlementStatusId"));
//                settlement.setSettlementStatus(record.getString("SettlementStatus"));
//                settlement.setDescription(record.getString("description"));
//                settlement.setTransactionTime(record.getTimestamp("SettlementTime"));
//                allSettlements.add(settlement);
//            }
//           
//            return allSettlements;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public List<SettlementCycles> getSettlementCycles() {
//        try {
//            List<SettlementCycles> cycles = new ArrayList<>();
//            String query = "Select * from settlementcycles";
//
//            
//            DbResult rs = dm.getRecord(query);
//
//           while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                SettlementCycles cycle = new SettlementCycles();
//                cycle.setId(record.getInt("SettlementCycleId"));
//                cycle.setSettlementCycle(record.getString("SettlementCycle"));
//
//                cycles.add(cycle);
//
//            }
//
//            
//
//            return cycles;
//        } catch (Exception exp) {
//            Logger.getLogger(SettlementCycles.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//
//    public List<Integer> getSetTransIds(int SettlementId) {
//        try {
//            List<Integer> tIds = new ArrayList<>();
//            String query = "Select * from settlementtransactions where SettlementId=" + SettlementId;
//
//            
//            DbResult rs = dm.getRecord(query);
//
//            while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                int tId;
//                tId = record.getInt("TransactionId");
//                tIds.add(tId);
//
//            }
//
//          
//            return tIds;
//        } catch (Exception exp) {
//            Logger.getLogger(SettlementCycles.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//
//    public List<SettlementMethods> getSettlementMethods() {
//        try {
//            List<SettlementMethods> methods = new ArrayList<>();
//            String query = "Select * from settlementmethods";
//
//            
//            DbResult rs = dm.getRecord(query);
//
//           while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                SettlementMethods method = new SettlementMethods();
//                method.setId(record.getInt("SettlementMethodId"));
//                method.setSettlementMethod(record.getString("SettlementMethod"));
//                methods.add(method);
//
//            }
//
//            
//            return methods;
//        } catch (SQLException exp) {
//            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//
//    public List<SettlementModes> getSettlementModes() {
//        try {
//            List<SettlementModes> modes = new ArrayList<>();
//            String query = "Select * from settlementmodes";
//
//            
//            DbResult rs = dm.getRecord(query);
//
//           while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                SettlementModes mode = new SettlementModes();
//                mode.setId(record.getInt("SettlementModeId"));
//                mode.setName(record.getString("SettlementMode"));
//                modes.add(mode);
//
//            }
//
//           
//
//            return modes;
//        } catch (Exception exp) {
//            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//
//    public List<AccountTypes> getAccountTypes() {
//        try {
//            List<AccountTypes> types = new ArrayList<>();
//            String query = "Select * from accounttypes";
//
//            
//            DbResult rs = dm.getRecord(query);
//
//           while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                AccountTypes type = new AccountTypes();
//                type.setId(record.getInt("AccountTypeId"));
//                type.setName(record.getString("AccountType"));
//                types.add(type);
//
//            }
//
//          
//
//            return types;
//        } catch (Exception exp) {
//            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//
//    public List<Account> getAccountId() {
//        try {
//            List<Account> accounts = new ArrayList<>();
//            String query = "Select * from accounts";
//
//            
//            DbResult rs = dm.getRecord(query);
//
//          while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                Account acc = new Account();
//                acc.setAccountId(record.getInt("AccountId"));
//                acc.setAccountTitle(record.getString("AccountTitle"));
//                accounts.add(acc);
//
//            }
//
//
//            return accounts;
//        } catch (Exception exp) {
//            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, exp);
//            return null;
//        }
//    }
//    
//    public FxUser getUserInfo(String accessId, int channelId) {
//        FxUser user = null;
//        
//        String sp_name = "sp_getuserauthinfo";           
//        CallableStatement cs = null;
//        try {
//                         retCode = 0; Connection conn = getConnection();
//            cs = conn.prepareCall("{call " + sp_name + "(?, ?, ?)}");
//            cs.registerOutParameter("RETURNCODE", Types.NUMERIC);
//            cs.setInt("CHANNELID", channelId);
//            cs.setString("ACCESSID", accessId);
//            
//            cs.executeUpdate();
//            
//            ResultSet rset = null;
//          
//            rset = cs.getResultSet();
//
////                ResultSetMetaData rsmd = rs.getMetaData();
////		int numColumns = rsmd.getColumnCount();
////		logger.fine("ColumnCount: " + numColumns);
////		for (int i = 1; i <= numColumns; i++) {
////			logger.fine(rsmd.getColumnName(i));
////		}
//
//            logger.info("extractData resultset");
//            DbResult rs = new DbResult(rset);
//            if (rs.hasNext()) {
//                logger.info("Record available");
//                DbRecord record = rs.next();
//                user = new FxUser(); 
//                user.loadData(record);
//            }
//       cs.closeOnCompletion();     
//        } catch (Exception e) {
//            logger.severe("db_call_error: "+e.getMessage());
//        }            
//       
//        
//        return user;
//    }
//    
//    public Store getStoreInfo(String storedUID) {
//        try {
//            String strMerchantInfoQuery = "SELECT * FROM Stores WHERE UID='" + storedUID + "'";
//            DbResult rs = dm.getRecord(strMerchantInfoQuery);
//            Store store = null;
//           if (rs.hasNext()) {
//               logger.info("found record");
//                DbRecord record = rs.next();     
//                store = new Store();
//                store.setMerchantId(record.getInt("MerchantId"));
//                store.setName(record.getString("Name"));
//                store.setUsername(record.getString("Username"));
//                store.setPassword(record.getString("Password"));
//                store.setStoreKey(record.getString("StoreKey"));
//                
//            }
//
//            return store;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//
//    }
//    
//    public List<Store> getStores(int merchantId) {
//        try {
//            String strMerchantInfoQuery = "SELECT * FROM Stores WHERE MerchantId='" + merchantId + "'";
//            DbResult rs = dm.getRecord(strMerchantInfoQuery);
//            List<Store> stores = new ArrayList<>();
//
//            while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                Store store = new Store();
//                store.setStoreId(record.getInt("StoreId"));
//                store.setName(record.getString("Name"));
//                store.setUsername(record.getString("Username"));
//                store.setPassword(record.getString("Password"));
//                store.setStoreKey(record.getString("StoreKey"));
//                store.setWebURL(record.getString("AuthorizedURL"));
//                store.setIsActive(record.getBoolean("IsActive"));
//                
//                stores.add(store);
//            }
//    
//
//            return stores;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//
//    }
//    
//    public int createStore(int merchantId,int uId, String name, String username, String password, String webUrl, int isActive) {
//        try {
//            //random number for store key
//            long storeKey = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
//            String query = "INSERT INTO stores VALUES(null,'" + name + "','" + uId + "','"
//                    + username + "','" + password +"',"+ merchantId + ",'" + webUrl+"',"  + isActive +","+storeKey+ ")";
//            
//            int ret = dm.AddRecord(query);
//            return ret;
//        } catch (Exception exp) {
//            return -1;
//        }
//    }
//       
//    public boolean updateStore(int storeId, int merchantId, String name, String username, String password, String webUrl, int isActive, String storeKey) {
//        
//         try {
//            String strQuery = "UPDATE stores "
//                    + " SET Name=" + "'"+ name+"'"
//                    + " , Username=" + "'"+username+"'"
//                    + " , Password=" + "'"+password+"'"
//                    + " , AuthorizedURL=" + "'"+webUrl+"'"
//                    + " , StoreKey=" + "'"+storeKey+"'"
//                    + " , MerchantId=" + "'"+ merchantId +"'"
//                    + " , IsActive=" +"'"+isActive +"'"      
//                    + " WHERE StoreId = " + storeId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//        
//    }
//    
//    public Transaction getTransactionInfo(int TransactionId) {
//        try {
//            String strQuery = "select t.*, p.PaymentMethod, c.PaymentChannel from transactions t \n"
//                    + "inner join paymentmethods p on t.PaymentMethodId=p.PaymentMethodId\n"
//                    + "inner join paymentchannels c on t.PaymentChannelId=c.PaymentChannelId "
//                    + " WHERE TransactionId = " + TransactionId;
//            DbResult rs = dm.getRecord(strQuery);
//
//            boolean hasRecord = false;
//
//            Transaction transaction = new Transaction();
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                transaction.setReferenceId(record.getInt("TransactionId"));
//                transaction.setIssuerTransactionId(record.getString("IssuerTransactionId"));
//                transaction.setTransactionTime(record.getTimestamp("TransactionTime"));
//                transaction.setPaymentProcessorId(record.getInt("PaymentProcessorId"));
//                transaction.setAmount(record.getDouble("Amount"));
//                transaction.setCurrency(record.getString("currency"));
//                transaction.setTransactionCharges(record.getDouble("TransactionCharges"));
//                transaction.setDescription(record.getString("Description"));
//                transaction.setInvoiceId(record.getString("InvoiceId"));
//                transaction.setTransactionStatusId(record.getInt("TransactionStatusId"));
//                transaction.setMerchantId(record.getInt("MerchantId"));
//                if(record.get("CashierId")==null)
//                    transaction.setCashierId(0);
//                else
//                    transaction.setCashierId(record.getInt("CashierId"));
//                transaction.setPaymentMethodId(record.getInt("PaymentMethodId"));
//                transaction.setPaymentMethod(record.getString("PaymentMethod"));
//                transaction.setPaymentChannelId(record.getInt("PaymentChannelId"));
//                transaction.setPaymentChannel(record.getString("PaymentChannel"));
//                transaction.setPayerId(record.getString("PayerId"));
//                transaction.setPayerIdType(record.getInt("PayerIdType"));
//                transaction.setPoSDevicedId(record.getInt("PoSDevicedId"));
//                transaction.setOnlineStoreId(record.getInt("OnlineStoreId"));
//                transaction.setSettlementStatusId(record.getInt("SettlementStatusId"));
//                transaction.setUpdateTime(record.getTimestamp("updateTime"));
//                hasRecord = true;
//            }
//            
//            if (!hasRecord) {
//                return null;
//            } else {
//                return transaction;
//            }
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public int createTransaction(PaymentRequest paymentRequest, double transactionCharges, int merchantId, int paymentProcessorId, int paymentMethodId, int paymentChannelId) throws Exception {
//
//        try {
//            Date date = new Date();
//            Timestamp transactionTime = new Timestamp(date.getTime());
//            Timestamp updateTime = transactionTime;
//           
//            String payerId;
//            int payerIdType;
//            int PoSDevicedId;
//            int OnlineStoreId;
//
//            String description = paymentRequest.serviceParams.getTransactionInfo().getDescription();
//            String invoiceId = paymentRequest.serviceParams.getTransactionInfo().getInvoiceId();
//            String currency = paymentRequest.serviceParams.getTransactionInfo().getCurrency();
//            double amount = paymentRequest.serviceParams.getTransactionInfo().getAmount();
//
//            int statusId = TransactionStatus.PendingStatusId;
//            String cashierId = paymentRequest.merchantInfo.getCashierId();
//
////            String paymentMethod = paymentRequest.serviceParams.getPayerInfo().getPaymentMethod();
////            int paymentMethodId = 0;
////
////            if (null == paymentMethod) {
////                paymentMethodId = 5; // Undefined Method
////            } else {
////                switch (paymentMethod) {
////                    case "mwallet_account":
////                        paymentMethodId = 1;
////                        break;
////                    case "mwallet_card":
////                        paymentMethodId = 2; // Debit Card
////                        break;
////                    case "credit_card":
////                        paymentMethodId = 3; // Credit Card
////                        break;
////                    case "debit_card":
////                        paymentMethodId = 4; // SafariPay Local Account
////                        break;
////                    default:
////                        paymentMethodId = 5; // Undefined Method
////                        break;
////                }
////            }
//
////            String paymentType = paymentRequest.serviceParams.getPayerInfo().getPaymentType();
////
////            if (null == paymentType) {
////                paymentChannelId = 3;//POS
////            } else {
////                switch (paymentType) {
////                    case "NFC":
////                        paymentChannelId = 1;
////                        break;
////                    case "WEB":
////                        paymentChannelId = 2;
////                        break;
////                    default:
////                        paymentChannelId = 3;//POS
////                        break;
////                }
////            }
//// TODO:
//            payerId = paymentRequest.serviceParams.getPayerInfo().getMobileWallet().getSubscriptionId();
//            payerIdType = 1;
//
//            PoSDevicedId = 1; // Get DeviceId from Device Info
//            OnlineStoreId = 1;
//
//            String strQuery = "INSERT INTO Transactions Values(null," + null + "," + paymentProcessorId + ",'" + transactionTime + "'," + amount
//                    + "," + transactionCharges + "," + statusId + ",'" + description + "','" + invoiceId + "'," + merchantId + "," + cashierId + ","
//                    + paymentMethodId + "," + paymentChannelId + "," + PoSDevicedId + "," + OnlineStoreId + ",'" + payerId + "'," + payerIdType
//                    + ",'" + currency + "','" + updateTime + "',null,1)";
//
//            //Add getTransactionInfo Record in the Database
//            return dm.AddRecord(strQuery);
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            throw ex;
//        }
//    }
//    public int createTransaction1(int requestId, int OnlineStoreId, int PoSDevicedId, String payerId, int payerIdType, String cashierId, int merchantId, int paymentProcessorId, int paymentMethodId, int paymentChannelId, double amount, String currency, String invoiceId, String description, double transactionCharges) throws Exception {
//
//        try {
//            Date date = new Date();
//            Timestamp transactionTime = new Timestamp(date.getTime());
//            Timestamp updateTime = transactionTime;
//            
//            int statusId = TransactionStatus.PendingStatusId;            
//           
//            String strQuery = "INSERT INTO Transactions Values(null," + null + "," + paymentProcessorId + ",'" + transactionTime + "'," + amount
//                    + "," + transactionCharges + "," + statusId + ",'" + description + "','" + invoiceId + "'," + merchantId + "," + cashierId + ","
//                    + paymentMethodId + "," + paymentChannelId + "," + PoSDevicedId + "," + OnlineStoreId + ",'" + payerId + "'," + payerIdType
//                    + ",'" + currency + "','" + updateTime + "', null, 1, "+requestId+")";
//
//            //Add getTransactionInfo Record in the Database
//            return dm.AddRecord(strQuery);
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            throw ex;
//        }
//    }
//    
//     public double getTransactionCharges(int ChargingPlanId, int transactionTypeId, double txAmount) throws Exception {
//               
//        
//        double charges = 0;
//        
//        logger.log(Level.INFO, "Retrieving transaction charges for txamount: "+ txAmount);
//        logger.log(Level.INFO, "ChargingPlanId: "+ChargingPlanId);
//        logger.log(Level.INFO, "TransactionTypeId: "+transactionTypeId);
//        TransactionCharge txCharges = getTransactionCharge(ChargingPlanId, transactionTypeId);
//        charges = txCharges.getChargeValue();
//
//        if (txCharges.getChargeValueTypeId() == 2)//if Percentage
//        {
//            logger.log(Level.INFO, "Charging type: Percentage");
//            charges = (charges / 100);
//        }
//
//        return charges;
//    }
//    public boolean updateTranStatusAndIssuerId(int transactionId, int statusId, String issuerTransactionId) {
//
//        try {
//
//            String strQuery = "UPDATE Transactions "
//                    + " Set IssuerTransactionId='" + issuerTransactionId
//                    + "', TransactionStatusId=" + statusId
//                    + " WHERE TransactionId = " + transactionId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//
//    public boolean updateTransactionStatus(int transactionId, int statusId) {
//
//        try {
//            String strQuery = "UPDATE Transactions "
//                    + " Set TransactionStatusId=" + statusId
//                    + " WHERE TransactionId = " + transactionId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//
//    public void updateSettlementStatus(int transactionId, int settlementStatusId) {
//        try {
//            String strQuery = "UPDATE Transactions "
//                    + " Set SettlementStatusId=" + settlementStatusId
//                    + " WHERE TransactionId = " + transactionId;
//            boolean ret = dm.UpdateRecord(strQuery);
//
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//
//        }
//    }
//
//    public ArrayList<TransactionInfo> getTransactions(int noOfTransactions, int merchantUid, int cashierId) {
//        try {
//            ArrayList<TransactionInfo> transactions = new ArrayList<>();
//            //String strQuery = "select * from Transactions where CashierId= " + cashierId + " limit " + noOfTransactions;
//            String strQuery="select T.*, M.UID from Transactions T join Merchants M on M.MerchantId=T.MerchantId where M.UID="+merchantUid+" and CashierId="+cashierId+" limit "+noOfTransactions;
//            DbResult rs = dm.getRecord(strQuery);
//
//            while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                TransactionInfo tx = new TransactionInfo();
//                tx.setAmount(record.getDouble("Amount"));
//                tx.setCurrency(record.getString("currency"));
//                tx.setDescription(record.getString("Description"));
//                tx.setReferenceId(record.getInt("TransactionId"));
//                tx.setInvoiceId(record.getString("InvoiceId"));
//                transactions.add(tx);
//
//            }
//
//            return transactions;
//
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public ArrayList<Transaction> getAllTransactions() {
//
//        ArrayList<Transaction> transactions = new ArrayList<>();
//        try {
//            String strQuery
//                    = //"select * from Transactions";
//                    "SELECT TransactionId, Amount, TransactionTime, TransactionCharges, Currency,Description,\n"
//                    + " paymentprocessortypes.PaymentProcessorType, \n"
//                    + "transactionstatus.TransactionStatus\n"
//                    + " FROM transactions\n"
//                    + "inner join paymentprocessortypes\n"
//                    + "on transactions.PaymentProcessorId=paymentprocessortypes.PaymentProcessorTypeId\n"
//                    + "inner join transactionstatus\n"
//                    + "on transactions.TransactionStatusId= transactionstatus.TransactionStatusId";
//            DbResult rs = dm.getRecord(strQuery);
//
//          while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                Transaction transaction = new Transaction();
//                transaction.setReferenceId(record.getInt("TransactionId"));
//                transaction.setTransactionTime(record.getTimestamp("TransactionTime"));
////                transaction.setPaymentProcessorTransactionId(record.getInt("IssuingBankTransactionId"));
////              transaction.setPaymentProcessorId(record.getInt("PaymentProcessorId"));                     
//                transaction.setPaymentProcessortype(record.getString("PaymentProcessorType"));
//                transaction.setAmount(record.getDouble("Amount"));
//                transaction.setCurrency(record.getString("currency"));
//                transaction.setTransactionCharges(record.getDouble("TransactionCharges"));
//                transaction.setDescription(record.getString("Description"));
//                //        transaction.setInvoiceId(record.getString("InvoiceId"));
////                transaction.setTransactionStatusId(record.getInt("TransactionStatusId"));                                
//                transaction.setTransactionStatus(record.getString("TransactionStatus"));
//
//                //        transaction.setMerchantId(record.getInt("MerchantId"));
//                //        transaction.setCashierId(record.getInt("CashierId"));
//                //        transaction.setPaymentMethodId(record.getInt("PaymentMethodId"));
//                //        transaction.setPaymentChannelId(record.getInt("PaymentChannelId"));
//                //        transaction.setPayerId(record.getString("PayerId"));
//                //        transaction.setPayerIdType(record.getInt("PayerIdType"));
//                //        transaction.setPoSDevicedId(record.getInt("PoSDevicedId"));
//                //        transaction.setOnlineStoreId(record.getInt("OnlineStoreId"));
//                //       transaction.setIsMerchantSettlementDone(record.getBoolean("IsMerchantCredited"));
//                //       transaction.setUpdateTime(record.getTimestamp("updateTime"));
//                transactions.add(transaction);
//
//            }
//           
//            return transactions;
//
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public ArrayList<Settlement> getMerchantUnSettledTransactions(int merchantId) {
//
//        ArrayList<Settlement> settlemens = new ArrayList<>();
//        try {
//            String strQuery = "select * from Settlements where MerchantId=" + merchantId
//                    + " AND (`SettlementStatusId`= 1 OR `SettlementStatusId`= 5)";
//            DbResult rs = dm.getRecord(strQuery);
//
//           while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                Settlement settlement = new Settlement();
//                settlement.setSettlementId(record.getInt("SettlementId"));
//                settlement.setTransactionTime(record.getTimestamp("SettlementTime"));
//                settlement.setAmount(record.getDouble("Amount"));
//                settlement.setSettlementStatusId(record.getInt("SettlementStatusId"));
//                settlement.setReferenceId(record.getInt("TransactionId"));
//                settlement.setIssuerSettlementId(record.getString("IssuerSettlementId"));
//                settlemens.add(settlement);
//            }
//            
//            return settlemens;
//
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public PaymentProcessor getTransactionProcessor(int customerTransactionId) {
//        try {
//            String strPPIdQuery = "select PaymentProcessorId from Transactions where TransactionId=" + customerTransactionId;
//            DbResult rsPPId = dm.getRecord(strPPIdQuery);
//            if (!rsPPId.hasNext()) return null;
//            
//            DbRecord rspp = rsPPId.next();            
//            int ProcessorId = rspp.getInt("PaymentProcessorId");
//
//            String strQuery = "select distinct PP.*,proto.ProtocolName, ppt.PaymentProcessorType, ppt.PaymentProcessorTypeId FROM PaymentProcessors PP  "
//                    + "INNER JOIN protocols proto on PP.ProtocolId = proto.ProtocolId\n"
//                    + "INNER JOIN paymentprocessortypes ppt on PP.PaymentProcessorTypeId = ppt.PaymentProcessorTypeId\n"
//                    + "\n"
//                    + "where PP.PaymentProcessorId = " + ProcessorId;
//            DbResult rs = dm.getRecord(strQuery);
//
//            PaymentProcessor paymentProcessorInfo = null;
//            while (rs.hasNext()) {
//                DbRecord record = rs.next(); 
//                
//                paymentProcessorInfo = new PaymentProcessor();
//                paymentProcessorInfo.setPaymentProcessorId(record.getInt("PaymentProcessorId"));
//                paymentProcessorInfo.setSystemURL(record.getString("SystemURL"));
//                paymentProcessorInfo.setIsLocalProcessor(false);
//                paymentProcessorInfo.setPaymentProcessorType(record.getString("PaymentProcessorType"));
//                paymentProcessorInfo.setPaymentProcessorTypeId(record.getInt("PaymentProcessorTypeId"));
//                paymentProcessorInfo.setIsActive(record.getBoolean("isActive"));
//                paymentProcessorInfo.setProtocol(record.getString("ProtocolName"));
//                paymentProcessorInfo.setUserName(record.getString("SystemUsername"));
//                paymentProcessorInfo.setPassword(record.getString("SystemPassword"));
//                paymentProcessorInfo.setSystemSecret(record.getString("SystemSecret"));
//                paymentProcessorInfo.setMyIP(record.getString("MyIP"));
//                paymentProcessorInfo.setMyPartnerId(record.getString("MyPartnerId"));
//                paymentProcessorInfo.setName(record.getString("Name"));
//                paymentProcessorInfo.setDescription(record.getString("Description"));
//
//
//            }
//          
//            return paymentProcessorInfo;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//    public int storeMerchantToken(String muid, int storeId, int cashierId, String requestUri, String token){
//        try {
//            
//            String query = "INSERT INTO merchanturirequests VALUES (null, '" + muid + "', "+storeId+" ,"+cashierId+" ,'"+ requestUri +"', null, '"+token+"', 0)" ;
//            
//            int seqId = dm.AddRecord(query);
//            return seqId;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//    }
//    public int validateMerchantToken(String uid, String token) {
//        try {
//            String query = "SELECT merchantUid, RequestTokenId FROM merchanturirequests WHERE IsUsed=0 and RequestToken='" + token+"'";
//            
//            DbResult rs = dm.getRecord(query);
//            
//            String muid = "";
//            int RequestTokenId = 0;
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                muid = record.getString("merchantUid");
//                RequestTokenId = record.getInt("RequestTokenId");
//            }
//        
//            return ((uid.compareTo(muid) == 0)? RequestTokenId: 0);
//            
//        } catch (Exception ex) {            
//            logger.log(Level.SEVERE, null, ex);
//            return 0;
//        }
//    }
//    
//    public boolean updateMerchatTokenStatus(int requestTokenId){
//        try {
//            String strQuery = "UPDATE merchanturirequests "
//                    + " SET IsUsed=1"                    
//                    + " WHERE RequestTokenId = " + requestTokenId;
//            boolean ret = dm.UpdateRecord(strQuery);
//            return ret;
//        } catch (Exception ex) {
//           logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//    public boolean isProviderResponseReceivedFor(String pgTransactionId){
//        try {
//            String query = "SELECT PartnerRefId FROM providerresponses WHERE TransactionId=" + pgTransactionId;
//            
//            DbResult rs = dm.getRecord(query);
//            
//            String partnerRefId = "";
//            
//
//            while (rs.hasNext()) {
//               DbRecord record = rs.next(); 
//               partnerRefId = record.getString("PartnerRefId");               
//               
//               return true;
//            }
//        
//            
//        } catch (Exception ex) {            
//            logger.log(Level.SEVERE, null, ex);            
//        }
//        return false;
//    }
//    
//    public int storeProviderResponse(int pgTransactionId, String vRefId, String vBillingId, String vRespCode, String vRespMsg, String vAuthCode, double vAmountSettlement, String content){
//         try {
//            
//            String query = "INSERT INTO providerresponses VALUES (null, " + pgTransactionId + ", '"+vRefId+"' ,'"+vBillingId+"','"+ vRespCode +"', '"+vRespMsg+"', '"+vAuthCode+"', "+vAmountSettlement+", CURRENT_TIMESTAMP() , '"+content+"')" ;
//            
//            int seqId = dm.AddRecord(query);
//            return seqId;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//    }
//    public ProviderResponse getProviderResponse(int seqId){
//         try {
//            
//            String query = "SELECT * FROM providerresponses WHERE SeqId="+seqId;            
//            DbResult rs = dm.getRecord(query);
//            
//            ProviderResponse obj = new ProviderResponse();
//            if (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                obj.setSeqId(record.getInt("SeqId"));
//                obj.setPgTransactionId(record.getInt("TransactionId"));
//                obj.setPartnerRefId(record.getString("PartnerRefId"));
//                obj.setPartnerBillingId(record.getString("PartnerBillingId"));
//                obj.setRespCode(record.getString("RespCode"));
//                obj.setRespMsg(record.getString("RespMsg"));
//                obj.setAuthCode(record.getString("AuthCode"));
//                obj.setAmount(record.getDouble("AmountSettlement"));
//                obj.setTimestamp(record.getString("CreatedDate"));
//                
//                return obj;
//            }
//            return null;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//    public int storeClientRequestInfo(String mrRequestId, int requestTokenId, String merchantUid, String storeUid, String cashierUid, String rqstContent, String mrReferenceId, String mrInvoiceId, String mrTransactionId, String mrCallbackUrl) {
//         try {
//            
//            String query = "INSERT INTO requestmaster VALUES (null, '" + merchantUid + "', '"+storeUid+"', '"+cashierUid+"', '"+ mrInvoiceId +"', '"+mrTransactionId+"', '"+mrReferenceId+"', '"+mrCallbackUrl+"', '"+rqstContent+"', '"+mrRequestId+"', "+requestTokenId+", CURRENT_TIMESTAMP())" ;
//            
//            int requestId = dm.AddRecord(query);
//            return requestId;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//    }

//    public boolean validateUser(String username, String password) {
//        try {
//            String query = "SELECT * FROM users WHERE Login='" + username + "' AND IsActive=1";
//            
//            DbResult rs = dm.getRecord(query);
//            String LoginId = "";
//            String LoginKey = "";
//
//          while (rs.hasNext()) {
//                    DbRecord record = rs.next();
//                
//                LoginId = record.getString("Login");
//                LoginKey = record.getString("Password");
//            }
//        
//            return LoginId.compareTo(username) == 0 && LoginKey.compareTo(password) == 0;
//        } catch (Exception ex) {
//            
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
//        public RequestInfo getMerchantRequestInfoByTranId(int pgTransactionId) {
//        try {
//            String strMerchantInfoQuery = "SELECT * FROM requestmaster rm join transactions t on t.requestid=rm.requestid  WHERE t.transactionid=" + pgTransactionId ;
//            DbResult rs = dm.getRecord(strMerchantInfoQuery);
//            
//            RequestInfo rqstInfo = new RequestInfo();
//           if (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                rqstInfo.setRequestId(record.getInt("RequestId"));
//                rqstInfo.setMerchantUid(record.getString("MerchantUid"));
//                rqstInfo.setStoreId(record.getInt("StoreId"));
//                rqstInfo.setCashierId(record.getInt("CashierId"));
//                rqstInfo.setRequestTokenId(record.getInt("RequestTokenId"));
//                
//                rqstInfo.setMrInvoiceId(record.getString("MrInvoiceId"));
//                rqstInfo.setMrTransactionId(record.getString("MrTransactionId"));
//                rqstInfo.setMrReferenceId(record.getString("MrReferenceId"));
//                
//                rqstInfo.setMrStatusCallbackUrl(record.getString("MrStatusCallbackUrl"));
//                rqstInfo.setRequestContent(record.getString("RequestContent"));
//                rqstInfo.setCreatedDate(record.getString("CreatedDate"));
//            }
//
//            return rqstInfo;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//    
//    
//    public boolean validateMerchantByApiKey(String uid, String key) {
//        try {
//            String query = "SELECT APIKey FROM merchants WHERE UID='" + uid+"'";
//            
//            DbResult rs = dm.getRecord(query);
//            
//            String apiKey = "";
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();                
//                apiKey = record.getString("APIKey");
//            }
//        
//            return (apiKey.compareTo(key) == 0) ;
//            
//        } catch (Exception ex) {            
//            logger.log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
//     public _SysConfigParam db_get_SysConfigParam_Info1(String code) {
//         _SysConfigParam obj = null;
//        try {
//           
//            String query = "Select * from sysconfig where ParamCode = '"+code+"'";
//
//            DbResult rs = dm.getRecord(query);
//            
//            
//            if(rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                obj = new _SysConfigParam();
//                obj.loadFromDbRecord(record);                            
//            }         
//            
//        } catch (Exception exp) {
//            logger.finer("Err: "+exp.getMessage());            
//        }
//        return obj;
//    }
//     
//    public List<_SysConfigParam> db_get_SysConfigParam_List1() {
//        try {
//            List<_SysConfigParam> list = new ArrayList<>();
//            String query = "Select * from sysconfig";
//
//            DbResult rs = dm.getRecord(query);
//
//            while (rs.hasNext()) {
//                DbRecord record = rs.next();
//                
//                _SysConfigParam obj = new _SysConfigParam();
//                obj.loadFromDbRecord(record);
//                               
//                list.add(obj);                
//            }
//         
//            return list;
//        } catch (Exception exp) {
//            logger.finer("Err: "+exp.getMessage());
//            return null;
//        }
//    }
//    
//    public int db_create_SysConfigParam1(String code, String name, String val){        
//         try {            
//            String query = "INSERT INTO sysconfig VALUES (null, '"+ code +"' ,'"+ name +"','"+ val +"')";            
//            int seqId = dm.AddRecord(query);
//            return seqId;
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            return -1;
//        }
//    }
//    
//     public boolean db_update_SysConfigParam1(int id, String code, String name, String val){
//         boolean n = false;
//         try {          
//            
//            String query = "UPDATE sysconfig SET ParamCode='"+code+"', ParamName='"+name+"', ParamValue='"+val+"' WHERE ParamId="+id;
//            n = dm.UpdateRecord(query);
//            
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);            
//        }
//         return n;
//    }
