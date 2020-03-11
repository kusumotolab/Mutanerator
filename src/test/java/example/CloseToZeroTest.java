package example;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CloseToZeroTest {

  @Test
  public void test01() {
    assertEquals(1, new CloseToZero().closeToZero(2));
  }

  @Test
  public void test03() {
    assertEquals(0, new CloseToZero().closeToZero(0));
  }

  @Test
  public void test04() {
    assertEquals(-1, new CloseToZero().closeToZero(-2));
  }
}
