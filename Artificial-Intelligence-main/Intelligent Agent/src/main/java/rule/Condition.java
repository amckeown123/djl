package rule;




/**
 * The <code>Condition</code> class implements the conditional test within a
 * clause.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */

public class Condition {
  int index;
  String symbol;


  /**
   * Creates a <code>Condition</code> with the given conditional test.
   *
   * @param symbol the String that represents the conditional test (=, <, >, or !=)
   */
  public Condition(String symbol) {
    this.symbol = symbol;
    if (symbol.equals("=")) {
      index = 1;
    } else if (symbol.equals(">")) {
      index = 2;
    } else if (symbol.equals("<")) {
      index = 3;
    } else if (symbol.equals("!=")) {
      index = 4;
    } else {
      index = -1;
    }
  }


  /**
   * Converts the <code>Condition</code> to a format that can be displayed.
   *
   * @return the <code>Condition</code> in string format
   */
  public String toString() {
    return symbol;
  }
}
