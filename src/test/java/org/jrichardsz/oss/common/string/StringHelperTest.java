package org.jrichardsz.oss.common.string;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class StringHelperTest {

  @Before
  public void stringHelperConstructor() throws Exception {
    new StringHelper();
  }

  @Test
  public void splitByEmptyLinesOne() throws Exception {
    String basePath = new File("").getAbsolutePath();
    String rawTextPath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/string/StringHelperTest/oneEmptyLine.txt";

    String rawText = new String(Files.readAllBytes(Paths.get(rawTextPath)));

    List<String> partials = StringHelper.splitByEmptyLines(rawText);
    assertEquals(3, partials.size());
    assertEquals("aaaaa", partials.get(0).trim());
    assertEquals("bbbbb", partials.get(1).trim());
    assertEquals("ccccc", partials.get(2).trim());
  }

  @Test
  public void splitByEmptyLinesSeveral() throws Exception {
    String basePath = new File("").getAbsolutePath();
    String rawTextPath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/string/StringHelperTest/severalEmptyLine.txt";

    String rawText = new String(Files.readAllBytes(Paths.get(rawTextPath)));

    List<String> partials = StringHelper.splitByEmptyLines(rawText);
    assertEquals("aaaaa", partials.get(0).trim());
    assertEquals("bbbbb", partials.get(1).trim());
    assertEquals("ccccc", partials.get(2).trim());
  }

  @Test
  public void splitByEmptyLinesSpaces() throws Exception {
    String basePath = new File("").getAbsolutePath();
    String rawTextPath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/string/StringHelperTest/severalSpacesLine.txt";

    String rawText = new String(Files.readAllBytes(Paths.get(rawTextPath)));

    List<String> partials = StringHelper.splitByEmptyLines(rawText);
    assertEquals(3, partials.size());
    assertEquals("aaaaa", partials.get(0).trim());
    assertEquals("bbbbb", partials.get(1).trim());
    assertEquals("ccccc", partials.get(2).trim());
  }

  @Test
  public void isComment() throws Exception {
    assertEquals(true, StringHelper.isComment("-- hello"));
    assertEquals(true, StringHelper.isComment("  -- hello"));
    assertEquals(true, StringHelper.isComment("  -- hello  "));
    assertEquals(false, StringHelper.isComment("--hello"));
  }

  @Test
  public void isCommentWithCommand() throws Exception {
    assertEquals(true, StringHelper
        .isCommentWithCommand("/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;"));
    assertEquals(true, StringHelper.isCommentWithCommand(
        "  /*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;  "));
    assertEquals(false, StringHelper
        .isCommentWithCommand("/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */"));
    assertEquals(false, StringHelper
        .isCommentWithCommand("/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT"));
  }

  @Test
  public void getMetadataIfExist() throws Exception {
    String basePath = new File("").getAbsolutePath();
    String rawTextPath = basePath + File.separator
        + "src/test/resources/org/usil/oss/common/string/StringHelperTest/scriptWithMetadata.txt";

    String rawText = new String(Files.readAllBytes(Paths.get(rawTextPath)));

    Map<String, String> metadata = StringHelper.getMetadataScript(rawText);

    assertEquals("true", metadata.get("metadata"));
    assertEquals("bar", metadata.get("foo"));
  }

  @Test
  public void getEmptyMetadataIfNotExist() throws Exception {

    Map<String, String> metadata = StringHelper.getMetadataScript("");
    assertEquals(0, metadata.size());
    metadata = StringHelper.getMetadataScript("\\nbar");
    assertEquals(0, metadata.size());
    metadata = StringHelper.getMetadataScript("fooo\\nbar");
    assertEquals(0, metadata.size());
  }

}
