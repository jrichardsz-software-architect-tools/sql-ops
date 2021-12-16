package org.usil.oss.common.database;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.junit.Test;

public class ScriptExecutorTest {

  @Test
  public void singleAndSimpleDDLScriptWithResultWithoutAnyError() throws Exception {

    String basePath = new File("").getAbsolutePath();
    String sqlFilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/ScriptExecutorTest/simple.sql";
    String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));

    PreparedStatement statement = mock(PreparedStatement.class);
    Connection connection = mock(Connection.class);

    when(connection.getAutoCommit()).thenReturn(true);
    when(connection.prepareStatement(sqlString.split(";")[0])).thenReturn(statement);
    doNothing().when(connection).commit();

    // when(statement.execute(any(String.class))).thenAnswer(invocation -> true);
    when(statement.execute()).thenReturn(true);

    ResultSet mockResultSet =
        MockResultSet.create(new String[] {"status"}, new String[][] {{"success"}});
    when(statement.getResultSet()).thenReturn(mockResultSet);

    ScriptExecutor scriptExecutor = new ScriptExecutor();

    // hasResult and resultset != null
    ArrayList<ArrayList<Object>> scriptResponse = scriptExecutor.exec(sqlString, connection);
    assertEquals(1, scriptResponse.size());
    assertEquals(1, scriptResponse.get(0).size());
    assertEquals("success", scriptResponse.get(0).get(0));

    // hasResult and resultset == null
    when(statement.getResultSet()).thenReturn(null);
    ArrayList<ArrayList<Object>> scriptResponse2 = scriptExecutor.exec(sqlString, connection);
    assertEquals(0, scriptResponse2.size());
  }

  @Test
  public void singleAndSimpleDDLScriptWithoutResultAndAnyError() throws Exception {
    
    String basePath = new File("").getAbsolutePath();
    String sqlFilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/ScriptExecutorTest/simple.sql";
    String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));
    
    PreparedStatement statement = mock(PreparedStatement.class);
    Connection connection = mock(Connection.class);
    
    when(connection.getAutoCommit()).thenReturn(true);
    when(connection.prepareStatement(sqlString.split(";")[0])).thenReturn(statement);
    doNothing().when(connection).commit();
    
    when(statement.execute()).thenReturn(false);
    when(statement.getResultSet()).thenReturn(null);
    
    ScriptExecutor scriptExecutor = new ScriptExecutor();
    ArrayList<ArrayList<Object>> scriptResponse = scriptExecutor.exec(sqlString, connection);
    assertEquals(0, scriptResponse.size());
  }

  @Test
  public void singleAndSimpleDDLScriptMixEmptyNoEmptyWithoutResultNoErrors() throws Exception {

    String basePath = new File("").getAbsolutePath();
    String sqlFilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/ScriptExecutorTest/mix_empty_no_empty.sql";
    String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));

    PreparedStatement statement = mock(PreparedStatement.class);
    Connection connection = mock(Connection.class);

    when(connection.getAutoCommit()).thenReturn(true);
    when(connection.prepareStatement(any(String.class))).thenReturn(statement);
    doNothing().when(connection).commit();

    when(statement.execute()).thenReturn(false);
    when(statement.getResultSet()).thenReturn(null);

    ScriptExecutor scriptExecutor = new ScriptExecutor();
    ArrayList<ArrayList<Object>> scriptResponse = scriptExecutor.exec(sqlString, connection);
    assertEquals(0, scriptResponse.size());
  }


}
