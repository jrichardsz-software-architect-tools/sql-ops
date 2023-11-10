package org.usil.oss.devops.databaseops;

public class DatabaseOpsCmdEntrypoint {
  private static DatabaseOps databaseOps = new DatabaseOps();
  
  public static void main(String[] args) throws Exception {
    databaseOps.perform(args);
  }
}
