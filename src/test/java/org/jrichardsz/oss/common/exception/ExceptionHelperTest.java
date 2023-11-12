package org.jrichardsz.oss.common.exception;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class ExceptionHelperTest {

  @Before
  public void exceptionHelperConstrunctor() {
    new ExceptionHelper();
  }

  @Test
  public void summarizedNoPretty() {

    try {
      throw new Exception("Im a jerk 1");
    } catch (Exception e1) {
      try {
        throw new Exception("Im a jerk 2", e1);
      } catch (Exception e2) {
        String[] summarizedError = ExceptionHelper.summarizeTrace(e2, false).split(",");
        assertEquals(true,
            summarizedError[0].contains("Caused by: class java.lang.Exception:Im a jerk 2"));
        assertEquals(true,
            summarizedError[1].contains("Caused by: class java.lang.Exception:Im a jerk 1"));
      }
    }
  }

  @Test
  public void summarizedPretty() {

    try {
      throw new Exception("Im a jerk 1");
    } catch (Exception e1) {
      try {
        throw new Exception("Im a jerk 2", e1);
      } catch (Exception e2) {
        String[] summarizedError = ExceptionHelper.summarizeTrace(e2, true).split("\\n\\n");
        assertEquals(true,
            summarizedError[1].contains("Caused by: class java.lang.Exception:Im a jerk 2"));
        assertEquals(true,
            summarizedError[2].contains("Caused by: class java.lang.Exception:Im a jerk 1"));
      }
    }
  }
}
