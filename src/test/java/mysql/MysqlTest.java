package mysql;

import java.io.File;
import org.junit.Test;
import org.usil.oss.infra.db.dbvops.DbvopsCmdEntrypoint;

public class MysqlTest {

  @Test
  public void successExecution() throws Exception {
    String basePath = new File("").getAbsolutePath();
    String scriptsFolder = basePath + File.separator + "src/test/resources/mysql/success";
    String cmdArguments = "--database_host=192.168.0.14 " + "--database_port=3306 "
        + "--database_name=world " + "--database_user=root " + "--database_password=secret "
        + "--scripts_folder=@scriptsFolder " + "--engine=mysql ";// +"--verbose_log"
    cmdArguments = cmdArguments.replace("@scriptsFolder", scriptsFolder);
    String[] args = cmdArguments.split("\s+");
    DbvopsCmdEntrypoint.main(args);
  }


}

