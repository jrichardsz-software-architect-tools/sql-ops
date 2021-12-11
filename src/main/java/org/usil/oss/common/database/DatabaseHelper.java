package org.usil.oss.common.database;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usil.oss.common.file.ClassPathProperties;

public class DatabaseHelper {

  private final Logger logger = LogManager.getLogger(DatabaseHelper.class);

  private Connection getConnection(String engine, String host, int port, String databaseName,
      String user, String password) throws Exception {

    String driver = ClassPathProperties.getProperty(engine + ".driver");

    try {
      Class.forName(driver);
    } catch (Exception e) {
      // e.printStackTrace();
      throw new Exception("Failed to stabilish connection", e);
    }

    try {
      String jdbcUrlTemplate = ClassPathProperties.getProperty(engine + ".jdbcUrlTemplate");
      jdbcUrlTemplate = jdbcUrlTemplate.replace("#host", host);
      jdbcUrlTemplate = jdbcUrlTemplate.replace("#port", "" + port);
      jdbcUrlTemplate = jdbcUrlTemplate.replace("#database_name", databaseName);

      logger.debug("jdbc url: " + jdbcUrlTemplate);

      DriverManager.setLoginTimeout(30);
      Connection c = DriverManager.getConnection(jdbcUrlTemplate, user, password);
      return c;
    } catch (Exception e) {
      throw new Exception("Failed to stabilish connection.", e);
    }

  }

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

    Connection c = getConnection(engine, host, port, sid, user, password);
    try {
      /*
       * CallableStatement cs = c.prepareCall(sqlString); cs.execute();
       */
      // Statement stmt = c.createStatement();
      // stmt.execute(sqlString);

      // ResultSet rs = cs.getResultSet();
      // if (rs == null) {
      // return new ArrayList<Object>();
      // }

      ScriptRunner runner = new ScriptRunner(c, true, true);
      ArrayList<?> result = runner.runScript(new StringReader(sqlString));

      // ArrayList<?> result = new ArrayList<Object>();
      // ArrayList<?> result = results2Array(rs);

      // stmt.close();
      // c.close();
      return result;
    } catch (Exception e) {
      throw new Exception("Failed to execute sql string: " + sqlString, e);
    }
  }


}
