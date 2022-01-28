package org.usil.oss.common.logger;

import java.io.File;
import java.util.UUID;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class LoggerHelper {

  public static void setDebugLevel() {
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
        .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
    root.setLevel(ch.qos.logback.classic.Level.DEBUG);
  }

  public static void initialize() throws JoranException {
    String logName = "database-ops-" + UUID.randomUUID().toString() + ".log";
    String logFolder = System.getProperty("java.io.tmpdir");
    String logPath = logFolder+File.separator+logName;
    System.setProperty("log_path", logPath);
    System.out.println("Log file: " + logPath);    
    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    ContextInitializer ci = new ContextInitializer(lc);
    lc.reset();
    ci.autoConfig();     
    
  }
}
