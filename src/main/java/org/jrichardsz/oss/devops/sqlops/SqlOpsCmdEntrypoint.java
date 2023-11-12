package org.jrichardsz.oss.devops.sqlops;

public class SqlOpsCmdEntrypoint {
  private static SqlOps databaseOps = new SqlOps();

  public static void main(String[] args) throws Exception {
    databaseOps.perform(args);
  }
}
