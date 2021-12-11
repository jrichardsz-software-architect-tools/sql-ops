package org.usil.oss.common.string;

import java.util.Arrays;
import java.util.List;

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
}
