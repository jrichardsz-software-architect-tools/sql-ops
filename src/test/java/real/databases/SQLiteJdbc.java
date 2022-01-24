package real.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.usil.oss.common.database.DatabaseHelper;

public class SQLiteJdbc {

  public static ArrayList<ArrayList<Object>> exec(String query, String absoluteDbPath) throws Exception {

    ArrayList<ArrayList<Object>> result = null;
    Connection c = null;
    PreparedStatement stmt = null;
    Class.forName("org.sqlite.JDBC");
    c = DriverManager.getConnection("jdbc:sqlite:"+absoluteDbPath);
    c.setAutoCommit(false);

    stmt = c.prepareStatement(query);
    boolean hasResults = stmt.execute();

    ResultSet rs = stmt.getResultSet();
    if (hasResults && rs != null) {
      result = DatabaseHelper.resulsetToArray(rs);
    }

    rs.close();
    stmt.close();
    c.close();

    return result;
  }

}
