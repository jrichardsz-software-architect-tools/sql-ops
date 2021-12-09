package org.usil.oss.common.string;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StringHelper {
  public static HashMap<String, String> severalKeyValuesInlineToMap(String rawVariables) {
    String rawPairs[] = rawVariables.split("\\s+");
    HashMap<String, String> params = new HashMap<String, String>();
    for (String rawPair : rawPairs) {
      String[] pair = rawPair.split("=");
      if (pair.length == 2 && pair[0] != null && pair[1] != null) {
        params.put(pair[0], pair[1]);
      }
    }
    return params;
  }

  public static List<String> splitByEmptyLines(String rawText) {
    return Arrays.asList(rawText.split("(?m)^\\s*$[\n\r]{1,}"));
  }
  
  public static boolean isComment(String rawText) {
    return rawText.matches("^\\s*--\\s+.+");
  }
  
  public static boolean isCommentWithCommand(String rawText) {
    return rawText.matches("^\\s*/\\*.+\\*/;\\s*");
  }
}
