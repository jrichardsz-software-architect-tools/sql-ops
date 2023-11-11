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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseExecutorTest {

  @Mock
  private ConnectionHelper connectionHelper;

  @Mock
  private SqlRunner sqlRunner;

  @InjectMocks
  private DatabaseExecutor databaseHelper;

  @Test
  public void executeSimpleScriptStringForSuccess() throws Exception {

    Connection connection = mock(Connection.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    Statement statement = mock(Statement.class);

    when(connection.createStatement()).thenReturn(statement);

    when(statement.execute("selec * from dual ")).thenReturn(true);

    ResultSet mockResultSet = MockResultSet.create(new String[] {"name", "lastname"},
        new String[][] {{"jane", "doe"}, {"kurt", "weller"}});

    when(statement.getResultSet()).thenReturn(mockResultSet);

    ArrayList dbResponse = databaseHelper.executeSimpleScriptString("DatabaseHelper", "localhost",
        2708, "sid", "jane", "****", "selec * from dual;");
    assertEquals(2, dbResponse.size());
    java.util.ArrayList row1 = (ArrayList) dbResponse.get(0);
    assertEquals("jane", row1.get(0));
  }

  @Test
  public void executeSimpleScriptStringForError() throws Exception {

    Connection connection = mock(Connection.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    ArrayList result = new ArrayList();
    result.add("adasd");

    doThrow(new Exception("Im a jerk")).when(sqlRunner).runScript("selec * from dual;");

    try {
      databaseHelper.executeSimpleScriptString("DatabaseHelper", "localhost", 2708, "sid", "jane",
          "****", "selec * from dual;");
      fail(
          "My method didn't throw when I expected it to: executeSimpleScriptString throw an error");
    } catch (Exception e) {
    }
  }

  @Test
  public void executeSimpleScriptFileForSuccess() throws Exception {

    Connection connection = mock(Connection.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    Statement statement = mock(Statement.class);

    when(connection.createStatement()).thenReturn(statement);

    when(statement.execute("selec * from dual ")).thenReturn(true);

    ResultSet mockResultSet = MockResultSet.create(new String[] {"name", "lastname"},
        new String[][] {{"jane", "doe"}, {"kurt", "weller"}});

    when(statement.getResultSet()).thenReturn(mockResultSet);

    Path tempFile = Files.createTempFile("databaseops-test-sql-", null);
    Files.write(tempFile, "selec * from dual;".getBytes(StandardCharsets.UTF_8));

    ArrayList dbResponse = databaseHelper.executeSimpleScriptFile("DatabaseHelper", "localhost",
        2708, "sid", "jane", "****", "root", "****", tempFile.toAbsolutePath().toString());
    assertEquals(2, dbResponse.size());
    java.util.ArrayList row1 = (ArrayList) dbResponse.get(0);
    assertEquals("jane", row1.get(0));
  }

  @Test
  public void executeSimpleScriptFileForError() throws Exception {

    Connection connection = mock(Connection.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    ArrayList result = new ArrayList();
    result.add("adasd");

    doThrow(new Exception("Im a jerk")).when(sqlRunner).runScript("selec * from dual;");

    try {
      databaseHelper.executeSimpleScriptFile("DatabaseHelper", "localhost", 2708, "sid", "jane",
          "****", "root", "****", "/foo/bar/baz.sql");
      fail(
          "My method didn't throw when I expected it to: executeSimpleScriptString throw an error");
    } catch (Exception e) {
    }
  }

  @Test
  public void executeEmptyBlankScript() throws Exception {

    try {
      databaseHelper.executeSimpleScriptString("DatabaseHelper", "localhost", 2708, "sid", "jane",
          "****", "");
      fail("My method didn't throw when I expected it to: script is empty");
    } catch (Exception e) {
    }
  }
}
