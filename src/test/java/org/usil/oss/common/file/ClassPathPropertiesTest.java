package org.usil.oss.common.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class ClassPathPropertiesTest {

  @Before
  public void setUp() throws Exception {
    new ClassPathProperties();
  }

  @Test
  public void readExistentProperty() throws Exception {
    assertEquals("com.mysql.cj.jdbc.Driver", ClassPathProperties.getProperty("dummy1.driver"));
  }

  @Test
  public void failOnInexistentProperty() throws Exception {

    try {
      assertEquals("baz", ClassPathProperties.getProperty("foo.bar"));
      fail("My method didn't throw when I expected it to: key don't exist on properties");
    } catch (Exception e) {
    }
  }

}
