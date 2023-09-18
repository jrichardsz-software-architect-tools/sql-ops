package org.usil.oss.databaseops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.usil.oss.common.database.DatabaseExecutor;
import org.usil.oss.devops.databaseops.DatabaseOps;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class DatabaseOpsTest {

  @Mock
  private DatabaseExecutor databaseHelper;

  @InjectMocks
  private DatabaseOps databaseOps;

  @Test
  public void shouldFailOnMissingParameters() throws Exception {
    Exception excOnNull = null;
    try {
      databaseOps.perform(null, 0, "sid", "user", "password", "root", "changeme", "scriptsFolder",
          "engine", false);
    } catch (Exception e) {
      excOnNull = e;
    }
    assertNotNull("Exception should be throwed on null host", excOnNull);

    excOnNull = null;
    try {
      databaseOps.perform("foo", 0, null, "user", "password", "root", "changeme", "scriptsFolder",
          "engine", false);
    } catch (Exception e) {
      excOnNull = e;
    }
    assertNotNull("Exception should be throwed on null database name", excOnNull);

    excOnNull = null;
    try {
      databaseOps.perform("foo", 0, "foo", null, "password", "root", "changeme", "scriptsFolder",
          "engine", false);
    } catch (Exception e) {
      excOnNull = e;
    }
    assertNotNull("Exception should be throwed on null database user", excOnNull);

    excOnNull = null;
    try {
      databaseOps.perform("foo", 0, "foo", "foo", null, "root", "changeme", "scriptsFolder",
          "engine", false);
    } catch (Exception e) {
      excOnNull = e;
    }
    assertNotNull("Exception should be throwed on null database password", excOnNull);

    excOnNull = null;
    try {
      databaseOps.perform("foo", 0, "foo", "foo", "root", "changeme", "foo", null, "engine", false);
    } catch (Exception e) {
      excOnNull = e;
    }
    assertNotNull("Exception should be throwed on null scripts folder", excOnNull);

    excOnNull = null;
    try {
      databaseOps.perform("foo", 0, "foo", "foo", "foo", "root", "changeme", "foo", null, false);
    } catch (Exception e) {
      excOnNull = e;
    }
    assertNotNull("Exception should be throwed on null engine", excOnNull);


  }

  @Test
  public void shouldFailOnEmptyFolder() throws Exception {
    String emptyFolderPath =
        System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString();
    File emptyFolder = new File(emptyFolderPath);
    emptyFolder.mkdirs();

    Exception excOnNull = null;
    try {
      databaseOps.perform("foo", 0, "foo", "foo", "foo", "root", "changeme", emptyFolderPath,
          "mysql", false);
    } catch (Exception e) {
      excOnNull = e;
    }
    assertNotNull("Exception should be throwed on folder with zero scripts", excOnNull);
    emptyFolder.delete();
  }

  @Test
  public void successExecutionWithoutErrorCheckAndWithoutErrorsUsingParams() throws Exception {
    String engine = "dummy2";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/001.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/002.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest";

    HashMap<String, Object> executionDetails = databaseOps.perform(host, port, sid, user, password,
        "root", "changeme", scriptsFolder, engine, false);

    assertEquals(0, ((List<?>) executionDetails.get("afterErrors")).size());
    assertEquals(0, ((List<?>) executionDetails.get("beforeErrors")).size());
    assertEquals(3, ((List<?>) executionDetails.get("queryScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("rollbackScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("executedQueryScripts")).size());
    assertEquals(0, ((List<?>) executionDetails.get("executedRollbackScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("successOutputs")).size());
    assertEquals(0, ((List<?>) executionDetails.get("errorOutputs")).size());

  }

  @Test
  public void successExecutionWithoutErrorCheckAndWithoutErrors() throws Exception {
    String engine = "dummy2";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/001.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/002.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest";

    String cmdArguments = String.format(
        "--database_host=%s --database_port=%s "
            + "--database_name=%s --database_user=%s --database_password=%s "
            + "--scripts_folder=%s --engine=%s",
        host, port, sid, user, password, scriptsFolder, engine);
    String[] args = cmdArguments.split("\\s+");
    HashMap<String, Object> executionDetails = databaseOps.perform(args);

    assertEquals(0, ((List<?>) executionDetails.get("afterErrors")).size());
    assertEquals(0, ((List<?>) executionDetails.get("beforeErrors")).size());
    assertEquals(3, ((List<?>) executionDetails.get("queryScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("rollbackScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("executedQueryScripts")).size());
    assertEquals(0, ((List<?>) executionDetails.get("executedRollbackScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("successOutputs")).size());
    assertEquals(0, ((List<?>) executionDetails.get("errorOutputs")).size());

  }

  @Test
  public void successExecutionWithErrorCheckAndWithoutErrors() throws Exception {
    String engine = "dummy1";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/001.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/002.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest";

    String cmdArguments = String.format(
        "--database_host=%s --database_port=%s "
            + "--database_name=%s --database_user=%s --database_password=%s "
            + "--scripts_folder=%s --engine=%s",
        host, port, sid, user, password, scriptsFolder, engine);
    String[] args = cmdArguments.split("\\s+");
    HashMap<String, Object> executionDetails = databaseOps.perform(args);

    assertEquals(0, ((List<?>) executionDetails.get("afterErrors")).size());
    assertEquals(0, ((List<?>) executionDetails.get("beforeErrors")).size());
    assertEquals(3, ((List<?>) executionDetails.get("queryScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("rollbackScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("executedQueryScripts")).size());
    assertEquals(0, ((List<?>) executionDetails.get("executedRollbackScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("successOutputs")).size());
    assertEquals(0, ((List<?>) executionDetails.get("errorOutputs")).size());

  }

  @Test
  public void successExecutionWithErrorCheckAndThrowErrors() throws Exception {
    String engine = "dummy1";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    ArrayList<String> noErrors = new ArrayList<String>();
    ArrayList<String> twoErrors = new ArrayList<String>();
    twoErrors.add("error1");
    twoErrors.add("error2");

    Mockito.when(databaseHelper.executeSimpleScriptString(engine, host, port, sid, user, password,
        "show dummy1 errors;")).thenReturn(noErrors).thenReturn(twoErrors);

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/001.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/002.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        "root", "****", password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest";

    String cmdArguments = String.format("--database_host=%s --database_port=%s "
        + "--database_name=%s --database_user=%s --database_password=%s "
        + "--database_dba_user=%s --database_dba_password=%s " + "--scripts_folder=%s --engine=%s",
        host, port, sid, user, password, user, password, scriptsFolder, engine);
    String[] args = cmdArguments.split("\\s+");
    HashMap<String, Object> executionDetails = databaseOps.perform(args);

    assertEquals(2, ((List<?>) executionDetails.get("afterErrors")).size());
    assertEquals(0, ((List<?>) executionDetails.get("beforeErrors")).size());
    assertEquals(3, ((List<?>) executionDetails.get("queryScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("rollbackScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("executedQueryScripts")).size());
    assertEquals(0, ((List<?>) executionDetails.get("executedRollbackScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("successOutputs")).size());
    assertEquals(0, ((List<?>) executionDetails.get("errorOutputs")).size());

  }



  @Test
  public void errorOnFirstScriptWithErrorCheckAndNoErrorsWithVerboseLog() throws Exception {
    String engine = "dummy1";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doThrow(new Exception("Im a jerk")).when(databaseHelper).executeSimpleScriptFile(engine, host,
        port, sid, user, password, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/001.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/002.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest";

    String cmdArguments = String.format(
        "--database_host=%s --database_port=%s "
            + "--database_name=%s --database_user=%s --database_password=%s "
            + "--database_dba_user=%s --database_dba_password=%s "
            + "--scripts_folder=%s --engine=%s --verbose_log",
        host, port, sid, user, password, user, password, scriptsFolder, engine);
    String[] args = cmdArguments.split("\\s+");
    HashMap<String, Object> executionDetails = databaseOps.perform(args);

    assertEquals(0, ((List<?>) executionDetails.get("afterErrors")).size());
    assertEquals(0, ((List<?>) executionDetails.get("beforeErrors")).size());
    assertEquals(3, ((List<?>) executionDetails.get("queryScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("rollbackScripts")).size());
    assertEquals(0, ((List<?>) executionDetails.get("executedQueryScripts")).size());
    assertEquals(0, ((List<?>) executionDetails.get("executedRollbackScripts")).size());
    assertEquals(0, ((List<?>) executionDetails.get("successOutputs")).size());
    assertEquals(0, ((List<?>) executionDetails.get("rollbackErrorOutputs")).size());
    assertEquals(1, ((List<?>) executionDetails.get("errorOutputs")).size());

    // tostring assert
    assertNotNull("ExecutionMetadata tostring should be not null to show error info",
        executionDetails.toString());

  }

  @Test
  public void errorOnMiddleScriptWithErrorCheckAndNoErrors() throws Exception {
    String engine = "dummy1";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/001.sql");

    doThrow(new Exception("Im a jerk")).when(databaseHelper).executeSimpleScriptFile(engine, host,
        port, sid, user, password, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/002.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest";

    String cmdArguments = String.format("--database_host=%s --database_port=%s "
        + "--database_name=%s --database_user=%s --database_password=%s "
        + "--database_dba_user=%s --database_dba_password=%s " + "--scripts_folder=%s --engine=%s",
        host, port, sid, user, password, user, password, scriptsFolder, engine);

    String[] args = cmdArguments.split("\\s+");
    HashMap<String, Object> executionDetails = databaseOps.perform(args);

    assertEquals(0, ((List<?>) executionDetails.get("afterErrors")).size());
    assertEquals(0, ((List<?>) executionDetails.get("beforeErrors")).size());
    assertEquals(3, ((List<?>) executionDetails.get("queryScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("rollbackScripts")).size());
    assertEquals(1, ((List<?>) executionDetails.get("executedQueryScripts")).size());
    assertEquals(1, ((List<?>) executionDetails.get("executedRollbackScripts")).size());
    assertEquals(1, ((List<?>) executionDetails.get("successOutputs")).size());
    assertEquals(1, ((List<?>) executionDetails.get("errorOutputs")).size());

    assertEquals(true, ((List<String>) executionDetails.get("executedRollbackScripts")).get(0)
        .endsWith("001.sql.rollback"));
  }

  @Test
  public void errorOnLastScriptWithErrorCheckAndNoErrors() throws Exception {
    String engine = "dummy1";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/001.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/002.sql");

    doThrow(new Exception("Im a jerk")).when(databaseHelper).executeSimpleScriptFile(engine, host,
        port, sid, user, password, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/databaseops/DbvopsCmdEntrypointTest";

    String cmdArguments = String.format(
        "--database_host=%s --database_port=%s "
            + "--database_name=%s --database_user=%s --database_password=%s "
            + "--database_dba_user=%s --database_dba_password=%s "
            + "--scripts_folder=%s --engine=%s --verbose_log",
        host, port, sid, user, password, user, password, scriptsFolder, engine);

    cmdArguments = cmdArguments.replace("@scriptsFolder", scriptsFolder);
    String[] args = cmdArguments.split("\\s+");
    HashMap<String, Object> executionDetails = databaseOps.perform(args);

    assertEquals(0, ((List<?>) executionDetails.get("afterErrors")).size());
    assertEquals(0, ((List<?>) executionDetails.get("beforeErrors")).size());
    assertEquals(3, ((List<?>) executionDetails.get("queryScripts")).size());
    assertEquals(3, ((List<?>) executionDetails.get("rollbackScripts")).size());
    assertEquals(2, ((List<?>) executionDetails.get("executedQueryScripts")).size());
    assertEquals(2, ((List<?>) executionDetails.get("executedRollbackScripts")).size());
    assertEquals(2, ((List<?>) executionDetails.get("successOutputs")).size());
    assertEquals(1, ((List<?>) executionDetails.get("errorOutputs")).size());
    assertEquals(2, ((List<?>) executionDetails.get("rollbackSuccessOutputs")).size());

    assertEquals(true, ((List<String>) executionDetails.get("executedRollbackScripts")).get(0)
        .endsWith("002.sql.rollback"));
    assertEquals(true, ((List<String>) executionDetails.get("executedRollbackScripts")).get(1)
        .endsWith("001.sql.rollback"));
  }

}
