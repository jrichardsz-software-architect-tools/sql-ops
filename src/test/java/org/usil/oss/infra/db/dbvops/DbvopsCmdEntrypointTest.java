package org.usil.oss.infra.db.dbvops;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import java.io.File;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.usil.oss.common.database.DatabaseHelper;
import org.usil.oss.common.model.ExecutionMetadata;

@RunWith(MockitoJUnitRunner.class)
public class DbvopsCmdEntrypointTest {

  @Mock
  private DatabaseHelper databaseHelper;

  @InjectMocks
  private DbvopsCmdEntrypoint cmdEntrypoint;

  @Test
  public void successExecution() throws Exception {
    String engine = "mysql";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/001.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/002.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest";

    String cmdArguments = "--database_host=192.168.0.17 " + "--database_port=3306 "
        + "--database_name=deleteme " + "--database_user=root " + "--database_password=secret "
        + "--scripts_folder=@scriptsFolder " + "--engine=mysql --verbose_log";// +"--verbose_log"
    cmdArguments = cmdArguments.replace("@scriptsFolder", scriptsFolder);
    String[] args = cmdArguments.split("\\s+");
    ExecutionMetadata executionMetadata = cmdEntrypoint.perform(args);

    assertEquals(0, executionMetadata.getAfterErrors().size());
    assertEquals(0, executionMetadata.getBeforeErrors().size());
    assertEquals(3, executionMetadata.getQueryScripts().size());
    assertEquals(3, executionMetadata.getRollbackScripts().size());
    assertEquals(3, executionMetadata.getExecutedQueryScripts().size());
    assertEquals(0, executionMetadata.getExecutedRollbackScripts().size());
    assertEquals(3, executionMetadata.getSuccessOutputs().size());
    assertEquals(0, executionMetadata.getErrorOutputs().size());

  }

  @Test
  public void firstScriptError() throws Exception {
    String engine = "mysql";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doThrow(new Exception("Im a jerk")).when(databaseHelper).executeSimpleScriptFile(engine, host,
        port, sid, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/001.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/002.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest";

    String cmdArguments = "--database_host=192.168.0.17 " + "--database_port=3306 "
        + "--database_name=deleteme " + "--database_user=root " + "--database_password=secret "
        + "--scripts_folder=@scriptsFolder " + "--engine=mysql --verbose_log";// +"--verbose_log"
    cmdArguments = cmdArguments.replace("@scriptsFolder", scriptsFolder);
    String[] args = cmdArguments.split("\\s+");
    ExecutionMetadata executionMetadata = cmdEntrypoint.perform(args);

    assertEquals(0, executionMetadata.getAfterErrors().size());
    assertEquals(0, executionMetadata.getBeforeErrors().size());
    assertEquals(3, executionMetadata.getQueryScripts().size());
    assertEquals(3, executionMetadata.getRollbackScripts().size());
    assertEquals(0, executionMetadata.getExecutedQueryScripts().size());
    assertEquals(0, executionMetadata.getExecutedRollbackScripts().size());
    assertEquals(0, executionMetadata.getSuccessOutputs().size());
    assertEquals(0, executionMetadata.getErrorOutputs().size());

  }

  @Test
  public void middleScriptError() throws Exception {
    String engine = "mysql";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/001.sql");

    doThrow(new Exception("Im a jerk")).when(databaseHelper).executeSimpleScriptFile(engine, host,
        port, sid, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/002.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest";

    String cmdArguments = "--database_host=192.168.0.17 " + "--database_port=3306 "
        + "--database_name=deleteme " + "--database_user=root " + "--database_password=secret "
        + "--scripts_folder=@scriptsFolder " + "--engine=mysql --verbose_log";// +"--verbose_log"
    cmdArguments = cmdArguments.replace("@scriptsFolder", scriptsFolder);
    String[] args = cmdArguments.split("\\s+");
    ExecutionMetadata executionMetadata = cmdEntrypoint.perform(args);

    assertEquals(0, executionMetadata.getAfterErrors().size());
    assertEquals(0, executionMetadata.getBeforeErrors().size());
    assertEquals(3, executionMetadata.getQueryScripts().size());
    assertEquals(3, executionMetadata.getRollbackScripts().size());
    assertEquals(1, executionMetadata.getExecutedQueryScripts().size());
    assertEquals(1, executionMetadata.getExecutedRollbackScripts().size());
    assertEquals(1, executionMetadata.getSuccessOutputs().size());
    assertEquals(1, executionMetadata.getErrorOutputs().size());

    assertEquals(true,
        executionMetadata.getExecutedRollbackScripts().get(0).endsWith("001.sql.rollback"));
  }

  @Test
  public void lastScriptError() throws Exception {
    String engine = "mysql";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";

    ArrayList<String> result = new ArrayList<String>();
    result.add("success");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/001.sql");

    doReturn(result).when(databaseHelper).executeSimpleScriptFile(engine, host, port, sid, user,
        password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/002.sql");

    doThrow(new Exception("Im a jerk")).when(databaseHelper).executeSimpleScriptFile(engine, host,
        port, sid, user, password, new File("").getAbsolutePath()
            + "/src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest/003.sql");

    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator
        + "src/test/resources/org/usil/oss/infra/db/dbvops/DbvopsCmdEntrypointTest";

    String cmdArguments = "--database_host=192.168.0.17 " + "--database_port=3306 "
        + "--database_name=deleteme " + "--database_user=root " + "--database_password=secret "
        + "--scripts_folder=@scriptsFolder " + "--engine=mysql --verbose_log";// +"--verbose_log"
    cmdArguments = cmdArguments.replace("@scriptsFolder", scriptsFolder);
    String[] args = cmdArguments.split("\\s+");
    ExecutionMetadata executionMetadata = cmdEntrypoint.perform(args);

    assertEquals(0, executionMetadata.getAfterErrors().size());
    assertEquals(0, executionMetadata.getBeforeErrors().size());
    assertEquals(3, executionMetadata.getQueryScripts().size());
    assertEquals(3, executionMetadata.getRollbackScripts().size());
    assertEquals(2, executionMetadata.getExecutedQueryScripts().size());
    assertEquals(2, executionMetadata.getExecutedRollbackScripts().size());
    assertEquals(2, executionMetadata.getSuccessOutputs().size());
    assertEquals(2, executionMetadata.getErrorOutputs().size());

    assertEquals(true,
        executionMetadata.getExecutedRollbackScripts().get(0).endsWith("002.sql.rollback"));
    assertEquals(true,
        executionMetadata.getExecutedRollbackScripts().get(1).endsWith("001.sql.rollback"));
  }
}
