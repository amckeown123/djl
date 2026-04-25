package rule;


import javax.swing.*;


/**
 * The <code>Fact</code> class implements support for facts within
 * a rule base.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public class Fact {
  //BooleanRuleBase rb;
  String name;
  Clause fact;    //only 1 clause allowed
  Boolean truth;  // states = (null=unknown, true, or false)
  boolean fired = false;


  /**
   * Creates a <code>Fact</code> object as part of the given rule base
   * with the specified name and the fact clause.
   *
   * @param Rb the BooleanRuleBase object that this fact belongs to
   * @param Name the String object that contains the name of the fact
   * @param f the Clause object that contains the fact
   *
   */
  Fact(BooleanRuleBase Rb, String Name, Clause f) {
    
	BooleanRuleBase rb = Rb;
    name = Name;
    fact = f;
    rb.addFact(this);  // add self to fact list
    truth = null;
  }

 

  public void asserting(BooleanRuleBase rb) {
    if (fired == true) {
      return;  // only assert once
    }
    rb.trace("\nAsserting fact " + name);
    truth = true;
    fired = true;
    if (fact.lhs == null) {

      // it's an effector
      ((EffectorClause) fact).perform(rb);  // call the effector method
    } else {

      // set the variable value and update clauses
      fact.lhs.setValue(fact.rhs);

      // now retest any rules whose clauses just changed
    }
  }


  /**
   * Adds display information about this fact to the given text
   * area.
   *
   * @param textArea the JTextArea object to which the fact information
   *                 is added
   *
   */
  void display(JTextArea textArea) {
    textArea.append(name + ": ");
    textArea.append(fact.toString() + "\n");
  }
}
