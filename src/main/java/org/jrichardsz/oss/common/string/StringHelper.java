package org.jrichardsz.oss.common.string;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
  public static List<String> splitByEmptyLines(String rawText) {
    return Arrays.asList(rawText.split("(?m)^\\s*$[\n\r]{1,}"));
  }

  public static boolean isComment(String rawText) {
    return rawText.matches("^\\s*--\\s+.+");
  }

  public static boolean isCommentWithCommand(String rawText) {
    return rawText.matches("^\\s*/\\*.+\\*/;\\s*");
  }

  public static Map<String, String> getMetadataScript(String rawSql) throws Exception {
    Map<String, String> attr = new HashMap<>();
    Matcher m1 = Pattern.compile("^.*").matcher(rawSql);
    if (!m1.find())
      return attr;
    String firstLine = m1.group();
    if (firstLine.trim().length() == 0)
      return attr;
    Matcher m = Pattern.compile("(\\w+)=(.*?)(?=\\w+=|$)").matcher(firstLine);
    while (m.find())
      attr.put(m.group(1).trim(), m.group(2).trim());
    return attr;
  }
}
