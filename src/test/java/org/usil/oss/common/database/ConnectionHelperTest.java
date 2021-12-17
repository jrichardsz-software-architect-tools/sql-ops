package org.usil.oss.common.database;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.io.File;
import java.sql.DriverManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.usil.oss.common.file.ClassPathProperties;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DriverManager.class, ClassPathProperties.class, Class.class})
@PowerMockIgnore("javax.management.*")
public class ConnectionHelperTest {

  @Test
  public void shouldFailOnUnknownDriver() throws Exception {
    PowerMockito.mockStatic(ClassPathProperties.class);
    BDDMockito.given(ClassPathProperties.getProperty("foo.driver")).willReturn("bar.baz");

    ConnectionHelper connectionHelper = new ConnectionHelper();

    try {
      connectionHelper.getConnection("foo", "host", 0, "db", "jane", "***");
      fail(
          "My method didn't throw when I expected it to: executeSimpleScriptString throw an error");
    } catch (Exception e) {
    }
  }

  @Test
  public void shouldFailOnConnectionError() throws Exception {
    PowerMockito.mockStatic(ClassPathProperties.class);
    BDDMockito.given(ClassPathProperties.getProperty("conn.jdbcUrlTemplate"))
        .willReturn("jdbc:sqlite:/foo/bar.db");
    BDDMockito.given(ClassPathProperties.getProperty("conn.driver")).willReturn("org.sqlite.JDBC");

    ConnectionHelper connectionHelper = new ConnectionHelper();

    try {
      connectionHelper.getConnection("conn", "10.10.10.10", 2708, "foo", "jane", "***");
      fail(
          "My method didn't throw when I expected it to: executeSimpleScriptString throw an error");
    } catch (Exception e) {
    }
  }

  @Test
  public void successConnection() throws Exception {

    String basePath = new File("").getAbsolutePath();
    String sqliteDbFilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/ScriptExecutorTest/chinook.db";
    PowerMockito.mockStatic(ClassPathProperties.class);
    BDDMockito.given(ClassPathProperties.getProperty("conn.jdbcUrlTemplate"))
        .willReturn("jdbc:sqlite:" + sqliteDbFilePath);
    BDDMockito.given(ClassPathProperties.getProperty("conn.driver")).willReturn("org.sqlite.JDBC");

    ConnectionHelper connectionHelper = new ConnectionHelper();

    Object dbConnection =
        connectionHelper.getConnection("conn", "10.10.10.10", 2708, "foo", "jane", "***");

    assertNotNull(dbConnection);

  }
}
