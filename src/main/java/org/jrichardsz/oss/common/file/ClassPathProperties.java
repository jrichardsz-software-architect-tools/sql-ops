package org.jrichardsz.oss.common.file;

import java.io.InputStream;
import java.util.Properties;

public class ClassPathProperties {
  private static Properties properties = null;
  
  private static String propertiesFileName = "databases.properties";
  
  private static Properties getDefaultInstance() throws Exception {
    return getCustomInstance(ClassPathProperties.class
        .getClassLoader().getResourceAsStream(propertiesFileName));
  }
  
  private static Properties getCustomInstance(InputStream propertiesInputStream) throws Exception {
    properties = new Properties();
    properties.load(propertiesInputStream);
    propertiesInputStream.close();
    return properties;
  }
  
  public static String getProperty(String property) throws Exception {
    validateSingleton();
    String value = properties.getProperty(property);
    if (value == null)
      throw new Exception(
          String.format("Failed to read key %s in %s file", new Object[] { property, propertiesFileName })); 
    return value;
  }
  
  public static boolean hasProperty(String property) throws Exception {
    validateSingleton();
    String value = properties.getProperty(property);
    return (value != null);
  }
  
  private static void validateSingleton() throws Exception {
    if (properties == null)
      properties = getDefaultInstance(); 
  }
}
