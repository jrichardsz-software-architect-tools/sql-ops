package org.usil.oss.common.file;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class FileHelperTest {


  @Before
  public void setup() {
    new FileHelper();
  }

  @Test
  public void readFilesAtRoot() throws Exception {
    String basePath = new File("").getAbsolutePath();
    String dir = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/file/FileHelperTest.listFileTreeSimple";
    ArrayList<String> files = FileHelper.readFilesAtRoot(new File(dir), ".sql$");
    assertEquals(3, files.size());
    assertEquals(basePath + File.separator
        + "src/test/resources/org/usil/oss/common/file/FileHelperTest.listFileTreeSimple/001.sql",
        files.get(0));
    assertEquals(basePath + File.separator
        + "src/test/resources/org/usil/oss/common/file/FileHelperTest.listFileTreeSimple/002.sql",
        files.get(1));
    assertEquals(basePath + File.separator
        + "src/test/resources/org/usil/oss/common/file/FileHelperTest.listFileTreeSimple/003.sql",
        files.get(2));
  }

  @Test
  public void readFilesAtRootNull() throws Exception {

    try {
      FileHelper.readFilesAtRoot(null, ".sql$");
    } catch (Exception e) {
      assertEquals("root dir is null", e.getMessage());
    }
  }

  @Test
  public void readFilesAtRootEmpty() throws Exception {

    String emptyFolder =
        System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString();
    Path path = Paths.get(emptyFolder);
    Files.createDirectory(path);

    ArrayList<String> files = FileHelper.readFilesAtRoot(new File(emptyFolder), ".sql$");
    assertEquals(0, files.size());
  }

  @Test
  public void detectRequiredPairSuccess() throws Exception {
    String basePath = new File("").getAbsolutePath();
    String dir = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/file/FileHelperTest.detectRequiredPairSuccess";

    ArrayList<String> mainFiles = FileHelper.readFilesAtRoot(new File(dir), ".sql$");
    ArrayList<String> otherFiles = FileHelper.readFilesAtRoot(new File(dir), ".rollback$");
    FileHelper.detectRequiredPairs(mainFiles, otherFiles, ".rollback");

  }

  @Test
  public void detectRequiredPairErrorNumber() throws Exception {

    ArrayList<String> mainFiles = new ArrayList<String>();
    mainFiles.add("001.sql");
    mainFiles.add("002.sql");
    mainFiles.add("003.sql");

    ArrayList<String> otherFiles = new ArrayList<String>();
    mainFiles.add("001.sql.rollback");
    mainFiles.add("003.sql.rollback");

    try {
      FileHelper.detectRequiredPairs(mainFiles, otherFiles, ".rollback");
    } catch (Exception e) {
      assertEquals("The number of files does not match.", e.getMessage());
    }
  }

  @Test
  public void detectRequiredPairErrorMissingRollback() throws Exception {
    String basePath = new File("").getAbsolutePath();
    String dir = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/file/FileHelperTest.detectRequiredPairErrorMissingRollback";

    ArrayList<String> mainFiles = FileHelper.readFilesAtRoot(new File(dir), ".sql$");
    ArrayList<String> otherFiles = FileHelper.readFilesAtRoot(new File(dir), ".rollback$");

    try {
      FileHelper.detectRequiredPairs(mainFiles, otherFiles, ".rollback");
    } catch (Exception e) {
      assertEquals(String.format("This file %s does not have its required file %s",
          dir + "/002.sql", ".rollback"), e.getMessage());
    }

  }

  @Test
  public void getFileAsStringFromClasspath() throws Exception {
    String simpleClassPathFileContent = FileHelper.getFileAsStringFromClasspath(
        "org/usil/oss/common/file/FileHelperTest.getFileAsStringFromClasspath/classpathfile.txt");
    assertEquals("javadabadoo", simpleClassPathFileContent);
  }

}
