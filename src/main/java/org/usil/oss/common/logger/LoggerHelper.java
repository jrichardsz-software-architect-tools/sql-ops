package org.usil.oss.common.logger;

import java.util.UUID;

public class LoggerHelper {

  public static void setDebugLevel() {
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
        .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
    root.setLevel(ch.qos.logback.classic.Level.DEBUG);
  }

  public static void initialize() {
    System.setProperty("log.name", "database-ops-" + UUID.randomUUID().toString() + ".log");
    System.setProperty("log.folder", System.getProperty("java.io.tmpdir"));
  }
}
