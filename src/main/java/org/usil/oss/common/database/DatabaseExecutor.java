package org.usil.oss.common.database;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import com.mysql.cj.util.StringUtils;

public class DatabaseExecutor implements Serializable{

  private ScriptExecutor scriptExecutor = new ScriptExecutor();
  private ConnectionHelper connectionHelper = new ConnectionHelper();

  public ArrayList<?> executeSimpleScriptFile(String engine, String host, int port, String sid,
      String user, String password, String queryAbsoluteFilePath) throws Exception {
    String sqlString = null;

    try {
      sqlString = new String(Files.readAllBytes(Paths.get(queryAbsoluteFilePath)));
    } catch (Exception e) {
      throw new Exception("sql file was not found: " + queryAbsoluteFilePath, e);
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
