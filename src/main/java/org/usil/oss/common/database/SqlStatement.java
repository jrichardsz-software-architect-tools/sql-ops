package org.usil.oss.common.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SqlStatement {
  private Connection con;
  
  private String query;
  
  public SqlStatement(Connection con, String query) {
    this.con = con;
    this.query = query;
  }
  
  public ArrayList<ArrayList<Object>> exec() throws Exception {
    ArrayList<ArrayList<Object>> result = null;
    try (PreparedStatement stmt = this.con.prepareStatement(this.query)) {
      boolean hasResults = stmt.execute();
      ResultSet rs = stmt.getResultSet();
      if (hasResults && rs != null)
        result = DatabaseHelper.resulsetToArray(rs); 
      stmt.close();
      return result;
    } 
  }
}
