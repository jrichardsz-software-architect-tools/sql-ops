package org.usil.oss.common.database;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import org.usil.oss.common.string.StringHelper;
import com.mysql.cj.util.StringUtils;

public class DatabaseExecutor implements Serializable {

  private static final long serialVersionUID = 1L;
  private ScriptExecutor scriptExecutor = new ScriptExecutor();
  private ConnectionHelper connectionHelper = new ConnectionHelper();

  public ArrayList<?> executeSimpleScriptFile(String engine, String host, int port, String sid,
      String databaseUser, String databasePassword, String databaseDbaUser,
      String databaseDbaPassword, String queryAbsoluteFilePath) throws Exception {
    String sqlString = null;

    try {
      sqlString = new String(Files.readAllBytes(Paths.get(queryAbsoluteFilePath)));
    } catch (Exception e) {
      throw new Exception("sql file was not found: " + queryAbsoluteFilePath, e);
    }

    Map<String, String> metadata = StringHelper.getMetadataScript(sqlString);

    String user, password = null;
    if (metadata.get("use_dba_user") != null
        && metadata.get("use_dba_user").contentEquals("true")) {
      user = databaseDbaUser;
      password = databaseDbaPassword;
      if (databaseDbaUser == null || databaseDbaUser.isEmpty() || databaseDbaPassword == null
          || databaseDbaPassword.isEmpty()) {
        throw new Exception("use_super_user is enabled but there were not configured");
      }
    } else {
      user = databaseUser;
      password = databasePassword;
    }

    return executeSimpleScriptString(engine, host, port, sid, user, password, sqlString);
  }

  public ArrayList executeSimpleScriptString(String engine, String host, int port, String sid,
      String user, String password, String sqlString) throws Exception {

    if (StringUtils.isEmptyOrWhitespaceOnly(sqlString)) {
      throw new Exception("Failed to execute sql string. Sql string is empty or blank.");
    }

    Connection conn = connectionHelper.getConnection(engine, host, port, sid, user, password);
    try {
      ArrayList<?> result = scriptExecutor.exec(sqlString, conn);
      return result;
    } catch (Exception e) {
      throw new Exception("Failed to execute sql string: " + sqlString, e);
    } finally {
      conn.close();
    }
  }


}
