package com.todo;

import com.todo.cbsAuthenticate;
import com.todo.cbsAuthorize;
import com.todo.DataAccessController;
import com.safari.pg.util.CbsErrors;
import com.safari.pg.util.CbsException;
import com.safari.pg.util.IxAuthenticate;
import com.safari.pg.util.CbsResponse;
import com.safari.pg.util.IxAuthorize;
import com.safari.pg.util._MerchantSetting;
import com.safari.pg.util._UserAuthInfo;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Safarifone
 */
public class CbsInterface {
    Logger logger = Logger.getLogger(CbsInterface.class.getName());
    
    public _UserAuthInfo authenticate(DataAccessReplicaController dbc, String accessId, String password, int channelId, int authType) throws CbsException{
        logger.info("cbsInterface: authenticate"); 
        
        _UserAuthInfo res = null;
        try {
            
            logger.finest("Prepare input"); 
            IxAuthenticate input = new IxAuthenticate();
            input.setAccessId(accessId);
            input.setSecret(password);
            input.setChannelId(channelId);
            input.setAuthType(authType);
            input.setProfileStatusValidation(true);
            
            logger.info("Input: "+input.toString()); 
            logger.info("Execute cbs-service"); 
            cbsAuthenticate x = new cbsAuthenticate(dbc);                    
            CbsResponse output = x.process(input);      
            logger.info("Output: "+output.toString());     
            
            if(output.getResultCode() != CbsErrors.CBSEC_SUCCESS){
                throw new  CbsException(output.getResultCode());
        
            } else {
                res = (_UserAuthInfo)output.getPayload();
            }
           
        } catch(CbsException e){
            logger.info("cbsInt: " + e.toString());       
            throw e;
        } catch(Exception e){
            logger.info("cbsInt: " + e.getMessage());       
            throw new CbsException(CbsErrors.CBSEC_INTERFACE_ERROR);
        }
        return res;
    }
    
    public _UserAuthInfo authorize(DataAccessReplicaController dbc, int userId, String serviceCode, double amount) throws CbsException{
        logger.finest("cbsInterface: authorize"); 
        
        _UserAuthInfo res = null;
        try {
            
            logger.finest("Prepare input"); 
            IxAuthorize input = new IxAuthorize();
            input.setUserId(userId);
            input.setServiceCode(serviceCode);
            input.setAmount(amount);
            
            logger.finest("Input: "+input.toString()); 
            logger.finest("Execute cbs-service"); 
            cbsAuthorize x = new cbsAuthorize(dbc);                    
            CbsResponse output = x.process(input);      
            logger.finest("Output: "+output.toString());     
            
            if(output.getResultCode() != CbsErrors.CBSEC_SUCCESS){
                logger.finest("CBS returned error: "+output.getResultCode());
                throw new  CbsException(output.getResultCode());
        
            } else {
                res = (_UserAuthInfo)output.getPayload();
            }
           
        } catch(CbsException e){
            logger.finest("cbsInt: " + e.toString());       
            throw e;
        } catch(Exception e){
            logger.finest("cbsInt: " + e.getMessage());       
            throw new CbsException(CbsErrors.CBSEC_INTERFACE_ERROR);
        }
        return res;
    }
    
    public boolean validateMerchantGroupSettings(DataAccessController dbc, int groupId, int featureId) throws CbsException{
        logger.finest("cbsInterface: validateMerchantGroupSettings for groupid: "+ groupId); 
        
        boolean res = false;
        try {
            
            logger.finest("Retriving group settings..."); 
            List<_MerchantSetting> list = dbc.db_get_merchantgroup_settings(groupId);
            
             for (_MerchantSetting obj : list) {
               if(obj.getFeatureId()==featureId){
                   res = true;
                   break;
               }
            }
           
        } catch(Exception e){
            logger.finest("cbsInt: " + e.getMessage());       
            throw new CbsException(CbsErrors.CBSEC_INTERFACE_ERROR);
        }
        return res;
    }
    
}

