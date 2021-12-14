package org.usil.oss.common.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class DatabaseHelperTest {

  @Before
  public void databaseHelperConstructor() throws Exception {
    new DatabaseHelper();
  }

  @Test
  public void resulsetToArrayOnSuccess() throws Exception {
    ResultSet mockResultSet = MockResultSet.create(new String[] {"name", "lastname"},
        new String[][] {{"jane", "doe"}, {"kurt", "weller"}, {"william", "patterson"}});
    ArrayList<ArrayList<Object>> array = DatabaseHelper.resulsetToArray(mockResultSet);
    assertEquals(3, array.size());
    assertEquals(2, array.get(0).size());
    assertEquals(2, array.get(1).size());
    assertEquals(2, array.get(2).size());
  }

  @Test
  public void resulsetToArrayOnNullRs() throws Exception {
    try {
      DatabaseHelper.resulsetToArray(null);
      fail(
          "My method didn't throw when I expected it to: ResultSet is null");
    } catch (Exception e) {
    }
  }
}
