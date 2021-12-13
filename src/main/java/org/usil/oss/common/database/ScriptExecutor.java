package org.usil.oss.common.database;

/*
 * Added additional null checks when closing the ResultSet and Statements.
 *
 * Thanks to pihug12 and Grzegorz Oledzki at stackoverflow.com
 * http://stackoverflow.com/questions/5332149/jdbc-scriptrunner-java-lang-nullpointerexception?tab=
 * active#tab-top
 */
/*
 * Modified: Use logWriter in print(Object), JavaDoc comments, correct Typo.
 */
/*
 * Modified by Pantelis Sopasakis <chvng@mail.ntua.gr> to take care of DELIMITER statements. This
 * way you can execute scripts that contain some TRIGGER creation code. New version using REGEXPs!
 * Latest modification: Cater for a NullPointerException while parsing. Date: Feb 16, 2011, 11:48
 * EET
 */
/*
 * Slightly modified version of the com.ibatis.common.jdbc.ScriptRunner class from the iBATIS Apache
 * project. Only removed dependency on Resource class and a constructor
 */
/*
 * Copyright 2004 Clinton Begin Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tool to run database scripts. This version of the script can be found at
 * https://gist.github.com/gists/831762/
 */
public class ScriptExecutor {

  private static final String DEFAULT_DELIMITER = ";";
  private static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+";
  private static final String DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER";

  private final boolean stopOnError;
  private final boolean autoCommit;
  private String delimiter = DEFAULT_DELIMITER;
  private boolean fullLineDelimiter = false;

  /**
   * Default constructor.
   * 
   * @param connection
   * @param autoCommit
   * @param stopOnError
   */
  public ScriptExecutor(boolean autoCommit, boolean stopOnError) {
    this.autoCommit = autoCommit;
    this.stopOnError = stopOnError;
  }

  /**
   * @param delimiter
   * @param fullLineDelimiter
   */
  public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
    this.delimiter = delimiter;
    this.fullLineDelimiter = fullLineDelimiter;
  }

  /**
   * Runs an SQL script (read in using the Reader parameter) using the connection passed in.
   * 
   * @param conn - the connection to use for the script
   * @param reader - the source of the script
   * @throws Exception
   */
  public ArrayList runScript(Connection conn, String scriptString) throws Exception {
    StringReader reader = new StringReader(scriptString);
    boolean originalAutoCommit = conn.getAutoCommit();
    if (originalAutoCommit != autoCommit) {
      conn.setAutoCommit(autoCommit);
    }

    StringBuffer command = null;
    ArrayList<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
    try {
      LineNumberReader lineReader = new LineNumberReader(reader);
      String line = null;
      while ((line = lineReader.readLine()) != null) {
        if (command == null) {
          command = new StringBuffer();
        }
        String trimmedLine = line.trim();
        if (trimmedLine.startsWith("--")) {
          // Do nothing
        } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
          // Do nothing
        } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
          // Do nothing
        } else if (!fullLineDelimiter && trimmedLine.endsWith(getDelimiter())
            || fullLineDelimiter && trimmedLine.equals(getDelimiter())) {

          Pattern pattern = Pattern.compile(DELIMITER_LINE_REGEX);
          Matcher matcher = pattern.matcher(trimmedLine);
          if (matcher.matches()) {
            setDelimiter(trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim(),
                fullLineDelimiter);
            line = lineReader.readLine();
            if (line == null) {
              break;
            }
            trimmedLine = line.trim();
          }

          command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
          command.append(" ");
          Statement statement = conn.createStatement();

          boolean hasResults = false;
          if (stopOnError) {
            hasResults = statement.execute(command.toString());
          }

          if (autoCommit && !conn.getAutoCommit()) {
            conn.commit();
          }

          ResultSet rs = statement.getResultSet();
          if (hasResults && rs != null) {

            results.addAll(results2Array(rs));

          }

          command = null;
          try {
            if (rs != null) {
              rs.close();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          try {
            if (statement != null) {
              statement.close();
            }
          } catch (Exception e) {
            e.printStackTrace();
            // Ignore to workaround a bug in Jakarta DBCP
          }
        } else {
          Pattern pattern = Pattern.compile(DELIMITER_LINE_REGEX);
          Matcher matcher = pattern.matcher(trimmedLine);
          if (matcher.matches()) {
            setDelimiter(trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim(),
                fullLineDelimiter);
            line = lineReader.readLine();
            if (line == null) {
              break;
            }
            trimmedLine = line.trim();
          }
          command.append(line);
          command.append(" ");
        }
      }
      if (!autoCommit) {
        conn.commit();
      }
    } catch (SQLException e) {
      throw new Exception("Error executing: " + command, e);
    } catch (IOException e) {
      throw new Exception("Error executing: " + command, e);
    } finally {
      conn.setAutoCommit(originalAutoCommit);
    }

    return results;
  }

  private String getDelimiter() {
    return delimiter;
  }

  private ArrayList<ArrayList<Object>> results2Array(ResultSet rs) throws SQLException {
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
