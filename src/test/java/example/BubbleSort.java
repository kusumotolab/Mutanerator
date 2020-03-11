package example;

public class BubbleSort {

  public int[] sort(int[] values) {

    for (int i = 0; i < values.length; i++) {
      for (int j = 0; j < values.length - i; j++) {
        if (values[j + 1] > values[j]) {
          int tmp = values[j];
          values[j] = values[j + 1];
          values[j + 1] = tmp;
        }
      }
    }
    return values;
  }
}
