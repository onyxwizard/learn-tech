package phase1.methodchain.intermediate;


/**
 * 🎯 Create a StringBuilder-like TextBuilder with:
 * .append(String), .upper(), .reverse(), .toString() — all chainable.
 * Test: "hello" → "OLLEH".
 */

public class TextBuilder {
  private StringBuilder word;
  
  TextBuilder(String word) {
    this.word = new StringBuilder(word);
  }

  TextBuilder append(String w) {
    this.word.append(w);
    return this;
  }

  TextBuilder upper() {
    for(int i=0;i<this.word.length();i++){
      char s = this.word.charAt(i);
      if(!Character.isUpperCase(s)){
        this.word.setCharAt(i, Character.toUpperCase(s));
      }
    }
    return this;
  }

  TextBuilder reverse() {
    this.word.reverse();
    return this;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return this.word.toString();
  }
  
  public static void main(String[] args) {
    TextBuilder word = new TextBuilder("Hello");
    System.out.println(word.reverse().upper().toString()); //OLLEH
  }
  
}
