package example;

public class CloseToZero {

  public int closeToZero(int value) {
    if (0 < value) {
      value--;
    }
    if (value < 0) {
      value++;
    }
    return 0;
  }
}
