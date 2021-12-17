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

    setStatic(DatabaseOpsCmdEntrypoint.class.getDeclaredField("databaseOps"), databaseOps);
    DatabaseOpsCmdEntrypoint.main(new String[] {"p1"});
  }

  // https://newbedev.com/mock-private-static-final-field-using-mockito-or-jmockit
  private static void setStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    field.set(null, newValue);
  }
}
