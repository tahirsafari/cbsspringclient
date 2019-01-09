package com.todo;

import com.safari.pg.util.CbsException;
import com.safari.pg.util.CbsConstants;
import com.todo.cbsServiceBase;
import com.todo.DataAccessController;
import com.safari.pg.util.CbsErrors;
import com.safari.pg.util.IxAuthenticate;
import com.safari.pg.util.CbsResponse;
import com.safari.pg.util._UserAuthInfo;
import com.safari.pg.util.AppConstants;
import java.util.logging.Logger;

/**
 *
 * @author Safarifone
 */
public class cbsAuthenticate extends cbsServiceBase {
    Logger logger = Logger.getLogger(cbsAuthenticate.class.getName());    
    
    public cbsAuthenticate(DataAccessReplicaController dbc){
        setDataAccessController(dbc);
    }
    
   public CbsResponse process(IxAuthenticate input) throws CbsException{
            CbsResponse res = new CbsResponse();
            logger.info("Retrieving user data for ["+input.getAccessId()+"], AuthType: "+input.getAuthType());
            
            _UserAuthInfo obj = null;
            try {
                if(input.getAuthType() == CbsConstants.AUTHTYPE_LOGINID_PWD){
                    logger.info("Handling authtype: login_pwd");
//                    obj = dbc.db_get_User_AuthInfo_ByLoginId(input.getAccessId(), input.getChannelId());
//                    if(obj==null){
//                        throw new CbsException(CbsErrors.CBSEC_AUTH_INVALID_LOGINID);
//                    }
//                    logger.info("Validating the credentials");
//                    if(!validate(obj.getLoginPassword(), input.getSecret())){
//                        throw new CbsException(CbsErrors.CBSEC_AUTH_INVALID_PWD);
//                    }
                    
                    
                } else if(input.getAuthType() == CbsConstants.AUTHTYPE_USERID_ACCESSKEY){
                                        logger.info("Handling authtype: userid_accesskey");
                    //obj = dbc.db_get_User_AuthInfo_ByUserId(Integer.parseInt(input.getAccessId()));
                    
                    if(obj==null){
                        throw new CbsException(CbsErrors.CBSEC_AUTH_INVALID_USERID);
                    }
                    logger.info("Validating the credentials");
                    if(!validate(obj.getAccessKey(), input.getSecret())){
                        throw new CbsException(CbsErrors.CBSEC_AUTH_INVALID_KEY);
                    }
                    
                } else if(input.getAuthType() == CbsConstants.AUTHTYPE_USERID_TRANPWD){
                                        logger.info("Handling authtype: userid_tranpwd");
                    //obj = dbc.db_get_User_AuthInfo_ByUserId(Integer.parseInt(input.getAccessId()));
                    
                    if(obj==null){
                        throw new CbsException(CbsErrors.CBSEC_AUTH_INVALID_USERID);
                    }
                    logger.info("Validating the credentials");
                    if(!validate(obj.getTranPassword(), input.getSecret())){
                        throw new CbsException(CbsErrors.CBSEC_AUTH_INVALID_PWD);
                    }
                    
                } else if(input.getAuthType() == CbsConstants.AUTHTYPE_USERID_ACTIVATIONTOKEN){
                                        logger.info("Handling authtype: userid_acttoken");
                   // obj = dbc.db_get_User_AuthInfo_ByUserId(Integer.parseInt(input.getAccessId()));
                    
                    if(obj==null){
                        throw new CbsException(CbsErrors.CBSEC_AUTH_INVALID_USERID);
                    }
                    logger.info("Validating the credentials");
                    if(!validate(obj.getActivationToken(), input.getSecret())){
                        throw new CbsException(CbsErrors.CBSEC_AUTH_INVALID_ACTIVATIONTOKEN);
                    }
                    
                } else {
                    logger.info("invalid authtype");
                    throw new CbsException(CbsErrors.CBSEC_AUTH_INVALID_AUTHTYPE);
                }                
                
                if(input.getProfileStatusValidation()){
                    logger.info("validating profile status");
                    if(obj.getProfileStatusId()!=AppConstants.USER_ACCESSPROFILE_STATUS_ACTIVE){
                        throw new CbsException(CbsErrors.CBSEC_USER_PROFILESTATUS_INVALID);
                    }
                }
                                
                res.setPayload(obj);
                
            } catch(CbsException e){
                logger.info("CbsErr: " + e.getMessage());                    
                res.setResultCode(e.errCode);
                
            } catch(Exception e){                
                logger.info("CbsExc: " + e.getMessage());                    
                res.setResultCode(CbsErrors.CBSEC_AUTH_FAILED);
            }
        
        return res;
    }
    
    boolean validate(String data1, String data2){
        boolean res = false;
        if(data1.equals(data2))
            res = true;
     
        return res;
    }
}