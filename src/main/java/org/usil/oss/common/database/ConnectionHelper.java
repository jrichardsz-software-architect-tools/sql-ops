package org.usil.oss.common.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import org.usil.oss.common.file.ClassPathProperties;

public class ConnectionHelper implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public Connection getConnection(String engine, String host, int port, String databaseName, String user, String password) throws Exception {
    String driver = ClassPathProperties.getProperty(engine + ".driver");
    try {
      Class.forName(driver);
    } catch (Exception e) {
      throw new Exception("Failed to stabilish connection", e);
    } 
    try {
      String jdbcUrlTemplate = ClassPathProperties.getProperty(engine + ".jdbcUrlTemplate");
      jdbcUrlTemplate = jdbcUrlTemplate.replace("#host", host);
      jdbcUrlTemplate = jdbcUrlTemplate.replace("#port", "" + port);
      jdbcUrlTemplate = jdbcUrlTemplate.replace("#database_name", databaseName);
      DriverManager.setLoginTimeout(30);
      return DriverManager.getConnection(jdbcUrlTemplate, user, password);
    } catch (Exception e) {
      throw new Exception("Failed to stabilish connection.", e);
    } 
  }
}
