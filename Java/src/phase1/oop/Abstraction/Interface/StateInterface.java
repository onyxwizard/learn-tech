package phase1.oop.Abstraction.Interface;

/**
 * Interface - State decalation
 * Java Automatically adds static and final to the variables
 * If variable is not instantiate then user needs to instantiate - if not you get a error : why?
 * because interface are contracts to work on behavior not state[fields]
 */

interface Addition {
  //int x; throws error : 
  int val=10; // works
}

public class StateInterface implements Addition {
  public static void main(String[] args){
    try{
      System.out.println(val);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}

