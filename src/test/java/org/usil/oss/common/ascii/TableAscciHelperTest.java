package org.usil.oss.common.ascii;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class TableAscciHelperTest {

  @Before
  public void setup() {
    new TableAscciHelper();
  }

  @Test
  public void createSimpleTable() throws Exception {
    ArrayList<Object> r1 = new ArrayList<Object>();
    r1.add("a");
    r1.add("aa");
    r1.add("aaa");

    ArrayList<Object> r2 = new ArrayList<Object>();
    r2.add("b");
    r2.add("bb");
    r2.add("bbb");

    ArrayList<Object> r3 = new ArrayList<Object>();
    r3.add("c");
    r3.add("cc");
    r3.add("ccc");

    ArrayList<Object> data = new ArrayList<>();
    data.add(r1);
    data.add(r2);
    data.add(r3);

    String table = TableAscciHelper.createSimpleTable(data);
    String[] rows = table.split("\n");
    assertEquals(3, rows.length);
    assertEquals("a  ||aa  ||aaa  ||", rows[0]);
    assertEquals("b  ||bb  ||bbb  ||", rows[1]);
    assertEquals("c  ||cc  ||ccc  ||", rows[2]);
  }

}
