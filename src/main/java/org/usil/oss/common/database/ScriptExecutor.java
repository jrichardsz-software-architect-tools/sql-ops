package org.usil.oss.common.database;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.mysql.cj.util.StringUtils;

public class ScriptExecutor implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Execute queries composed by statements separated by ';'
   * 
   * @param sql
   * @throws Exception
   * @throws UnsupportedEncodingException
   * @throws IOException
   */
  public static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+",
      DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER", DEFAULT_DELIMITER = ";";
  private String delimiter = ScriptExecutor.DEFAULT_DELIMITER;

  public ArrayList<ArrayList<Object>> exec(String sql, Connection conn) throws Exception {

    SqlRunner sqlRunner = new SqlRunner(conn, true, true);

    return sqlRunner.runScript(sql);

    // String sqlPrune = sql.replaceAll("/\\*.*?\\*/;", "");
    // sqlPrune = sql.replaceAll("/\\*.*?\\*/", "");

    // ArrayList<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
    // get the statements
    // List<String> statements = getStatements(sql, conn);

    // SqlStatement stmt = new SqlStatement(conn, sql);
    // stmt.exec();

    // execute them in the order
    // for (String query : statements) {
    // SqlStatement stmt = new SqlStatement(conn, query);
    // System.out.println(">>>>>>>>>>>>>>>>>>"+query);
    //// Statement stmt = conn.createStatement();
    //// stmt.execute(query);
    // ArrayList<ArrayList<Object>> result = stmt.exec();
    // if (result != null) {
    // results.addAll(result);
    // }
    // }

    // return results;
  }

  /**
   * Parse the document and get sql statements
   * 
   * @param query
   * @return
   * @throws UnsupportedEncodingException
   * @throws IOException
   */
  private List<String> getStatements(String sql, Connection conn) {

    List<String> statements = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(sql, ";");

    while (st.hasMoreTokens()) {

      String query = st.nextToken().trim();
      String sqlPrune = query.replaceAll("/\\*.*?\\*/", "");
      if (StringUtils.isEmptyOrWhitespaceOnly(sqlPrune)) {
        continue;
      }

      statements.add(sqlPrune);
    }
    // statements.add(new SqlStatement(conn, sql));

    return statements;
  }
}
