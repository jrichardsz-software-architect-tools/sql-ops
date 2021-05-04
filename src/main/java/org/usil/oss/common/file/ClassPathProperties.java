package org.usil.oss.common.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClassPathProperties {

  private static Properties properties = null;
  private static String propertiesFileName = "databases.properties";

  public static Properties getDefaultInstance() throws Exception {
    return getCustomInstance(
        ClassPathProperties.class.getClassLoader().getResourceAsStream(propertiesFileName));
  }

  public static Properties getCustomInstance(InputStream propertiesInputStream) throws Exception {

    InputStream input = null;

    try {
      properties = new Properties();
      // load a properties file
      properties.load(propertiesInputStream);
      return properties;
    } catch (Exception ex) {
      throw new Exception("Failed to read plugin properties: " + propertiesFileName, ex);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          throw new Exception("Failed to close stream after read: " + propertiesFileName, e);
        }
      }
    }

  }

  public static String getProperty(String property) throws Exception {

    if (properties == null) {
      properties = getDefaultInstance();
    }

    String value = properties.getProperty(property);
    if (value == null) {
      throw new Exception(
          String.format("Failed to read key %s in %s file", property, propertiesFileName));
    }
    return value;
  }

  public static boolean hasProperty(String property) throws Exception {

    if (properties == null) {
      properties = getDefaultInstance();
    }

    String value = properties.getProperty(property);
    return value != null;
  }


}
