package org.jrichardsz.oss.common.database;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.Test;

public class RawMysqlSqlParserTest {

  @Test
  public void multipleMyqlTriggers() throws Exception {

    String basePath = new File("").getAbsolutePath();
    String sqlFilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/RawMysqlSqlParserTest/multipleMysqlTriggers.sql";
    String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));

    RawSqlParser parser = new RawSqlParser();
    ArrayList<String> blocks = new ArrayList<String>();
    blocks = parser.parseRawString(sqlString);
    // assert quantity
    assertEquals(2, blocks.size());
    // assert block1 values
    String[] block1 = blocks.get(0).split("\\n");
    String expectedBlock1FilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/RawMysqlSqlParserTest/ExpectedBlock1.txt";
    String expectedBlock1String = new String(Files.readAllBytes(Paths.get(expectedBlock1FilePath)));
    String[] expectedBlock1 = expectedBlock1String.split("\\n");
    for (int i = 0; i < expectedBlock1.length; i++) {
      assertEquals(expectedBlock1[i], block1[i]);
    }

    // assert block2 values
    String[] block2 = blocks.get(1).split("\\n");
    String expectedBlock2FilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/RawMysqlSqlParserTest/ExpectedBlock2.txt";
    String expectedBlock2String = new String(Files.readAllBytes(Paths.get(expectedBlock2FilePath)));
    String[] expectedBlock2 = expectedBlock2String.split("\\n");
    for (int i = 0; i < expectedBlock2.length; i++) {
      assertEquals(expectedBlock2[i], block2[i]);
    }
  }
  
  // The script 
  // resources/org/usil/oss/common/database/RawMysqlSqlParserTest/mysqlDumpWithSpaceAtTheEnd.sql
  // in the line 3, ends without a space
  @Test
  public void mysqlDumpWithSpaceAtTheEnd() throws Exception {

    String basePath = new File("").getAbsolutePath();
    String sqlFilePath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/database/RawMysqlSqlParserTest/mysqlDumpWithSpaceAtTheEnd.sql";
    String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));

    RawSqlParser parser = new RawSqlParser();
    ArrayList<String> blocks = new ArrayList<String>();
    blocks = parser.parseRawString(sqlString);
    
    assertEquals(4, blocks.size());
    
    String[] expectedBlock2 = blocks.get(1).split("\\n");
    assertEquals("/*!50003 CREATE*/ /*!50017 */ /*!50003 TRIGGER before_user_profiles_modified", expectedBlock2[0]);
    assertEquals("BEFORE UPDATE ON `user_profiles`", expectedBlock2[1]);
    
    String[] expectedBlock4 = blocks.get(3).split("\\n");
    
    assertEquals("/*!50003 CREATE*/ /*!50017 */ /*!50003 TRIGGER after_user_profiles_modified", expectedBlock4[0]);
    assertEquals("AFTER UPDATE ON `user_profiles`", expectedBlock4[1]);      
  }  
  
  // @Test
  // public void severalDmlScriptWithResultWithoutAnyError() throws Exception {
  //
  // String basePath = new File("").getAbsolutePath();
  // String sqlFilePath = basePath + File.separator
  // + "src/test/resources/org/usil/oss/common/database/ScriptExecutorTest/multiple.sql";
  // String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));
  //
  // PreparedStatement statement = mock(PreparedStatement.class);
  // Connection connection = mock(Connection.class);
  //
  // doReturn(statement).when(connection).prepareStatement(any(String.class));
  //
  // doNothing().when(connection).commit();
  //
  // when(statement.execute()).thenReturn(true);
  //
  // ResultSet mockResultSet1 =
  // MockResultSet.create(new String[] {"status"}, new String[][] {{"success1"}});
  // ResultSet mockResultSet2 =
  // MockResultSet.create(new String[] {"status"}, new String[][] {{"success2"}});
  // ResultSet mockResultSet3 =
  // MockResultSet.create(new String[] {"status"}, new String[][] {{"success3"}});
  // when(statement.getResultSet()).thenReturn(mockResultSet1).thenReturn(mockResultSet2)
  // .thenReturn(mockResultSet3);
  //
  // ScriptExecutor scriptExecutor = new ScriptExecutor();
  //
  // // hasResult and resultset != null
  // ArrayList<ArrayList<Object>> scriptResponse = scriptExecutor.exec(sqlString, connection);
  // assertEquals(3, scriptResponse.size());
  // assertEquals(1, scriptResponse.get(0).size());
  // assertEquals(1, scriptResponse.get(1).size());
  // assertEquals(1, scriptResponse.get(2).size());
  // assertEquals("success1", scriptResponse.get(0).get(0));
  // assertEquals("success2", scriptResponse.get(1).get(0));
  // assertEquals("success3", scriptResponse.get(2).get(0));
  // }
  //
  // @Test
  // public void singleAndSimpleDDLScriptWithoutResultAndAnyError() throws Exception {
  //
  // String basePath = new File("").getAbsolutePath();
  // String sqlFilePath = basePath + File.separator
  // + "src/test/resources/org/usil/oss/common/database/ScriptExecutorTest/simple.sql";
  // String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));
  //
  // PreparedStatement statement = mock(PreparedStatement.class);
  // Connection connection = mock(Connection.class);
  //
  // when(connection.getAutoCommit()).thenReturn(true);
  // when(connection.prepareStatement(sqlString.split(";")[0])).thenReturn(statement);
  // doNothing().when(connection).commit();
  //
  // when(statement.execute()).thenReturn(false);
  // when(statement.getResultSet()).thenReturn(null);
  //
  // ScriptExecutor scriptExecutor = new ScriptExecutor();
  // ArrayList<ArrayList<Object>> scriptResponse = scriptExecutor.exec(sqlString, connection);
  // assertEquals(0, scriptResponse.size());
  // }
  //
  // @Test
  // public void singleAndSimpleDDLScriptMixEmptyNoEmptyWithoutResultNoErrors() throws Exception {
  //
  // String basePath = new File("").getAbsolutePath();
  // String sqlFilePath = basePath + File.separator
  // + "src/test/resources/org/usil/oss/common/database/ScriptExecutorTest/mix_empty_no_empty.sql";
  // String sqlString = new String(Files.readAllBytes(Paths.get(sqlFilePath)));
  //
  // PreparedStatement statement = mock(PreparedStatement.class);
  // Connection connection = mock(Connection.class);
  //
  // when(connection.getAutoCommit()).thenReturn(true);
  // when(connection.prepareStatement(any(String.class))).thenReturn(statement);
  // doNothing().when(connection).commit();
  //
  // when(statement.execute()).thenReturn(false);
  // when(statement.getResultSet()).thenReturn(null);
  //
  // ScriptExecutor scriptExecutor = new ScriptExecutor();
  // ArrayList<ArrayList<Object>> scriptResponse = scriptExecutor.exec(sqlString, connection);
  // assertEquals(0, scriptResponse.size());
  // }


}
