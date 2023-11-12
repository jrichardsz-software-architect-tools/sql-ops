package org.jrichardsz.oss.common.exception;

public class ExceptionHelper {
  public static String summarizeTrace(Throwable throwable, boolean pretty) {
    StringBuilder builder = new StringBuilder();
    while (throwable != null) {
      if (pretty)
        builder.append("\n\n"); 
      builder.append(
          String.format("Caused by: %s:%s ,", new Object[] { throwable.getClass(), throwable.getMessage() }));
      throwable = throwable.getCause();
    } 
    return builder.toString();
  }
}
