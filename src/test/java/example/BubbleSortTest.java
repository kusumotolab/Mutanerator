package example;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class BubbleSortTest {

  @Test
  public void test01() {
    assertArrayEquals(new int[] {1, 2, 3}, new BubbleSort().sort(new int[] {1, 2, 3}));
  }

  @Test
  public void test03() {
    assertArrayEquals(new int[] {1, 2, 3}, new BubbleSort().sort(new int[] {3, 2, 1}));
  }

  @Test
  public void test04() {
    assertArrayEquals(new int[] {1, 2, 3}, new BubbleSort().sort(new int[] {2, 1, 3}));
  }
}
