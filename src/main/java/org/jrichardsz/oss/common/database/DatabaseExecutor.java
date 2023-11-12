package org.jrichardsz.oss.common.database;

import com.mysql.cj.util.StringUtils;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jrichardsz.oss.common.string.StringHelper;

public class DatabaseExecutor implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private ConnectionHelper connectionHelper = new ConnectionHelper();
  
  private final Logger logger = LoggerFactory.getLogger(DatabaseExecutor.class);
  
  public ArrayList<?> executeSimpleScriptFile(String engine, String host, int port, String sid, String databaseUser, String databasePassword, String databaseDbaUser, String databaseDbaPassword, String queryAbsoluteFilePath) throws Exception {
    String user, sqlString = null;
    try {
      sqlString = new String(Files.readAllBytes(Paths.get(queryAbsoluteFilePath, new String[0])));
    } catch (Exception e) {
      throw new Exception("sql file was not found: " + queryAbsoluteFilePath, e);
    } 
    Map<String, String> metadata = StringHelper.getMetadataScript(sqlString);
    this.logger.info("Metadata");
    this.logger.info(metadata.toString());
    String password = null;
    if (metadata.get("use_dba_user") != null && ((String)metadata
      .get("use_dba_user")).contentEquals("true")) {
      user = databaseDbaUser;
      password = databaseDbaPassword;
      this.logger.info("use_dba_user is enabled");
      if (databaseDbaUser == null || databaseDbaUser.isEmpty() || databaseDbaPassword == null || databaseDbaPassword
        .isEmpty())
        throw new Exception("use_super_user is enabled but there were not configured"); 
    } else {
      this.logger.info("use_dba_user is not enabled. Simple user will be used");
      user = databaseUser;
      password = databasePassword;
    } 
    return executeSimpleScriptString(engine, host, port, sid, user, password, sqlString);
  }
  
  public ArrayList executeSimpleScriptString(String engine, String host, int port, String sid, String user, String password, String sqlString) throws Exception {
    if (StringUtils.isEmptyOrWhitespaceOnly(sqlString))
      throw new Exception("Failed to execute sql string. Sql string is empty or blank."); 
    Connection conn = this.connectionHelper.getConnection(engine, host, port, sid, user, password);
    try {
      SqlRunner sqlRunner = new SqlRunner(conn, true, true);
      ArrayList<?> result = sqlRunner.runScript(sqlString);      
      return result;
    }
    finally {
      conn.close();
    } 
  }
}
