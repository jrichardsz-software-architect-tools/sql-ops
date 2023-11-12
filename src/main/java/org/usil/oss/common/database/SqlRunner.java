package org.usil.oss.common.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlRunner {

  private final boolean autoCommit;

  private final boolean stopOnError;

  private final Connection connection;

  private final Logger logger = LoggerFactory.getLogger(SqlRunner.class);

  public SqlRunner(Connection connection, boolean autoCommit, boolean stopOnError) {
    if (connection == null)
      throw new RuntimeException("SqlRunner requires an SQL Connection");
    this.connection = connection;
    this.autoCommit = autoCommit;
    this.stopOnError = stopOnError;
  }

  public ArrayList<ArrayList<Object>> runScript(String query) throws Exception {
    ArrayList<ArrayList<Object>> results = new ArrayList<>();
    boolean originalAutoCommit = this.connection.getAutoCommit();

    ArrayList<String> blocks = null;
    try {
      RawSqlParser parser = new RawSqlParser();
      blocks = new ArrayList<String>();
      blocks = parser.parseRawString(query);
      this.logger.info("blocks: " + blocks);
    } catch (Exception e) {
      throw new Exception("Failed to parse the raw sql", e);
    }

    if (originalAutoCommit != this.autoCommit)
      this.connection.setAutoCommit(this.autoCommit);


    for (String sqlBlock : blocks) {
      try {
        results.addAll(runDelimitedScriptBlock(this.connection, sqlBlock));
      } catch (Exception e) {
        if (this.stopOnError)
          throw e;
      }

    }

    this.connection.setAutoCommit(originalAutoCommit);

    return results;
  }

  private ArrayList<ArrayList<Object>> runDelimitedScriptBlock(Connection conn, String queryBlock)
      throws Exception {
    ArrayList<ArrayList<Object>> results = new ArrayList<>();

    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      this.logger.debug("block to execute:");
      this.logger.debug(queryBlock);
      boolean hasResults = false;
      hasResults = stmt.execute(queryBlock);
      this.logger.debug("[executed]");
      if (this.autoCommit && !conn.getAutoCommit())
        conn.commit();
      rs = stmt.getResultSet();
      if (hasResults && rs != null) {
        this.logger.debug("Query returned at least one value");
        results.addAll(DatabaseHelper.resulsetToArray(rs));
      } else {
        this.logger.debug("No results. Updated rows: " + stmt.getUpdateCount());
      }
    } catch (Exception e) {
      throw new Exception("Failed to execute this sql block: " + queryBlock, e);
    } finally {
      if (rs != null)
        try {
          rs.close();
        } catch (Exception e) {
          this.logger.error("Failed to close result: ", e);
        }
      if (stmt != null)
        try {
          stmt.close();
        } catch (Exception e) {
          this.logger.error("Failed to close statement: ", e);
        }
    }

    if (!this.autoCommit)
      conn.commit();
    return results;
  }
}
