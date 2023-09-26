package real.databases;

import org.usil.oss.devops.databaseops.DatabaseOps;

public class MysqlIntegrationTest {

  public static void main(String args[]) throws Exception {

    String engine = "mysql";
    String host = System.getenv("host");
    int port = 3306;
    String sid = System.getenv("db_name");
    String user = System.getenv("simple_user");
    String password = System.getenv("simple_password");
    String root = System.getenv("root_user");
    String rootPassword = System.getenv("root_password");

    String objectsScriptsFolder = "/home/rleoni/Job/code/database_scripts";

    String cmdArguments = String.format(
        "--database_host=%s --database_port=%s "
            + "--database_name=%s --database_user=%s --database_password=%s "
            + "--database_dba_user=%s --database_dba_password=%s "
            + "--scripts_folder=%s --engine=%s --verbose_log",
        host, port, sid, user, password, root, rootPassword, objectsScriptsFolder, engine);

    String[] tablesArgs = cmdArguments.split("\\s+");
    DatabaseOps databaseOps = new DatabaseOps();
    String status = (String) databaseOps.perform(tablesArgs).get("status");
    if (!status.contentEquals("success")) {
      throw new Exception("mysql: simple dump failed");
    }
  }
}
