package real.databases;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.junit.Test;
import org.usil.oss.devops.databaseops.DatabaseOpsCmdEntrypoint;

public class SqliteTest {

  public String getDatabaseFileLocation() throws Exception {
    String basePath = new File("").getAbsolutePath();
    String sqliteDbFilePath =
        basePath + File.separator + "src/test/resources/real_databases/sqlite/chinook.db";

    String dbForTest = System.getProperty("java.io.tmpdir") + File.separator
        + UUID.randomUUID().toString() + ".db";
    Files.copy(new File(sqliteDbFilePath).toPath(), new File(dbForTest).toPath(),
        StandardCopyOption.REPLACE_EXISTING);

    return dbForTest;
  }

  @Test
  public void severalObjects() throws Exception {
    String basePath = new File("").getAbsolutePath();
    
    String sqliteDbFilePath = getDatabaseFileLocation();
    String engine = "sqlite";
    String host = "";
    int port = 0;
    String sid = sqliteDbFilePath;
    String user = "";
    String password = "";
    
    //create two tables
    int tablesCountBefore =
        SQLiteJdbc.exec("SELECT * FROM sqlite_master WHERE type='table'", sqliteDbFilePath).size();
    
    String tablesScriptsFolder =
        basePath + File.separator + "src/test/resources/real_databases/sqlite/ddl_tables";

    String tablesCmdArguments = String.format(
        "--database_host=%s --database_port=%s "
            + "--database_name=%s --database_user=%s --database_password=%s "
            + "--scripts_folder=%s --engine=%s",
        host, port, sid, user, password, tablesScriptsFolder, engine);

    String[] tablesArgs = tablesCmdArguments.split("\\s+");
    DatabaseOpsCmdEntrypoint.main(tablesArgs);

    int tablesCountAfter =
        SQLiteJdbc.exec("SELECT * FROM sqlite_master WHERE type='table'", sqliteDbFilePath).size();

    assertEquals("two new tables were expected", tablesCountBefore + 2, tablesCountAfter);
    
    //create two views
    int viewsCountBefore =
        SQLiteJdbc.exec("SELECT * FROM sqlite_master WHERE type='view'", sqliteDbFilePath).size();
    
    String viewsScriptsFolder =
        basePath + File.separator + "src/test/resources/real_databases/sqlite/ddl_views";

    String viewsCmdArguments = String.format(
        "--database_host=%s --database_port=%s "
            + "--database_name=%s --database_user=%s --database_password=%s "
            + "--scripts_folder=%s --engine=%s --verbose_log",
        host, port, sid, user, password, viewsScriptsFolder, engine);

    String[] viewsArgs = viewsCmdArguments.split("\\s+");
    DatabaseOpsCmdEntrypoint.main(viewsArgs);

    int viewsCountAfter =
        SQLiteJdbc.exec("SELECT * FROM sqlite_master WHERE type='view'", sqliteDbFilePath).size();

    assertEquals("two new views were expected", viewsCountBefore + 2, viewsCountAfter);
    
  }
  

  
}
