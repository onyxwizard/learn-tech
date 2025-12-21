package Iterator.challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;


public class Test2 {
  public static void main(String[] args) {
    List<String> items = new ArrayList<>(Arrays.asList("A", "B", "C"));
    Iterator<String> it = items.iterator();

    it.next(); // "A"
    it.next(); // "B"
    items.remove("C");
    System.out.println("Status : " + it.hasNext()); 
    
    // Try to access get ConcurrentModificationEx
    //String s = it.next();
    
    
    items.add("D");
    System.out.println("Status : " + it.hasNext()); 
    String s = it.next();
  }
}
