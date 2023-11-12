package org.jrichardsz.oss.common.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.apache.commons.cli.CommandLine;
import org.junit.Test;

public class ArgumentsHelperTest {

  @Test
  public void containsAllArguments() throws Exception {
    String engine = "dummy2";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";
    String scriptsFolder = "/foo";

    String cmdArguments = String.format(
        "--database_host=%s --database_port=%s "
            + "--database_name=%s --database_user=%s --database_password=%s "
            + "--scripts_folder=%s --engine=%s",
        host, port, sid, user, password, scriptsFolder, engine);


    String[] args = cmdArguments.split("\\s+");

    ArgumentsHelper argumentsHelper = new ArgumentsHelper();
    CommandLine commandLine = argumentsHelper.getArguments(args);

    assertEquals(host, commandLine.getOptionValue("database_host"));
    assertEquals("" + port, commandLine.getOptionValue("database_port"));
    assertEquals(sid, commandLine.getOptionValue("database_name"));
    assertEquals(user, commandLine.getOptionValue("database_user"));
    assertEquals(password, commandLine.getOptionValue("database_password"));
    assertEquals(scriptsFolder, commandLine.getOptionValue("scripts_folder"));
    assertEquals(engine, commandLine.getOptionValue("engine"));
  }

  @Test
  public void failOnMissingArguments() throws Exception {
    String engine = "dummy2";
    String host = "192.168.0.17";
    int port = 3306;
    String sid = "deleteme";
    String user = "root";
    String password = "secret";
    String scriptsFolder = "/foo";

    String cmdArguments = String.format(
        "--database_host-xyz=%s --database_port=%s "
            + "--database_name=%s --database_user=%s --database_password=%s "
            + "--scripts_folder=%s --engine=%s",
        host, port, sid, user, password, scriptsFolder, engine);


    String[] args = cmdArguments.split("\\s+");

    ArgumentsHelper argumentsHelper = new ArgumentsHelper();

    try {
      argumentsHelper.getArguments(args);
      fail("My method didn't throw when I expected it to: missing required arguments");
    } catch (Exception e) {
    }

  }
}
