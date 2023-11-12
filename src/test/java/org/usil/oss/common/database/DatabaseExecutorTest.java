package org.usil.oss.common.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseExecutorTest {

  private final Logger logger = LoggerFactory.getLogger(DatabaseExecutorTest.class);

  @Test
  public void executeSimpleScriptStringForSuccess() throws Exception {

    Connection connection = mock(Connection.class);
    ConnectionHelper connectionHelper = mock(ConnectionHelper.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    Statement statement = mock(Statement.class);

    when(connection.createStatement()).thenReturn(statement);

    when(statement.execute("selec * from dual")).thenReturn(true);

    ResultSet mockResultSet = MockResultSet.create(new String[] {"name", "lastname"},
        new String[][] {{"jane", "doe"}, {"kurt", "weller"}});

    when(statement.getResultSet()).thenReturn(mockResultSet);

    DatabaseExecutor databaseExecutor = new DatabaseExecutor();
    FieldUtils.writeField(databaseExecutor, "connectionHelper", connectionHelper, true);

    ArrayList dbResponse = databaseExecutor.executeSimpleScriptString("DatabaseHelper", "localhost",
        2708, "sid", "jane", "****", "selec * from dual;");
    assertEquals(2, dbResponse.size());
    java.util.ArrayList row1 = (ArrayList) dbResponse.get(0);
    assertEquals("jane", row1.get(0));
  }

  @Test
  public void executeSimpleScriptStringForError() throws Exception {

    ConnectionHelper connectionHelper = mock(ConnectionHelper.class);

    doThrow(new Exception("Im a jerk")).when(connectionHelper).getConnection("DatabaseHelper",
        "localhost", 2708, "sid", "jane", "****");

    DatabaseExecutor databaseExecutor = new DatabaseExecutor();
    FieldUtils.writeField(databaseExecutor, "connectionHelper", connectionHelper, true);

    try {
      databaseExecutor.executeSimpleScriptString("DatabaseHelper", "localhost", 2708, "sid", "jane",
          "****", "selec * from dual;");
      fail(
          "My method didn't throw when I expected it to: executeSimpleScriptString throw an error");
    } catch (Exception e) {
      this.logger.debug("Exception was expected o_O");
    }

  }

  @Test
  public void executeSimpleScriptFileForSuccess() throws Exception {

    Connection connection = mock(Connection.class);
    ConnectionHelper connectionHelper = mock(ConnectionHelper.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    Statement statement = mock(Statement.class);

    when(connection.createStatement()).thenReturn(statement);

    when(statement.execute("selec * from dual")).thenReturn(true);

    ResultSet mockResultSet = MockResultSet.create(new String[] {"name", "lastname"},
        new String[][] {{"jane", "doe"}, {"kurt", "weller"}});

    when(statement.getResultSet()).thenReturn(mockResultSet);

    DatabaseExecutor databaseExecutor = new DatabaseExecutor();
    FieldUtils.writeField(databaseExecutor, "connectionHelper", connectionHelper, true);

    Path tempFile = Files.createTempFile("databaseops-test-sql-", null);
    Files.write(tempFile, "selec * from dual;".getBytes(StandardCharsets.UTF_8));

    ArrayList dbResponse = databaseExecutor.executeSimpleScriptFile("DatabaseHelper", "localhost",
        2708, "sid", "jane", "****", "root", "****", tempFile.toAbsolutePath().toString());
    assertEquals(2, dbResponse.size());
    java.util.ArrayList row1 = (ArrayList) dbResponse.get(0);
    assertEquals("jane", row1.get(0));
  }

  @Test
  public void executeSimpleScriptFileForError() throws Exception {

    ConnectionHelper connectionHelper = mock(ConnectionHelper.class);

    doThrow(new Exception("Im a jerk")).when(connectionHelper).getConnection("DatabaseHelper",
        "localhost", 2708, "sid", "jane", "****");

    DatabaseExecutor databaseExecutor = new DatabaseExecutor();
    FieldUtils.writeField(databaseExecutor, "connectionHelper", connectionHelper, true);

    try {
      databaseExecutor.executeSimpleScriptFile("DatabaseHelper", "localhost", 2708, "sid", "jane",
          "****", "root", "****", "/foo/bar/baz.sql");
      fail(
          "My method didn't throw when I expected it to: executeSimpleScriptString throw an error");
    } catch (Exception e) {
      this.logger.debug("Exception was expected o_O");
    }
  }

  @Test
  public void executeEmptyBlankScript() throws Exception {

    Connection connection = mock(Connection.class);
    ConnectionHelper connectionHelper = mock(ConnectionHelper.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    DatabaseExecutor databaseExecutor = new DatabaseExecutor();
    FieldUtils.writeField(databaseExecutor, "connectionHelper", connectionHelper, true);

    try {
      databaseExecutor.executeSimpleScriptString("DatabaseHelper", "localhost", 2708, "sid", "jane",
          "****", "");
      fail("My method didn't throw when I expected it to: script is empty");
    } catch (Exception e) {
      this.logger.debug("Exception was expected o_O");
    }
  }
}
