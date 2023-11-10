package org.usil.oss.common.database;

import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlRunner {
  public static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+";
  
  public static final String DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER";
  
  public static final String DEFAULT_DELIMITER = ";";
  
  private final boolean autoCommit;
  
  private final boolean stopOnError;
  
  private final Connection connection;
  
  private String delimiter = ";";
  
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
    try {
      if (originalAutoCommit != this.autoCommit)
        this.connection.setAutoCommit(this.autoCommit); 
      results.addAll(runScript(this.connection, new StringReader(query)));
    } finally {
      this.connection.setAutoCommit(originalAutoCommit);
    } 
    return results;
  }
  
  private ArrayList<ArrayList<Object>> runScript(Connection conn, Reader reader) throws Exception {
    StringBuffer command = null;
    ArrayList<ArrayList<Object>> results = new ArrayList<>();
    LineNumberReader lineReader = new LineNumberReader(reader);
    String line = null;
    while ((line = lineReader.readLine()) != null) {
      if (command == null)
        command = new StringBuffer(); 
      String trimmedLine = line.trim();
      this.logger.debug("initial line: " + trimmedLine);
      if (trimmedLine.endsWith(this.delimiter)) {
        this.logger.debug("Line ends with delimiter");
        Pattern pattern1 = Pattern.compile("(?i)DELIMITER.+");
        Matcher matcher1 = pattern1.matcher(trimmedLine);
        if (matcher1.matches()) {
          this.delimiter = trimmedLine.split("(?i)DELIMITER")[1].trim();
          line = lineReader.readLine();
          if (line == null)
            break; 
          trimmedLine = line.trim();
        } 
        this.logger.debug("new line:" + line);
        if (line.lastIndexOf(this.delimiter) < 0) {
          command.append(line);
          continue;
        } 
        this.logger.debug("append:" + line.substring(0, line.lastIndexOf(this.delimiter)));
        command.append(line.substring(0, line.lastIndexOf(this.delimiter)));
        command.append(" ");
        Statement stmt = null;
        ResultSet rs = null;
        try {
          stmt = conn.createStatement();
          this.logger.debug("Final block to execute:");
          this.logger.debug(command.toString());
          boolean hasResults = false;
          if (this.stopOnError) {
            hasResults = stmt.execute(command.toString());
          } else {
            stmt.execute(command.toString());
          } 
          this.logger.debug("[executed]");
          this.delimiter = ";";
          if (this.autoCommit && !conn.getAutoCommit())
            conn.commit(); 
          rs = stmt.getResultSet();
          if (hasResults && rs != null) {
            results.addAll(DatabaseHelper.resulsetToArray(rs));
          } else {
            this.logger.debug("Updated rows: " + stmt.getUpdateCount());
          } 
          command = null;
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
        continue;
      } 
      this.logger.debug("Line is middle of a statement");
      Pattern pattern = Pattern.compile("(?i)DELIMITER.+");
      Matcher matcher = pattern.matcher(trimmedLine);
      if (matcher.matches()) {
        this.delimiter = trimmedLine.split("(?i)DELIMITER")[1].trim();
        line = lineReader.readLine();
        if (line == null)
          break; 
        trimmedLine = line.trim();
      } 
      command.append(line);
      command.append("\n");
    } 
    if (!this.autoCommit)
      conn.commit(); 
    return results;
  }
}
