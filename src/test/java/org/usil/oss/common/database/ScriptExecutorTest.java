package org.usil.oss.common.database;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.junit.Test;

public class ScriptExecutorTest {

  @Test
  public void singleAndSimpleDDLScriptWithoutAnyError() throws Exception {

    Statement statement = mock(Statement.class);
    Connection connection = mock(Connection.class);

    when(connection.getAutoCommit()).thenReturn(true);
    when(connection.createStatement()).thenReturn(statement);
    doNothing().when(connection).commit();

    when(statement.execute(any(String.class))).thenAnswer(invocation -> true);

    ResultSet mockResultSet =
        MockResultSet.create(new String[] {"status"}, new String[][] {{"success"}});
    when(statement.getResultSet()).thenReturn(mockResultSet);

    String basePath = new File("").getAbsolutePath();
    String sqlFilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/ScriptExecutorTest/simple.sql";
    String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));

    ScriptExecutor scriptExecutor = new ScriptExecutor(true, true);
    ArrayList<ArrayList<?>> scriptResponse = scriptExecutor.runScript(connection, sqlString);
    assertEquals(1, scriptResponse.size());
    assertEquals(1, scriptResponse.get(0).size());
  }

  @Test
  public void simpleScriptWithDelimiterWithoutAnyError() throws Exception {

    Statement statement = mock(Statement.class);
    Connection connection = mock(Connection.class);

    when(connection.getAutoCommit()).thenReturn(true);
    when(connection.createStatement()).thenReturn(statement);
    doNothing().when(connection).commit();

    when(statement.execute(any(String.class))).thenAnswer(invocation -> true);

    ResultSet mockResultSet =
        MockResultSet.create(new String[] {"status"}, new String[][] {{"success"}});
    when(statement.getResultSet()).thenReturn(mockResultSet);

    String basePath = new File("").getAbsolutePath();
    String sqlFilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/ScriptExecutorTest/simpleWithDelimiter.sql";
    String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));

    ScriptExecutor scriptExecutor = new ScriptExecutor(true, true);
    ArrayList<ArrayList<?>> scriptResponse = scriptExecutor.runScript(connection, sqlString);
    assertEquals(1, scriptResponse.size());
    assertEquals(1, scriptResponse.get(0).size());
  }
}
