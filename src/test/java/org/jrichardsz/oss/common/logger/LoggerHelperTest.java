package org.jrichardsz.oss.common.logger;

import org.junit.Before;
import org.junit.Test;

public class LoggerHelperTest {

  @Before
  public void loggerHelperConstrunctor() {
    new LoggerHelper();
  }

  @Test
  public void setDebugLevel() {
    LoggerHelper.setDebugLevel();
  }

}
