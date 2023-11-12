package org.jrichardsz.oss.devops.sqlops;

import java.lang.reflect.Field;
import org.junit.Test;
import org.mockito.Mockito;
import org.jrichardsz.oss.devops.sqlops.SqlOps;
import org.jrichardsz.oss.devops.sqlops.SqlOpsCmdEntrypoint;

public class SqlOpsCmdEntrypointTest {

  @Test
  public void constructor() {
    new SqlOpsCmdEntrypoint();
  }

  @Test
  public void shouldFailOnUnknownDriver() throws Exception {

    SqlOps databaseOps = Mockito.mock(SqlOps.class);
    Mockito.doReturn(null).when(databaseOps).perform(new String[] {"p1"});
    // danger: changing databaseOps in DatabaseOpsCmdEntrypoint
    setStatic(SqlOpsCmdEntrypoint.class.getDeclaredField("databaseOps"), databaseOps);
    SqlOpsCmdEntrypoint.main(new String[] {"p1"});
    // restoring databaseOps in DatabaseOpsCmdEntrypoint
    setStatic(SqlOpsCmdEntrypoint.class.getDeclaredField("databaseOps"), new SqlOps());
    // TODO: if I'm not restoring, another tests like SqliteTest which uses DatabaseOpsCmdEntrypoint
    // is invoking the mock databaseOps, not the real
    // https://stackoverflow.com/a/34921820/3957754
  }

  // https://newbedev.com/mock-private-static-final-field-using-mockito-or-jmockit
  private static void setStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    field.set(null, newValue);
  }
}
