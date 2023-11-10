package org.usil.oss.common.database;

import com.mysql.cj.util.StringUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ScriptExecutor implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+";
  
  public static final String DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER";
  
  public static final String DEFAULT_DELIMITER = ";";
  
  private String delimiter = ";";
  
  public ArrayList<ArrayList<Object>> exec(String sql, Connection conn) throws Exception {
    SqlRunner sqlRunner = new SqlRunner(conn, true, true);
    return sqlRunner.runScript(sql);
  }
  
  private List<String> getStatements(String sql, Connection conn) {
    List<String> statements = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(sql, ";");
    while (st.hasMoreTokens()) {
      String query = st.nextToken().trim();
      String sqlPrune = query.replaceAll("/\\*.*?\\*/", "");
      if (StringUtils.isEmptyOrWhitespaceOnly(sqlPrune))
        continue; 
      statements.add(sqlPrune);
    } 
    return statements;
  }
}
