package com.todo;

import com.safari.pg.util.DbResult;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author Safarifone
 */
public class DataManager  {
    Logger logger = Logger.getLogger(DataManager.class.getName());
    private boolean useConnPool = false;
    private static JdbcTemplate jdbcTemplate;
     
  
    public void setDataSource(DataSource dataSource) {
        logger.info("setDataSource called");
      if(this.jdbcTemplate==null)
        this.jdbcTemplate = new JdbcTemplate(dataSource);       
    }
   
    public DataManager() {
         logger.finest("DataManager constructor called");
         if(this.jdbcTemplate==null)
             logger.info("jdbc conn obj is null");
    }
    public void close() {
        logger.info("close is invoked");
    }
    
    public Connection getConnection() throws Exception {  
        
        Connection c = null;
        DataSource ds = null;
        if(this.jdbcTemplate!=null) {
            ds = jdbcTemplate.getDataSource();
           
        } else {
            logger.finer("failed bcz of jdbcTemplate is null");
        }
        
        if(useConnPool){
            logger.finer("Getting connection from pool");
            c = ds.getConnection();
            logger.finer(((c == null)?"failed":"success"));
            
        } else {
            
            //create manual database connection
            logger.finer("Creating manual connection");
            try {
                    
                    String url = (String) PropertyUtils.getSimpleProperty(ds, "jdbcUrl");
                    String user = (String) PropertyUtils.getSimpleProperty(ds, "username");
                    String password = (String) PropertyUtils.getSimpleProperty(ds, "password");
                   
                    // create a connection to the database
                    Class.forName("com.mysql.jdbc.Driver"); 
                    c = DriverManager.getConnection(url, user, password);
                    logger.finer(((c == null)?"failed":"success"));
                    // more processing here
                    // ... 
                } catch(SQLException e) {
                   logger.finer("Err: " +  e.getMessage());
                } 
        }
        
        
         if(c != null){
            java.util.Properties props = null;
            try {
                props = c.getClientInfo();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            logger.finer("CON_Properties: "+props.toString());
            
         } else {
             logger.finer("Connection is null");
         }
        
         
        return c;
    }
    
//    public void getDataSourceStats(){
//        DataSource dataSource = (DataSource) jdbcTemplate.getDataSource();
//        logger.info("Max Idle: " + dataSource.getMaxIdle());
//        logger.info("Max Active: " + dataSource.getMaxIdle());
//        logger.info("Active: " + dataSource.getNumActive());
//        logger.info("Idle: " + dataSource.getNumIdle());
//        
//    }
    public DbResult getRecord(String selectQuery) throws SQLException {

        DbResult result = null;

        try {
            logger.finest("Retrieving data from database");
            logger.finest("Executing db command, "+ selectQuery);
            
               Object obj = null;
               try{
                   if(jdbcTemplate==null)
                       throw new Exception("jdbcTemplate obj is null");

                   
                    obj =  jdbcTemplate.query(selectQuery, new ResultSetExtractor<Object>() {
                            @Override
                            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {  
                                 logger.finest("extractData resultset");
                                 return (new DbResult(rs));
                            }
                          });

               } catch(Exception ex) {
                   logger.log(Level.SEVERE, null, ex);
                   logger.finer("Failed to execute the request");
               }
           
            logger.finest("Return resultset");
            result = (DbResult) obj;
            
            
            
            return result;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }

    }
   
    public int AddRecord(String insertQuery) throws Exception {
        DbResult result = null;

        try {
            logger.finest("Inserting data in database");
            logger.finest("Executing db command, "+ insertQuery);
            
               Object obj = null;
               KeyHolder keyHolder = new GeneratedKeyHolder();
               try{
                   if(jdbcTemplate==null)
                       throw new Exception("jdbcTemplate obj is null");

                    PreparedStatementCreator psc =  new PreparedStatementCreator() {
                                                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                                            PreparedStatement ps = connection.prepareStatement(insertQuery,  new String[] { "id" });            
                                                            return ps;
                                                        }
                                                    };
                   
                    obj =  jdbcTemplate.update(psc, keyHolder);

               } catch(Exception ex) {
                   logger.log(Level.SEVERE, null, ex);
                   logger.finer("Failed to execute the request");
               }
           
            logger.finest("display the keys");
            Map<String, Object> keys = keyHolder.getKeys();            
            for(Map.Entry<String, Object> entry: keys.entrySet()){
                logger.finest("Key:"+entry.getKey()+", val: "+entry.getValue());
            }
            
            logger.finest("Return key value");
            return keyHolder.getKey().intValue();
            

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    public boolean UpdateRecord(String updateQuery) throws Exception {
        

        try {
            logger.finest("Updating data in database");
            logger.finest("Executing db command, "+ updateQuery);
            
               int obj = 0;
               try{
                   if(jdbcTemplate==null)
                       throw new Exception("jdbcTemplate obj is null");

                   
                    obj =  jdbcTemplate.update(updateQuery);
                    
               } catch(Exception ex) {
                   logger.log(Level.SEVERE, null, ex);
                   logger.finer("Failed to execute the request");
               }
           
            logger.finest("Return resultset");
           
            return (obj>0)?true: false;
            //print(resultList);            
            

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        }
     }
    public boolean DeleteRecord(String deleteQuery) throws Exception {
         
        try {
            logger.finest("deleting data in database");
            logger.finest("Executing db command, "+ deleteQuery);
            
               int obj = 0;
               try{
                   if(jdbcTemplate==null)
                       throw new Exception("jdbcTemplate obj is null");

                   
                    obj =  jdbcTemplate.update(deleteQuery);

               } catch(Exception ex) {
                   logger.log(Level.SEVERE, null, ex);
                   logger.finer("Failed to execute the request");
               }
           
            logger.finest("Return resultset");
           
             return (obj>0)?true: false;
            //print(resultList);            
            

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        }
     }
//     void convertToJson(ResultSet rs) {
//           try {
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int numColumns = rsmd.getColumnCount();
//            String[] columnNames = new String[numColumns];
//            int[] columnTypes = new int[numColumns];
//
//            for (int i = 0; i < columnNames.length; i++) {
//                columnNames[i] = rsmd.getColumnLabel(i + 1);
//                columnTypes[i] = rsmd.getColumnType(i + 1);
//            }
//JsonGenerator jgen = new JsonGenerator (rs);
//            jgen.writeStartArray();
//
//            while (rs.next()) {
//
//                boolean b;
//                long l;
//                double d;
//
//                jgen.writeStartObject();
//
//                for (int i = 0; i < columnNames.length; i++) {
//
//                    jgen.writeFieldName(columnNames[i]);
//                    switch (columnTypes[i]) {
//
//                    case Types.INTEGER:
//                        l = rs.getInt(i + 1);
//                        if (rs.wasNull()) {
//                            jgen.writeNull();
//                        } else {
//                            jgen.writeNumber(l);
//                        }
//                        break;
//
//                    case Types.BIGINT:
//                        l = rs.getLong(i + 1);
//                        if (rs.wasNull()) {
//                            jgen.writeNull();
//                        } else {
//                            jgen.writeNumber(l);
//                        }
//                        break;
//
//                    case Types.DECIMAL:
//                    case Types.NUMERIC:
//                        jgen.writeNumber(rs.getBigDecimal(i + 1));
//                        break;
//
//                    case Types.FLOAT:
//                    case Types.REAL:
//                    case Types.DOUBLE:
//                        d = rs.getDouble(i + 1);
//                        if (rs.wasNull()) {
//                            jgen.writeNull();
//                        } else {
//                            jgen.writeNumber(d);
//                        }
//                        break;
//
//                    case Types.NVARCHAR:
//                    case Types.VARCHAR:
//                    case Types.LONGNVARCHAR:
//                    case Types.LONGVARCHAR:
//                        jgen.writeString(rs.getString(i + 1));
//                        break;
//
//                    case Types.BOOLEAN:
//                    case Types.BIT:
//                        b = rs.getBoolean(i + 1);
//                        if (rs.wasNull()) {
//                            jgen.writeNull();
//                        } else {
//                            jgen.writeBoolean(b);
//                        }
//                        break;
//
//                    case Types.BINARY:
//                    case Types.VARBINARY:
//                    case Types.LONGVARBINARY:
//                        jgen.writeBinary(rs.getBytes(i + 1));
//                        break;
//
//                    case Types.TINYINT:
//                    case Types.SMALLINT:
//                        l = rs.getShort(i + 1);
//                        if (rs.wasNull()) {
//                            jgen.writeNull();
//                        } else {
//                            jgen.writeNumber(l);
//                        }
//                        break;
//
//                    case Types.DATE:
//                        provider.defaultSerializeDateValue(rs.getDate(i + 1), jgen);
//                        break;
//
//                    case Types.TIMESTAMP:
//                        provider.defaultSerializeDateValue(rs.getTime(i + 1), jgen);
//                        break;
//
//                    case Types.BLOB:
//                        Blob blob = rs.getBlob(i);
//                        provider.defaultSerializeValue(blob.getBinaryStream(), jgen);
//                        blob.free();
//                        break;
//
//                    case Types.CLOB:
//                        Clob clob = rs.getClob(i);
//                        provider.defaultSerializeValue(clob.getCharacterStream(), jgen);
//                        clob.free();
//                        break;
//
//                    case Types.ARRAY:
//                        throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type ARRAY");
//
//                    case Types.STRUCT:
//                        throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type STRUCT");
//
//                    case Types.DISTINCT:
//                        throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type DISTINCT");
//
//                    case Types.REF:
//                        throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type REF");
//
//                    case Types.JAVA_OBJECT:
//                    default:
//                        provider.defaultSerializeValue(rs.getObject(i + 1), jgen);
//                        break;
//                    }
//                }
//
//                jgen.writeEndObject();
//            }
//
//            jgen.writeEndArray();
//
//        } catch (Exception e) {
//            throw new ResultSetSerializerException(e);
//        }
//
//     }
//    public JSONArray convertToJson(ResultSet rs) throws Exception {
//		if (rs == null)
//			return null;
//		logger.fine("Converting resultset to json-object.");
//		JSONArray json = new JSONArray();
//
//		ResultSetMetaData rsmd = rs.getMetaData();
//
//		int numColumns = rsmd.getColumnCount();
//		logger.fine("ColumnCount: " + numColumns);
//		for (int i = 1; i <= numColumns; i++) {
//			logger.fine(rsmd.getColumnName(i));
//		}
//
//		while (rs.next()) {
//			JSONObject obj = new JSONObject();
//
//			for (int i = 1; i <= numColumns; i++) {
//				String column_name = rsmd.getColumnName(i);
//
//				if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
//					obj.put(column_name.toUpperCase(), rs.getArray(column_name));
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
//					int n = rs.getInt(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? 0 : n);
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
//					boolean n = rs.getBoolean(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? 0 : n);
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
//					obj.put(column_name.toUpperCase(), rs.getBlob(column_name));
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
//					double n = rs.getDouble(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? 0 : n);
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
//					float n = rs.getFloat(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? 0 : n);
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
//					int n = rs.getInt(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? 0 : n);
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
//					obj.put(column_name.toUpperCase(), rs.getNString(column_name));
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
//					String str = rs.getString(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? "" : str);
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
//					int n = rs.getInt(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? 0 : n);
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
//					int n = rs.getInt(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? 0 : n);
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
//
//					Date dt = rs.getDate(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? "" : dt.toString());
//
//				} else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
//					Timestamp dt = rs.getTimestamp(column_name);
//					obj.put(column_name.toUpperCase(), (rs.wasNull()) ? "" : dt.toString());
//
//				} else {
//					obj.put(column_name.toUpperCase(), rs.getObject(column_name));
//				}
//			}
//
//			json.put(obj);
//		}
//		rs.close();
//		return json;
//	}
}

