package Iterator.challenges;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Test {
  public static void main(String[] args) {
    List<String> words = Arrays.asList("alpha", "beta", "gamma");
    Iterator iter = words.iterator();
    System.out.println("========Iterator ==================");
    while (iter.hasNext()) {
      System.out.println(iter.next());
    }
    System.out.println("------------   ---------------");
    System.out.println("Final Status : "+ iter.hasNext());
    System.out.println("============    ==============");
    // Try to access get NoSuchElementEx 
    iter.next();
  }
}
