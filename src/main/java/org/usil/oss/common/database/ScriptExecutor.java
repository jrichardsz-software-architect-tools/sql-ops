package org.usil.oss.common.database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.mysql.cj.util.StringUtils;

public class ScriptExecutor {

  /**
   * Execute queries composed by statements separated by ';'
   * 
   * @param sql
   * @throws Exception
   * @throws UnsupportedEncodingException
   * @throws IOException
   */
  public ArrayList<ArrayList<Object>> exec(String sql, Connection conn) throws Exception {

    ArrayList<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
    // get the statements
    List<SQLStatement> statements = getStatements(sql, conn);

    // execute them in the order
    for (SQLStatement stmt : statements) {
      ArrayList<ArrayList<Object>> result = stmt.exec();
      if (result != null) {
        results.addAll(result);
      }
    }

    return results;
  }

  /**
   * Parse the document and get sql statements
   * 
   * @param query
   * @return
   * @throws UnsupportedEncodingException
   * @throws IOException
   */
  private List<SQLStatement> getStatements(String sql, Connection conn) {

    List<SQLStatement> statements = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(sql, ";");

    while (st.hasMoreTokens()) {

      String query = st.nextToken().trim();
      if (StringUtils.isEmptyOrWhitespaceOnly(query)) {
        continue;
      }
      statements.add(new SQLStatement(conn, query));
    }

    return statements;
  }
}
