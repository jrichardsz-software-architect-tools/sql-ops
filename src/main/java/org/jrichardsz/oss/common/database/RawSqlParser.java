package org.jrichardsz.oss.common.database;

import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawSqlParser {
  public static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+";

  public static final String DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER";

  public static final String DEFAULT_DELIMITER = ";";

  private String delimiter = ";";

  private final Logger logger = LoggerFactory.getLogger(RawSqlParser.class);

  public ArrayList<String> parseRawString(String query) throws Exception {
    ArrayList<String> sqlBlocks = parseRawString(new StringReader(query));
    return sqlBlocks;
  }

  private ArrayList<String> parseRawString(Reader reader) throws Exception {
    StringBuffer command = null;
    ArrayList<String> sqlBlocks = new ArrayList<>();
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
        String computedRawSql = command.toString();
        this.logger.debug("block: " + computedRawSql.trim());
        sqlBlocks.add(computedRawSql.trim());
        this.delimiter = ";";
        command = null;
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

    return sqlBlocks;
  }
}
