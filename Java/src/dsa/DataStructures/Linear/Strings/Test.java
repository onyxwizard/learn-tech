package Linear.Strings;

public class Test {
  public static void main(String[] args) {
    StringBuilder sb = new StringBuilder("Hello");
sb.append(" ").insert(6, "World").reverse();
System.out.println(sb);
  }
}
