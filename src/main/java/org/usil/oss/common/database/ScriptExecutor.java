package org.usil.oss.common.database;

/*
 * Support for DELIMITER usage https://www.mysqltutorial.org/mysql-stored-procedure/mysql-delimiter/
 * https://www.postgresql.org/docs/8.1/sql-keywords-appendix.html
 * https://docs.oracle.com/cd/E57185_01/ESBTR/delimiter.html
 */

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
  private String delimiter = null;
  private boolean fullLineDelimiter = false;

  /**
   * 
   * @param connection
   * @param autoCommit
   * @param stopOnError
   */
  public ScriptExecutor(boolean autoCommit, boolean stopOnError) {
    this.autoCommit = autoCommit;
    this.stopOnError = stopOnError;
    this.delimiter = DEFAULT_DELIMITER;
  }

  /**
   * 
   * @param connection
   * @param autoCommit
   * @param stopOnError
   */
  public ScriptExecutor(boolean autoCommit, boolean stopOnError, String delimiter,
      boolean fullLineDelimiter) {
    this.autoCommit = autoCommit;
    this.stopOnError = stopOnError;
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
    // boolean originalAutoCommit = conn.getAutoCommit();
    // if (originalAutoCommit != this.autoCommit) {
    // conn.setAutoCommit(this.autoCommit);
    // }

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
        } else if (trimmedLine.startsWith("//")) {
          // Do nothing
        } else if (trimmedLine.startsWith("--")) {
          // Do nothing
        } else if (trimmedLine.length() < 1) {
          // Do nothing
        } else if (trimmedLine.startsWith("--")) {
          // Do nothing
        } // if contains delimiters
        else if (!fullLineDelimiter && trimmedLine.endsWith(this.delimiter)
            || fullLineDelimiter && trimmedLine.equals(this.delimiter)) {

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

          command.append(line.substring(0, line.lastIndexOf(this.delimiter)));
          command.append(" ");
          Statement statement = conn.createStatement();

          boolean hasResults = false;
          try {
            hasResults = statement.execute(command.toString());
          } catch (Exception e) {
            if (stopOnError) {
              throw e;
            }
          }

          if (this.autoCommit) {
            conn.commit();
          }

          ResultSet rs = statement.getResultSet();
          if (hasResults && rs != null) {
            results.addAll(DatabaseHelper.resulsetToArray(rs));
          }

          command = null;
          if (rs != null) {
            rs.close();
          }
          if (statement != null) {
            statement.close();
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
      // if (!this.autoCommit) {
      // conn.commit();
      // }
    } catch (SQLException e) {
      throw new Exception("Error executing: " + command, e);
    } catch (IOException e) {
      throw new Exception("Error executing: " + command, e);
    } finally {
      // conn.setAutoCommit(originalAutoCommit);
      if (conn != null) {
        conn.close();
      }

    }

    return results;
  }

  private void setDelimiter(String delimiter, boolean fullLineDelimiter) {
    this.delimiter = delimiter;
    this.fullLineDelimiter = fullLineDelimiter;
  }
}
