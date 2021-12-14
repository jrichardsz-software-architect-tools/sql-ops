package org.usil.oss.common.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class DatabaseHelper {

  public static ArrayList<ArrayList<Object>> resulsetToArray(ResultSet rs) throws Exception {
    if(rs==null) {
      throw new Exception("Resulset is null");
    }
    ResultSetMetaData metaData = rs.getMetaData();
    int columns = metaData.getColumnCount();

    ArrayList<ArrayList<Object>> al = new ArrayList<ArrayList<Object>>();

    while (rs.next()) {
      ArrayList<Object> record = new ArrayList<Object>();

      for (int i = 1; i <= columns; i++) {
        Object value = rs.getObject(i);
        record.add(value);
      }
      al.add(record);
    }
    return al;
  }
}
