package org.usil.oss.databaseops;

import java.lang.reflect.Field;
import org.junit.Test;
import org.mockito.Mockito;
import org.usil.oss.devops.databaseops.DatabaseOps;
import org.usil.oss.devops.databaseops.DatabaseOpsCmdEntrypoint;

public class DatabaseOpsCmdEntrypointTest {

  @Test
  public void constructor() {
    new DatabaseOpsCmdEntrypoint();
  }

  @Test
  public void shouldFailOnUnknownDriver() throws Exception {

    DatabaseOps databaseOps = Mockito.mock(DatabaseOps.class);
    Mockito.doReturn(null).when(databaseOps).perform(new String[] {"p1"});
    // danger: changing databaseOps in DatabaseOpsCmdEntrypoint
    setStatic(DatabaseOpsCmdEntrypoint.class.getDeclaredField("databaseOps"), databaseOps);
    DatabaseOpsCmdEntrypoint.main(new String[] {"p1"});
    // restoring databaseOps in DatabaseOpsCmdEntrypoint
    setStatic(DatabaseOpsCmdEntrypoint.class.getDeclaredField("databaseOps"), new DatabaseOps());
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
