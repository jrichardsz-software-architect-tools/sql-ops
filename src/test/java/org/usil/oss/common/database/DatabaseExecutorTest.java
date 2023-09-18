package org.usil.oss.common.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
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
  private ScriptExecutor scriptExecutor;

  @InjectMocks
  private DatabaseExecutor databaseHelper;

  @Test
  public void executeSimpleScriptStringForSuccess() throws Exception {

    Connection connection = mock(Connection.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    ArrayList result = new ArrayList();
    result.add("success");

    doReturn(result).when(scriptExecutor).exec("selec * from dual;", connection);

    ArrayList dbResponse = databaseHelper.executeSimpleScriptString("DatabaseHelper", "localhost",
        2708, "sid", "jane", "****", "selec * from dual;");
    assertEquals(1, dbResponse.size());
    assertEquals("success", dbResponse.get(0));
  }

  @Test
  public void executeSimpleScriptStringForError() throws Exception {

    Connection connection = mock(Connection.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    ArrayList result = new ArrayList();
    result.add("adasd");

    doThrow(new Exception("Im a jerk")).when(scriptExecutor).exec("selec * from dual;", connection);

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

    ArrayList result = new ArrayList();
    result.add("success");

    Path tempFile = Files.createTempFile("databaseops-test-sql-", null);
    Files.write(tempFile, "selec * from dual;".getBytes(StandardCharsets.UTF_8));

    doReturn(result).when(scriptExecutor).exec("selec * from dual;", connection);

    ArrayList dbResponse = databaseHelper.executeSimpleScriptFile("DatabaseHelper", "localhost",
        2708, "sid", "jane", "****", "root", "****", tempFile.toAbsolutePath().toString());
    assertEquals(1, dbResponse.size());
    assertEquals("success", dbResponse.get(0));
  }

  @Test
  public void executeSimpleScriptFileForError() throws Exception {

    Connection connection = mock(Connection.class);

    when(connectionHelper.getConnection("DatabaseHelper", "localhost", 2708, "sid", "jane", "****"))
        .thenReturn(connection);

    ArrayList result = new ArrayList();
    result.add("adasd");

    doThrow(new Exception("Im a jerk")).when(scriptExecutor).exec("selec * from dual;", connection);

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
