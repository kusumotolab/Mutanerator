package example;

public class BubbleSort {

  public int[] sort(int[] values) {

    for (int i = 0; i < values.length - 1; i++) {
      for (int j = 0; j < values.length - 1 - i; j++) {
        if (values[j + 1] >= values[j]) {
          int tmp = values[j + 1];
          values[j + 1] = values[j];
          values[j] = tmp;
        }
      }
    }
    return values;
  }
}
