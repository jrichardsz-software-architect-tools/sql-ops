package org.usil.oss.common.database;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;

public class DatabaseExecutor {

  private ScriptExecutor scriptExecutor = new ScriptExecutor(true, true);
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

    Connection conn = connectionHelper.getConnection(engine, host, port, sid, user, password);
    try {
      ArrayList<?> result = scriptExecutor.runScript(conn, sqlString);
      return result;
    } catch (Exception e) {
      throw new Exception("Failed to execute sql string: " + sqlString, e);
    }
  }


}
