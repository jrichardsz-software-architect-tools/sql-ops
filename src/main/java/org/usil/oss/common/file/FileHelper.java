package org.usil.oss.common.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHelper {
  public static void detectRequiredPairs(ArrayList<String> mainFiles, ArrayList<String> otherFiles, String expectedExtension) throws Exception {
    if (mainFiles.size() != otherFiles.size())
      throw new Exception(String.format("The number of files does not match: scripts number is %s but rollbacks number is %s.", new Object[] { Integer.valueOf(mainFiles.size()), Integer.valueOf(otherFiles.size()) }) + "Scripts and rollbacks number should be the same. Example: If you have 3 .sql files, 3 .rollback files are required."); 
    for (String mainFile : mainFiles) {
      if (!otherFiles.contains(mainFile + expectedExtension))
        throw new Exception(String.format("This file %s does not have its required file %s", new Object[] { mainFile, expectedExtension })); 
    } 
  }
  
  public static String getFileAsStringFromClasspath(String file) throws IOException {
    ClassLoader classLoader = FileHelper.class.getClassLoader();
    InputStream classPathFileStream = classLoader.getResourceAsStream(file);
    InputStreamReader isReader = new InputStreamReader(classPathFileStream);
    BufferedReader reader = new BufferedReader(isReader);
    StringBuffer sb = new StringBuffer();
    String str;
    while ((str = reader.readLine()) != null)
      sb.append(str); 
    return sb.toString();
  }
  
  public static ArrayList<String> readFilesAtRoot(File dir, String filterRegexString) throws Exception {
    ArrayList<String> fileTree = new ArrayList<>();
    if (dir == null)
      throw new Exception("file to read is null"); 
    if ((dir.listFiles()).length == 0)
      return fileTree; 
    for (File entry : dir.listFiles()) {
      if (entry.isFile()) {
        Pattern pattern = Pattern.compile(filterRegexString);
        Matcher filterRegex = pattern.matcher(entry.getName());
        if (filterRegex.find())
          fileTree.add(entry.getAbsolutePath()); 
      } 
    } 
    Collections.sort(fileTree);
    return fileTree;
  }
}
