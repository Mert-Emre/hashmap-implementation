// Used for finding a prime that is bigger than the given number.
// Useful for calculating new array size for hashmap.
public abstract class MathUtils {
  public static int findPrime(int num) {
    if (num <= 2) {
      return 2;
    }
    if (num == 3) {
      return 3;
    }
    int temp = num;
    if (temp % 2 == 0) {
      temp++;
    }

    while (true) {
      boolean increaseTemp = false;
      for (int i = 3; i <= Math.sqrt(temp); i += 2) {
        if (temp % i == 0) {
          increaseTemp = true;
          break;
        }
      }
      if (!increaseTemp) {
        break;
      }
      temp += 2;
    }
    return temp;
  }

}
