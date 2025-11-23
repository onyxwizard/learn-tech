package phase1.oop.nestedclass.innernestedclass;

public class OuterInnerClass {
  private int secret = 13 ;
  class Inner{
    void output(){
      System.out.println("secret " + secret);
    }
  }

  // public static void main(String[] args) {
  //   OuterInnerClass outer = new OuterInnerClass();
  //   OuterInnerClass.Inner inner = outer.new Inner();
  //   inner.output();
  // }
}
