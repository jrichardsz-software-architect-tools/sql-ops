package org.usil.oss.common.logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerHelper {

  public static void setDebugLevel() {
    Logger rootLogger = LogManager.getLogManager().getLogger("");
    rootLogger.setLevel(Level.FINE);
    for (Handler h : rootLogger.getHandlers()) {
      System.out.println(h.getClass());
      h.setLevel(Level.FINE);
    }
  }

}
