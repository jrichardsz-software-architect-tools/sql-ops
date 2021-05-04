package org.usil.oss.common.ascii;

import java.util.ArrayList;

public class TableAscciHelper {
  private static String getRow(ArrayList<Object> row) {
    StringBuilder builder = new StringBuilder();
    for (Object i : row) {
      builder.append(i);
      builder.append("  ||");
    }
    builder.append("\n");
    return builder.toString();
  }

  @SuppressWarnings("unchecked")
  public static String createSimpleTable(ArrayList<?> data) {
    StringBuilder builder = new StringBuilder();

    for (Object row : data) {
      ArrayList<Object> cell = (ArrayList<Object>) row;
      builder.append(getRow(cell));
    }
    return builder.toString();
  }
}
